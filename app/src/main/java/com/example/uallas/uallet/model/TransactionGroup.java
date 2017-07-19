package com.example.uallas.uallet.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Uallas on 19/06/2017.
 */

public class TransactionGroup {

    private int id;
    private Date date;
    private List<Transaction> transactions;

    public TransactionGroup() {
    }

    public TransactionGroup(int id, Date date, List<Transaction> transactions) {
        this.id = id;
        this.date = date;
        this.transactions = transactions;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
