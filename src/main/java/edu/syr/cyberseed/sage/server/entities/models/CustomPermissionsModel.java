package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Data
public class CustomPermissionsModel {

    @NotNull
    @Size(min = 1, max = 255)
    private String username;
    
    @NotNull
    private List<String> customadds;

    @NotNull
    private List<String> customremoves;

}
