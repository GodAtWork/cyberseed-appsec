/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.cyberseed.sage.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DhruvVerma
 */
@Entity
@Table(name = "record_insurance")
public class Record_insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id")
    private long id;

    @Column(name = "madmin")
    private String madmin;
    
    @Column(name = "status")
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMadmin() {
        return madmin;
    }

    public void setMadmin(String madmin) {
        this.madmin = madmin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status.toLowerCase() == "filed" || status.toLowerCase() == "examining" || status.toLowerCase() == "rejected" || status.toLowerCase() == "accepted" || status.toLowerCase() == "paid" )
        this.status = status.toLowerCase();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
      @Column(name = "date")
    private Date date;
      
      @Column(name = "amount")
    private Float amount; 

}