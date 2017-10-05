package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement
@Data
public class DiagnosisRecordModel {

    @Range(min = 1l, max=999999999)
    private Integer id;

    @NotNull
    private Date DiagnosisDate;

    @NotNull
    @Size(min = 1, max = 255)
    private String patientUsername;

    @NotNull
    @Size(min = 1, max = 255)
    private String doctorUsername;

    @NotNull
    @Size(min = 1, max = 255)
    private String diagnosis;

    // optional
    private List<String> edit;

    //optional
    private List<String> view;

}
