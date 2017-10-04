package edu.syr.cyberseed.sage.server.entities;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.*;
import java.util.Set;
import java.lang.String;

@Entity
@Table(name = "medical_admin")
public class Medical_admin {

//    private static final long serialVersionUID = -3009157732241241604L;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "pname")
    private String pname;

    @Column(name = "paddress")
    private String paddress;

    @Column(name = "adoctor")
    private String adoctor;

    @Column(name = "anurse")
    private String anurse;

    //private Set<User> username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {this.pname = pname;}

    public String getPaddress() { return paddress; }
    public void setPaddress(String paddress) {this.paddress = paddress;}

    public String getDoctor() {
        return adoctor;
    }
    public void setDoctor(String doctor) {this.adoctor = doctor;}

    public String getAnurse() {return anurse;}
    public void setAnurse(String anurse) {this.anurse = anurse;}

    //@ManyToMany(mappedBy = "usernames")
    //public Set<User> getUsername() {
    //return username;
    //}

    public void setUsers(Set<User> users) {
        this.username = username;
    }

    protected Medical_admin() {
    }

    public Medical_admin(String username, String pname, String paddress, String adoctor, String anurse) {
        this.username = username;
        this.pname = pname;
        this.paddress=paddress;
        this.adoctor=adoctor;
        this.anurse=anurse;
    }

    @Override
    public String toString() {
        return String.format("Medical_admin[username=%s, pname='%s', paddress='%s', doctor='%s', anurse='%s']", username, pname, paddress, adoctor, anurse);
    }
}
