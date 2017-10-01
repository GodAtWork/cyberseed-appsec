package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Data
public class PatientUserModel {

    @NotNull
    @Size(min = 1, max = 255)
    private String username;

    @NotNull
    @Size(min = 15, max = 255)
    private String password;

    @NotNull
    @Size(min = 1, max = 255)
    private String fname;

    @NotNull
    @Size(min = 1, max = 255)
    private String lname;

    @NotNull
    private Date dob;

    @NotNull
    @Range(min = 0l, max=999999999)
    private Integer ssn;

    @NotNull
    @Size(min = 1, max = 255)
    private String address;
}
