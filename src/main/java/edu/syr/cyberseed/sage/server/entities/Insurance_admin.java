package edu.syr.cyberseed.sage.server.entities;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.*;
import java.util.Set;
import java.lang.String;

@Entity
@Table(name = "insurance_admin")
public class Insurance_admin {

//    private static final long serialVersionUID = -3009157732241241604L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;

    @Column(name = "Cname")
    private String cname;

    @Column(name = "Caddress")
    private String caddress;

    //private Set<User> username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {this.cname = cname;}

    public String getCaddress() { return caddress; }
    public void setCaddress(String caddress) {this.caddress = caddress;}
    //@ManyToMany(mappedBy = "usernames")
    //public Set<User> getUsername() {
    //return username;
    //}

    public void setUsers(Set<User> users) {
        this.username = username;
    }

    protected Insurance_admin() {
    }

    public Insurance_admin(String username, String pname) {
        this.username = username;
        this.cname = cname;
    }

    @Override
    public String toString() {
        return String.format("Insurance_admin[username=%s, cname='%s', caddress='%s']", username, cname, caddress);
    }
}