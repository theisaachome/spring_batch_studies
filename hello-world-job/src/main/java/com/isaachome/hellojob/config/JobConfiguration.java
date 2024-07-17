package com.isaachome.hellojob.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Log4j2
@Configuration
public class JobConfiguration {

    @Bean
    public Job job(JobRepository jobRepository,Step step1,Step step2){
    return  new JobBuilder("job",jobRepository)
            .start(step2)
            .next(step2)
            .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("hello-step-1",jobRepository)
                .tasklet((ch,ctx)->{
                    System.out.println("Hello From Step");
                    return  RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }
    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      Tasklet demoTasklet){
        return new StepBuilder("hello-step-2",jobRepository)
                .tasklet(demoTasklet,transactionManager)
                .build();
    }
    @Bean
    @StepScope
    public Tasklet demoTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                StepExecution stepExecution = contribution.getStepExecution();
                JobParameters jobParameters = stepExecution.getJobParameters();
               String firstName=  jobParameters.getString("firstName");
               String lastName=  jobParameters.getString("lastName");
                log.info("Hello World Job {} {}", firstName, lastName);
                return RepeatStatus.FINISHED;
            }
        };
    }
}
