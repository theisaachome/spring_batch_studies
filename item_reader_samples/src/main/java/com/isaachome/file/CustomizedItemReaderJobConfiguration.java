package com.isaachome.file;

import com.isaachome.model.Customer;
import com.isaachome.reader.CustomerItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomizedItemReaderJobConfiguration {

    @Bean
    public CustomerItemReader customerItemReader(){

        List<Customer> customers =new ArrayList<>(3);
        customers.add(new Customer(1L,"John","Muang", Date.valueOf(LocalDate.now())));
        customers.add(new Customer(1L,"Peter","Aung", Date.valueOf(LocalDate.now())));
        customers.add( new Customer(1L,"Maung","Mang", Date.valueOf(LocalDate.now())));
        return new CustomerItemReader(customers);

    }
    @Bean
    public ItemWriter<Customer> customerItemWriter(){
        return chunk -> chunk.forEach(System.out::println);
    }
    @Bean
    public Step step1(JobRepository jobRepository,PlatformTransactionManager transactionManager,
                      ItemReader<Customer> itemReader,
                      ItemWriter<Customer> itemWriter){
       return new StepBuilder("my-step",jobRepository)
                .<Customer,Customer>chunk(1,transactionManager)
                .reader(itemReader)
                .writer(itemWriter).build();
    }
    @Bean
    public  Job job(JobRepository jobRepository, Step step){
        return  new JobBuilder("job-customzed",jobRepository).incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
