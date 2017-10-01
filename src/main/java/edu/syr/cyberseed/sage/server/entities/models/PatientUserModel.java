package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
public class UserModel {

    @NotNull
    @Size(min = 3, max = 28)
    private String username;

    @NotNull
    @Size(min = 8, max = 28)
    private String password;

    @NotNull
    @Size(min = 1, max = 28)
    private String fname;

    @NotNull
    @Size(min = 1, max = 28)
    private String lname;

    @NotNull
    @Size(min = 1, max = 28)
    private String dob;

    @NotNull
    @Size(min = 1, max = 28)
    private String ssn;

    @NotNull
    @Size(min = 1, max = 28)
    private String Address;
}
