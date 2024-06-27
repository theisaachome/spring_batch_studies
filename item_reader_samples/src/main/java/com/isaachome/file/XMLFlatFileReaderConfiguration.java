package com.isaachome.file;

import com.isaachome.model.Customer;
import com.thoughtworks.xstream.security.ExplicitTypePermission;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class XMLFlatFileReaderConfiguration {


    @Bean
    public XStreamMarshaller xStreamMarshaller(){
        XStreamMarshaller marshaller = new XStreamMarshaller();
//        marshaller.setAliases(Map.of("id",Long.class,
//                "firstName",String.class,
//                "lastName",String.class,
//                "birthdate", Date.class));
        Map<String,Class<Customer>> map = new HashMap<>();
        map.put("customer", Customer.class);
        marshaller.setAliases(Map.of("customer", Customer.class));
        marshaller.setTypePermissions(new ExplicitTypePermission(new Class[]{Customer.class}));
        return marshaller;

    }

    @Bean
    public StaxEventItemReader<Customer> customerXMLReader() {
        return new StaxEventItemReaderBuilder<Customer>()
                .name("customerXMLReader")
                .resource(new ClassPathResource("/data/customer.xml"))
                .addFragmentRootElements("customer")
                .unmarshaller(xStreamMarshaller())
                .build();
    }


    @Bean
    public ItemWriter<Customer> customerItemWriter(){
        return chunk -> chunk.getItems().forEach(System.out::println);
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      ItemReader<Customer> itemReader,
                     ItemWriter<Customer> itemWriter){
        return  new StepBuilder("step",jobRepository)
                .<Customer,Customer>chunk(10,transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }
    @Bean
    public Job job(JobRepository jobRepository,Step step){
        return  new JobBuilder("job-1000",jobRepository).incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
