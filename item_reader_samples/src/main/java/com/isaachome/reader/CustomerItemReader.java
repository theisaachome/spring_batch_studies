package com.isaachome.reader;

import com.isaachome.model.Customer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

public class CustomerItemReader implements ItemReader<Customer> {

    private final List<Customer> data;

    public CustomerItemReader(List<Customer> customers) {
        this.data =  customers;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        for (Customer customer : data) {
            return customer;
        }
        return null;
    }
}
