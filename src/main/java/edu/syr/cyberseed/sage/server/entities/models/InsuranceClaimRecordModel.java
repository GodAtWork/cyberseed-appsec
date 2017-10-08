package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement
@Data
public class InsuranceClaimRecordModel {

    @Range(min = 1l, max=999999999)
    private Integer id;

    @NotNull
    private Date date;

    private Date ins_date;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String patientUsername;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String medadUsername;

    @NotNull
    @Size(min = 1, max = 255)
    private String status;

    @NotNull
    private Float amount;

    // optional
    private List<String> edit;

    //optional
    private List<String> view;

}
