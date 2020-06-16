package com.tek.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;

import static com.tek.core.TekCoreConstant.TEK_LOCALE_PATH;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TekLocaleControllerTest {

    @Autowired private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @Value("${server.servlet.context-path:}")
    private String servletPath;

    private String path;

    @PostConstruct
    @SuppressWarnings("unused")
    private void init() {
        path = String.format("%s%s", servletPath, TEK_LOCALE_PATH);
    }

    @Test
    public void getDefaultLocale() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(path)
            .servletPath(servletPath)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").exists())
            .andExpect(jsonPath("$.result").value("english"))
            .andDo(print());
    }

    @Test
    public void setITLocale() throws Exception {
        String IT = "it";
        mockMvc.perform(MockMvcRequestBuilders.post(path)
            .param("locale", IT)
            .servletPath(servletPath)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").exists())
            .andExpect(jsonPath("$.result").value("italiano"))
            .andDo(print());
    }

    @Test
    public void setENLocale() throws Exception {
        String EN = "en";
        mockMvc.perform(MockMvcRequestBuilders.post(path)
            .param("locale", EN)
            .servletPath(servletPath)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").exists())
            .andExpect(jsonPath("$.result").value("english"))
            .andDo(print());
    }
}
