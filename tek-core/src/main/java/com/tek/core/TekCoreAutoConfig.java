package com.tek.core;

import com.tek.core.constants.TekCoreConstants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//TODO test if this works
@Configuration
@ComponentScan(TekCoreConstants.TEK_CORE_PACKAGES_TO_SCAN)
public class TekCoreAutoConfig {

}
