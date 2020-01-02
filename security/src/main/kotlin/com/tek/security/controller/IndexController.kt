package com.tek.security.controller

import com.tek.security.SecurityPattern.BASE_PATH
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(BASE_PATH)
class IndexController {

    @GetMapping
    fun index(model: Model) = "forward:/index.html"
}