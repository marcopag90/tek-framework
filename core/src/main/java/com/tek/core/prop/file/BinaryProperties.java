package com.tek.core.prop.file;

import com.tek.core.prop.TekSchedulerProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class BinaryProperties {

    private File directory = new File("upload");

    /**
     * Upload directory cleans after (days)
     * //TODO see binary scheduler
     */
    private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}
