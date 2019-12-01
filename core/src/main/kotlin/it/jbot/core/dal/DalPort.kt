package it.jbot.core.dal

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RequestMapping("/crud/{entityName}")
interface DalPort {

    @PostMapping
    fun create(
        @PathVariable("entityName") entityName: String,
        @RequestBody properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse>

    @GetMapping
    fun list(
        @PathVariable("entityName") entityName: String,
        @RequestParam properties: MultiValueMap<String, String>,
        pageable: Pageable
    ): ResponseEntity<JBotResponse>

    @GetMapping("/{id}")
    fun read(
        @PathVariable("entityName") entityName: String,
        @PathVariable("id") id: Long
    ): ResponseEntity<JBotResponse>

    //    @PatchMapping
    fun update(
        @PathVariable("entityName") entityName: String,
        id: Long,
        properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse>

    //    @DeleteMapping
    fun delete(
        @PathVariable("entityName") entityName:
        String, id: Long
    ): ResponseEntity<JBotResponse>
}