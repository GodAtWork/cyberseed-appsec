/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.cyberseed.sage.server.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DhruvVerma
 */
@Entity
@Table(name = "record_correspondence")
@Data
public class CorrespondenceRecord {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "doctor")
    private String doctor;
    
    @Column(name = "note_date")
    private Date note_date;

    @Column(name = "note_text")
    private String note_text;

    private CorrespondenceRecord(){}

    public CorrespondenceRecord(Integer id, String doctor)
    {
        this.id=id;
        this.doctor=doctor;
    }

    public CorrespondenceRecord(Integer id, String doctor, Date d, String n)
    {
        this.id=id;
        this.doctor=doctor;
        this.note_date=d;
        this.note_text=n;
    }



}