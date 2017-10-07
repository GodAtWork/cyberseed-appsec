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

/**
 *
 * @author DhruvVerma
 */
@Entity
@Table(name = "record_raw")
@Data
public class RawRecord {

    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "file", nullable = false, columnDefinition = "BINARY(255)", length = 255)
    private byte[] file;

    @Column(name = "length")
    private Integer length;

    protected RawRecord() {

    }

    public RawRecord (Integer id, String description, byte[] file, Integer length) {
        this.id = id;
        this.description = description;
        this.file = file;
        this.length = length;

    }
}