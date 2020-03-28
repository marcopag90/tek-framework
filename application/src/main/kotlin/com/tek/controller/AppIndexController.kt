package com.tek.controller

import com.tek.security.common.INDEX_PATH
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(INDEX_PATH)
class AppIndexController {

    @GetMapping
    fun index(model: Model) = "forward:/index.html"
}