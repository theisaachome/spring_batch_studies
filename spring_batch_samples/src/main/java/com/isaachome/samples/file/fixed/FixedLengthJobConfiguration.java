package com.isaachome.samples.file.fixed;

import com.isaachome.samples.model.Customer;
import com.isaachome.samples.model.CustomerCredit;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FixedLengthJobConfiguration {

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerCredit> customerCreditReader(
            @Value("#{jobParameters[inputFile]}") Resource resource
    ) {
        return new FlatFileItemReaderBuilder<CustomerCredit>()
                .name("customerCreditReader")
                .resource(resource)
//                .resource(new ClassPathResource("/input/customerCredits.csv"))
                .fixedLength()
                .columns(new Range(1,9),new Range(10,11))
                .names("name","credit")
                .targetType(CustomerCredit.class)
                .build();
    }

    @Bean
    public  FlatFileItemReader<Customer> customerFlatFileItemReader(){
        return  new FlatFileItemReaderBuilder<Customer>()
                .name("customerFlatFileItemReader")
                .resource(new ClassPathResource("/input/customerFixedWidth.txt"))
                .fixedLength()
                .columns(new Range[]{new Range(1,11), new Range(12, 12), new Range(13, 22),
						new Range(23, 26), new Range(27,46), new Range(47,62), new Range(63,64),
						new Range(65,69)})
                .names("firstName", "middleInitial", "lastName",
						"addressNumber", "street", "city", "state","zipCode")
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public  ItemWriter<Customer> customerItemWriter(){
        return  (items)->{items.forEach(System.out::println);};
    }
    @Bean
    @StepScope
    public FlatFileItemWriter<CustomerCredit> customerCreditWriter(
            @Value("#{jobParameters[outputFile]}") WritableResource resource) {
        return new FlatFileItemWriterBuilder<CustomerCredit>().name("itemWriter")
                .resource(resource)
                .formatted()
                .format("%-9s%-2.0f")
                .names("name", "credit")
                .build();
    }


    @Bean
    public Step customerCreditReaderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                         ItemReader<Customer> customerCreditReader,
                                         ItemWriter<Customer> customerCreditWriter){
        return  new StepBuilder("customerCreditReaderStep",jobRepository)
                .<Customer,Customer>chunk(20,transactionManager)
                .reader(customerCreditReader)
                .writer(customerCreditWriter)
                .build();
    }
    @Bean
    public Job fixedLengthJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              Step customerCreditReaderStep) {
        return  new JobBuilder("fixedLengthJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerCreditReaderStep)
                .build();
    }

}
