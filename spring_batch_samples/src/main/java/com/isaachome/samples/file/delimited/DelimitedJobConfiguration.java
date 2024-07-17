package com.isaachome.samples.file.delimited;

import com.isaachome.samples.mappers.CustomerFieldMapper;
import com.isaachome.samples.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DelimitedJobConfiguration {

//    @Bean
//    public FlatFileItemReader<Customer> customerReader() {
//        return new FlatFileItemReaderBuilder<Customer>()
//                .name("customerReaderDelimitedFile")
//                .resource(new ClassPathResource("/input/customerDelimited.csv"))
//                .delimited()
//                .names("firstName", "middleInitial", "lastName",
//                        "addressNumber", "street", "city", "state","zipCode")
//                .targetType(Customer.class)
//                .build();
//    }
    @Bean
    public FlatFileItemReader<Customer> customerReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerReaderDelimitedFile")
                .resource(new ClassPathResource("/input/customerDelimited.csv"))
                .delimited()
                .names("firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state","zipCode")
                .fieldSetMapper(new CustomerFieldMapper())
                .build();
    }
    @Bean
    public ItemWriter<Customer> customerWriter() {
        return  chunk -> chunk.forEach(System.out::println);
    }

    @Bean
    public Job copyCustomerFile(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                ItemReader<Customer> itemReader,
                                ItemWriter<Customer> itemWriter){
        return new JobBuilder("customerReaderDelimitedFile",jobRepository)
                .start(
                        new StepBuilder("copy",jobRepository)
                                .<Customer,Customer>chunk(40,transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build())
                .build();

    }
}
