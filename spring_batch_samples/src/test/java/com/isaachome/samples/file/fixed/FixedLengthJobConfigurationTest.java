package com.isaachome.samples.file.fixed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FixedLengthJobConfigurationTest {
    private JobLauncherTestUtils jobLauncherTestUtils ;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils = new JobLauncherTestUtils();
    }
    @Test
    void testLaunchJobWithXmlConfig() throws Exception {
        String path = "/input/customerCredits.csv";
        String fileName = "test.zip";
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputFile", path)
                .addString("outputFile", "/output/fixedLengthOutput.txt")
                .toJobParameters();

        // when
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

}