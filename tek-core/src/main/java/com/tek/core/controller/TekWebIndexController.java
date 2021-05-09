package com.tek.core.controller;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.properties.TekCoreProperties;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Default entrypoint to serve static content.
 *
 * @author MarcoPagan
 */
@Controller
@ConditionalOnProperty(prefix = TEK_CORE_PREFIX, name = "web.enabled", havingValue = "true")
@RequestMapping("/")
@RequiredArgsConstructor
public class TekWebIndexController {

  private String forward;

  @PostConstruct
  void setup(TekCoreProperties coreProperties) {
    this.forward = String.format("forward:/%s", coreProperties.getWeb().getIndexPage());
  }

  @GetMapping
  public String index(Model model) {
    return this.forward;
  }

}
