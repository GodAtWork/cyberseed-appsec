package edu.syr.cyberseed.sage.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "record")
public class Record implements Serializable {

    private static final long serialVersionUID = -3009157732241241604L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long recordID;

    @Column(name = "Owner")
    private String owner;

    @Column(name = "Patient")
    private String patient;

    protected Record() {
    }

    public Record(String owner, String patient) {
        this.owner = owner;
        this.patient = patient;
    }

}