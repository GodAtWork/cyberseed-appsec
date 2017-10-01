package edu.syr.cyberseed.sage.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id")
    private long id;

    @Column(name = "record_type")
    private String record_type;
    
    @Column(name = "edit")
    private String edit_permissions;

    @Column(name = "view")
    private String view_permissions;
    
    @Column(name = "owner")
    private String owner;
    
    @Column(name = "patient")
    private String patient;
    
     @Column(name = "date")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public String getEdit_permissions() {
        return edit_permissions;
    }

    public void setEdit_permissions(String edit_permissions) {
        this.edit_permissions = edit_permissions;
    }

    public String getView_permissions() {
        return view_permissions;
    }

    public void setView_permissions(String view_permissions) {
        this.view_permissions = view_permissions;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected Record() {
    }

    public Record(String owner, String patient) {
        this.owner = owner;
        this.patient = patient;
    }

    @Override
    public String toString() {
        return String.format("Record[recordID=%d, patient='%s', owner='%s']", id, patient, owner);
    }

}