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
public class RawRecordModel {

    @Range(min = 1l, max=999999999)
    private Integer id;

    private Date date;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String patientUsername;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    // to encode a 255 byte file we need 4*(255/3) rounded up to multiple of 4 which is 340 char
    @Size(min = 4, max = 340)
    private String base64EncodedBinary;

    // optional
    private List<String> edit;

    //optional
    private List<String> view;
}
