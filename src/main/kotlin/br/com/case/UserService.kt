package br.com.case

import br.com.case.Exception.UserNotFoundException
import br.com.case.dto.CreateUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun get(id: Int): User {
        val value = userRepository.findById(id)
        if (!value.isPresent) {
            throw UserNotFoundException("User of id $id not found")
        }
        return value.get()
    }

    @Transactional
    fun save(user: CreateUserDto): User {
        return userRepository.save(user.toUser())
    }

    @Transactional
    @Throws(UserNotFoundException::class)
    fun update(id: Int, updatedUser: User) {
        val value = userRepository.findById(id);
        if (!value.isPresent) {
            throw UserNotFoundException("User of id $id not found")
        }
        val user = value.get()
        user.nome = updatedUser.nome
        user.documento = updatedUser.documento
        user.dataAtualizacao = ZonedDateTime.now(ZoneOffset.UTC)
    }

}