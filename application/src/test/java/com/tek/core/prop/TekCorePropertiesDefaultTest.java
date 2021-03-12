package com.tek.core.prop;

import com.tek.core.TekCoreProperties;
import com.tek.core.prop.file.BinaryProperties;
import com.tek.core.prop.file.TmpProperties;
import com.tek.core.prop.i18n.TekLocaleProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

@SpringBootTest
@TestPropertySource(locations = "classpath:core-default.properties")
@Slf4j
public class TekCorePropertiesDefaultTest {

    @SuppressWarnings("unused")
    @Autowired
    private TekCoreProperties properties;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testApplicationName() {
        Assertions.assertEquals(context.getApplicationName(), "Tek Framework Application");
    }

    @Test
    public void testCorsProperties() {
        TekCorsProperties corsProperties = properties.getCors();
        val allowedMethods = String.join(",", corsProperties.getAllowedMethods());
        val allowedHeaders = String.join(",", corsProperties.getAllowedHeaders());

        Assertions.assertEquals(corsProperties.getAllowedOrigin(), "http://localhost:4200");
        Assertions.assertEquals(corsProperties.getAllowedCredentials(), true);
        Assertions.assertEquals(allowedMethods, "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE");
        Assertions.assertEquals(
            allowedHeaders,
            "x-requested-with,authorization,Content-Type,Authorization,credential,X-XSRF-TOKEN"
        );
    }

    @Test
    public void testLocaleProperties() {
        TekLocaleProperties localeProperties = properties.getLocale();
        Assertions.assertEquals(
            localeProperties.getType(), TekLocaleProperties.TekLocaleType.SESSION
        );
        Assertions.assertEquals(localeProperties.getCookieName(), "locale");
        Assertions.assertEquals(localeProperties.getCookieMaxAge(), -1);
    }

    @Test
    public void testMailProperties() {
        TekMailProperties mailProperties = properties.getMail();
        Assertions.assertEquals(mailProperties.getSendErrors(), false);
        Assertions.assertEquals(mailProperties.getRealDelivery(), false);
    }

    @Test
    public void testSchedulerProperties() {
        TekSchedulerProperties schedulerProperties = properties.getScheduler();
        Assertions.assertEquals(schedulerProperties.getActive(), true);
    }

    @Test
    public void testFileTmpProperties() {
        TmpProperties tmpProperties = properties.getFile().getTmp();
        File tmpDirectory = tmpProperties.getDirectory();
        Integer tmpCleanAfter = tmpProperties.getCleanAfter();

        Assertions.assertEquals(tmpDirectory, new File("tmp"));
        Assertions.assertEquals(tmpProperties.getCron(), "0 0 * * * *");
        Assertions.assertEquals(tmpCleanAfter, 10);
    }

    @Test
    public void testFileBinaryProperties() {
        BinaryProperties binaryProperties = properties.getFile().getBinary();
        File binaryDirectory = binaryProperties.getDirectory();

        Assertions.assertEquals(binaryDirectory, new File("upload"));
        Assertions.assertEquals(binaryProperties.getCron(), "0 0 * * * *");
    }
}
