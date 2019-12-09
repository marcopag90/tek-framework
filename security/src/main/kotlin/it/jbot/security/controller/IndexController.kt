package it.jbot.security.controller

import it.jbot.security.SecurityPattern.BASE_PATTERN
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(BASE_PATTERN)
class IndexController {

    @GetMapping
    fun index(model: Model) = "forward:/index.html"
}