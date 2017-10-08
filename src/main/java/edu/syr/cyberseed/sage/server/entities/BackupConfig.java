package edu.syr.cyberseed.sage.server.entities;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BackupConfig {

    @NotNull
    @Size(min = 1, max = 15)
    private String offsiteServerIp;

    @NotNull
    @Size(min = 1, max = 255)
    private String offsiteServerUsername;

    @NotNull
    @Size(min = 1, max = 255)
    private String offsiteServerPassword;

    @NotNull
    @Size(min = 1, max = 255)
    private String offsiteServerFilename;

    @Size(min = 1, max = 255)
    private String answer;

}
