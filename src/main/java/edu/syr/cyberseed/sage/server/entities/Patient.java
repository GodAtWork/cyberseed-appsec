package edu.syr.cyberseed.sage.server.entities;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.lang.String;
import java.util.Date;

@Entity
@Table(name = "patient")
public class Patient {

//    private static final long serialVersionUID = -3009157732241241604L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;

    @Column(name = "Dob")
    private Date dob;

    @Column(name = "Ssn")
    private int ssn;

    @Column(name = "Address")
    private String address;

    //private Set<User> username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public Date getDob() { return dob; }
    public void setDob(Date dob) {this.dob = dob;}

    public Integer getSsn() { return ssn; }

    public void setSsn(Integer ssn) {this.ssn = ssn;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {this.address = address;}

    //@ManyToMany(mappedBy = "usernames")
    //public Set<User> getUsername() {
    //return username;
    //}

    public void setUsers(Set<User> users) {
        this.username = username;
    }

    protected Patient() {
    }

    public Patient(String username, String address) {
        this.username = username;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Patient[username=%s, ssn='%d', address='%s']", username, ssn, address);
    }
}
