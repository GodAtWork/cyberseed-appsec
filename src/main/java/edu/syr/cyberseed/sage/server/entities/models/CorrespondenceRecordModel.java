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
public class CorrespondenceRecordModel {

    @Range(min = 1l, max=999999999)
    private Integer id;

    @Range(min = 1, max=999999999)
    private Integer note_id;

    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String doctorUsername;

    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String patientUsername;

    @Size(min = 1, max = 255)
    private String note_text;

    private Date note_date;

    private Date date;

    // optional
    private List<String> edit;

    //optional
    private List<String> view;

}
