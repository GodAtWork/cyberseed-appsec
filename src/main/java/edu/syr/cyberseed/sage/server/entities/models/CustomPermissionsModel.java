package edu.syr.cyberseed.sage.server.entities.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Data
public class CustomPermissionsModel {

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String username;
    

    private List<String> customadds;


    private List<String> customremoves;

}
