package com.tek.core.prop.file;


import com.tek.core.prop.TekSchedulerProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class TmpProperties {

    /**
     * Tmp directory path
     */
    private File directory = new File("tmp");

    /**
     * Tmp directory cleans after (days)
     * <p> see {@link com.tek.core.task.TekTmpFileScheduler}
     */
    private Integer cleanAfter = 10;

    private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}