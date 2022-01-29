package com.tek.core.config.i18n;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_RESOURCE_BUNDLE_BEAN;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tek.core.config.i18n.TekCookieLocaleConfigurationTest.CookieLocaleController;
import com.tek.core.properties.TekCoreProperties;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@ContextConfiguration(
    classes = {
        TekCookieLocaleConfiguration.class,
        TekResourceBundleConfiguration.class,
        CookieLocaleController.class,
    }
)
@TestPropertySource(properties = {"tek.core.localeConfiguration.type=COOKIE"})
@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TekCookieLocaleConfigurationTest {

  @Autowired private TekCookieLocaleConfiguration configuration;
  @Autowired private MockMvc mockMvc;
  @Autowired private TekCoreProperties properties;

  private static final String LOCALE_PATH = "/locale";

  private String cookieName;

  @PostConstruct
  void setup() {
    cookieName = properties.getLocaleConfiguration().getCookieName();
  }

  @Test
  void test_default_or_unsupported_locale() throws Exception {
    final var response = mockMvc.perform(MockMvcRequestBuilders.post(LOCALE_PATH))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andReturn().getResponse().getContentAsString();
    Assertions.assertEquals("en-US", response);
  }

  @Test
  void test_en_US_locale() throws Exception {
    final var response = mockMvc.perform(MockMvcRequestBuilders.post(LOCALE_PATH)
            .cookie(new Cookie(cookieName, "en-US")))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andReturn().getResponse().getContentAsString();
    Assertions.assertEquals("en-US", response);
  }

  @Test
  void test_it_IT_locale() throws Exception {
    final var response = mockMvc.perform(MockMvcRequestBuilders.post(LOCALE_PATH)
            .cookie(new Cookie(cookieName, "it-IT")))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andReturn().getResponse().getContentAsString();
    Assertions.assertEquals("it-IT", response);
  }

  @RestController
  @RequestMapping(LOCALE_PATH)
  @SuppressWarnings("unused")
  static class CookieLocaleController {

    @Autowired
    @Qualifier(TEK_CORE_RESOURCE_BUNDLE_BEAN)
    private MessageSource messageSource;

    @PostMapping
    String locale(WebRequest request) {
      return messageSource.getMessage(TekI18nMessage.LOCALE, null, LocaleContextHolder.getLocale());
    }
  }
}
