package br.com.case

import br.com.case.dto.CreateUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("users")
@RequestMapping("/users")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Int): User {
        return userService.get(id)
    }

    @PostMapping
    fun create(@RequestBody user: CreateUserDto): ResponseEntity<User> {
        val created =  userService.save(user)
        return ResponseEntity.status(201).body(created)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Int, @RequestBody user: User): ResponseEntity<Void> {
        userService.update(id, user)
        return ResponseEntity.status(204).build<Void>()
    }
}