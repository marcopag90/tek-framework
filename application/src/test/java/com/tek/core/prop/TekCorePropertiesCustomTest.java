package com.tek.core.prop;

import com.tek.core.TekCoreProperties;
import com.tek.core.prop.file.BinaryProperties;
import com.tek.core.prop.file.TekFileProperties;
import com.tek.core.prop.file.TmpProperties;
import com.tek.core.prop.i18n.TekLocaleProperties;
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
@TestPropertySource(locations = "classpath:core-custom.properties")
@Slf4j
public class TekCorePropertiesCustomTest {

    @SuppressWarnings("unused")
    @Autowired
    private TekCoreProperties properties;

    @SuppressWarnings("unused")
    @Autowired
    private ApplicationContext context;

    @Test
    public void testApplicationName() {
        Assertions.assertEquals(context.getApplicationName(), "Tek Test Application");
    }

    @Test
    public void testCorsProperties() {
        TekCorsProperties corsProperties = properties.getCors();
        String allowedMethods = String.join(",", corsProperties.getAllowedMethods());
        String allowedHeaders = String.join(",", corsProperties.getAllowedHeaders());

        Assertions.assertEquals(corsProperties.getAllowedOrigin(), "http://localhost:4201");
        Assertions.assertEquals(corsProperties.getAllowedCredentials(), false);
        Assertions.assertEquals(allowedMethods, "GET,POST,PATCH,PUT,DELETE");
        Assertions.assertEquals(
            allowedHeaders,
            "x-requested-with,authorization,Content-Type,Authorization,credential"
        );
    }

    @Test
    public void testLocaleProperties() {
        TekLocaleProperties localeProperties = properties.getLocale();

        Assertions.assertEquals(
            localeProperties.getType(), TekLocaleProperties.TekLocaleType.COOKIE
        );
        Assertions.assertEquals(localeProperties.getCookieName(), "custom-cookie");
        Assertions.assertEquals(localeProperties.getCookieMaxAge(), 1000);
    }

    @Test
    public void testMailProperties() {
        TekMailProperties mailProperties = properties.getMail();
        Assertions.assertEquals(mailProperties.getSendErrors(), true);
        Assertions.assertEquals(mailProperties.getRealDelivery(), true);
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

        Assertions.assertEquals(tmpDirectory, new File("tmpDir"));
        Assertions.assertEquals(tmpProperties.getCron(), "*/5 * * * * *");
        Assertions.assertEquals(tmpCleanAfter, 20);
    }

    @Test
    public void testFileBinaryProperties() {
        BinaryProperties binaryProperties = properties.getFile().getBinary();
        File binaryDirectory = binaryProperties.getDirectory();

        Assertions.assertEquals(binaryDirectory, new File("uploadDir"));
        Assertions.assertEquals(binaryProperties.getCron(), "*/10 * * * * *");
    }
}
