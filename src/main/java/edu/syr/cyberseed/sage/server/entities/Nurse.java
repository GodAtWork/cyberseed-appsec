package edu.syr.cyberseed.sage.server.entities;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.*;
import java.util.Set;
import java.lang.String;

@Entity
@Table(name = "nurse")
public class Nurse {

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

    public String getAdoctor() {
        return adoctor;
    }
    public void setAdoctor(String adoctor) {this.adoctor = adoctor;}

    //@ManyToMany(mappedBy = "usernames")
    //public Set<User> getUsername() {
    //return username;
    //}

    public void setUsers(Set<User> users) {
        this.username = username;
    }

    protected Nurse() {
    }

    public Nurse(String username, String pname, String paddress, String adoctor) {
        this.username = username;
        this.pname = pname;
        this.paddress= paddress;
        this.adoctor=adoctor;
    }

    @Override
    public String toString() {
        return String.format("Nurse[username=%s, pname='%s', paddress='%s', adoctor='%s']", username, pname, paddress, adoctor);
    }
}
