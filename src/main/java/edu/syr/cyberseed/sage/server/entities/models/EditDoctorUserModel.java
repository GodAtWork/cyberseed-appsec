package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Data
public class EditDoctorUserModel {

    @NotNull
    @Size(min = 1, max = 255)
    private String username;

    @Size(min = 15, max = 255)
    private String password;

    @Size(min = 1, max = 255)
    private String fname;

    @Size(min = 1, max = 255)
    private String lname;

    @Size(min = 1, max = 255)
    private String practiceName;

    @Size(min = 1, max = 255)
    private String practiceAddress;

    @Size(min = 1, max = 255)
    private String recoveryPhrase;
}
