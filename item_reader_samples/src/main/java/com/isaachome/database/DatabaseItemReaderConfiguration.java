package com.isaachome.database;

import com.isaachome.mapper.CustomerMapper;
import com.isaachome.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseItemReaderConfiguration {

    @Autowired
    private DataSource dataSource;

//    @Bean
//	public JdbcCursorItemReader<Customer> cursorItemReader() {
//		JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
//
//		reader.setSql("select id, firstName, lastName, birthdate from customer order by lastName, firstName");
//		reader.setDataSource(this.dataSource);
//		reader.setRowMapper(new CustomerMapper());
//
//		return reader;
//	}
    @Bean
    public JdbcPagingItemReader<Customer> customerJdbcPagingItemReader(){
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, firstName,lastName,birthdate");
        queryProvider.setFromClause("FROM CUSTOMER");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
      return  new JdbcPagingItemReaderBuilder<Customer>()
              .name("customerJdbcPagingItemReader")
              .dataSource(dataSource)
              .fetchSize(10)
              .rowMapper(new CustomerMapper())
              .queryProvider(queryProvider)
              .sortKeys(sortKeys)
              .build();
    }
    @Bean
    public ItemWriter<Customer> itemWriter(){
        System.out.println("Item-Reader");
        return chunk -> chunk.forEach(System.out::println);
    }
//@Bean
//public ItemWriter<Customer> customerItemWriter() {
//    return items -> {
//        for (Customer item : items.getItems()) {
//            System.out.println(item.toString());
//        }
//    };
//}

    @Bean
    public Step ste(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                    ItemReader<Customer> customerJdbcPagingItemReader,
                    ItemWriter<Customer> customerItemWriter){
     return  new StepBuilder("step",jobRepository)
             .<Customer,Customer>chunk(10,transactionManager)
             .reader(customerJdbcPagingItemReader)
             .writer(customerItemWriter)
             .build();
    }
    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return  new JobBuilder("job-",jobRepository).incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
