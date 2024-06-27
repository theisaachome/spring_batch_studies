package com.isaachome.mapper;

import com.isaachome.model.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet field) throws BindException {
        return new Customer(
                field.readLong("id"),
                field.readString("firstName"),
                field.readString("lastName"),
                field.readDate("birthdate","yyyy-MM-dd HH:mm:ss")
        );
    }
}
