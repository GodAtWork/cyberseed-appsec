package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Data
public class DoctorUserModel {

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
    @Size(min = 1, max = 255)
    private String practiceName;

    @NotNull
    @Size(min = 1, max = 255)
    private String practiceAddress;

    @NotNull
    @Size(min = 1, max = 255)
    private String recoveryPhrase;
}
