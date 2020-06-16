package com.tek.core;

import com.tek.core.prop.TekCorsProperties;
import com.tek.core.prop.file.TekFileProperties;
import com.tek.core.prop.TekMailProperties;
import com.tek.core.prop.TekSchedulerProperties;
import com.tek.core.prop.i18n.TekLocaleProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.tek.core.TekCoreConstant.TEK_CORE;

/**
 * Tek Core Module properties to be evaluated from application.yaml / application.properties files.
 *
 * @author MarcoPagan
 */
@Configuration
@ConfigurationProperties(prefix = TEK_CORE)
@Getter
@Setter
public class TekCoreProperties {

    private TekCorsProperties cors = new TekCorsProperties();
    private TekLocaleProperties locale = new TekLocaleProperties();
    private TekFileProperties file = new TekFileProperties();
    private TekMailProperties mail = new TekMailProperties();
    private TekSchedulerProperties scheduler = new TekSchedulerProperties();

}


