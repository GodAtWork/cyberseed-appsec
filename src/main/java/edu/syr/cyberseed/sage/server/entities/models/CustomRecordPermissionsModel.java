package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Data
public class CustomRecordPermissionsModel {

    @Range(min = 1l, max=999999999)
    private Integer id;
    
    private List<String> viewers;

    private List<String> editors;

}
