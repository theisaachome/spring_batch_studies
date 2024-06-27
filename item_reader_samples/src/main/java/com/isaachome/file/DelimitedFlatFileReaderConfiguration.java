package com.isaachome.file;

import com.isaachome.mapper.CustomerFieldSetMapper;
import com.isaachome.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

//@Configuration
public class DelimitedFlatFileReaderConfiguration {

    @Bean
    public FlatFileItemReader<Customer> customerFlatFileItemReader(){
        return  new FlatFileItemReaderBuilder<Customer>()
                .name("customerFlatFileItemReader")
                .resource(new ClassPathResource("/data/customer.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id","firstName","lastName","birthdate")
                .fieldSetMapper(new CustomerFieldSetMapper())
                .build();
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter(){
        return chunk -> chunk.forEach(System.out::println);
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
        return  new JobBuilder("job",jobRepository)
                .start(step)
                .build();
    }
}
