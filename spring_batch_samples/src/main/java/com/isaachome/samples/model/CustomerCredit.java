package com.isaachome.samples.model;

import java.math.BigDecimal;
import java.util.Objects;


public class CustomerCredit {
    private String name;
    private BigDecimal credit;

    public CustomerCredit() {
    }

    public CustomerCredit(String name, BigDecimal credit) {
        this.name = name;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerCredit that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(credit, that.credit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, credit);
    }

    @Override
    public String toString() {
        return "CustomerCredit{" +
                "name='" + name + '\'' +
                ", credit=" + credit +
                '}';
    }
}
