package br.com.case

import br.com.*
import br.com.case.Exception.UserNotFoundException
import br.com.case.dto.CreateUserDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.util.NestedServletException
import java.nio.charset.Charset
import java.time.ZonedDateTime


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userRepository: UserRepository

    val URL = "http://localhost:8080/users"

    @Test
    fun shouldCreate() {
        val dto = CreateUserDto("User 123", "11554477")
        val result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("User 123"))
            .andReturn()


        val responseBody = objectMapper.readValue(result.response.contentAsString.toByteArray(Charset.defaultCharset()), User::class.java)
        val user = userRepository.getOne(responseBody.id)
        Assertions.assertEquals("User 123", user.nome)
        Assertions.assertEquals("11554477", user.documento)
        Assertions.assertNotNull(user.dataCriacao)
        Assertions.assertNull(user.dataAtualizacao)
    }

    @Test
    fun shouldPut() {
        val userId = userRepository.save(User("Joao", "123456")).id
        val dto = User("Marcos", "213")
        mockMvc.perform(MockMvcRequestBuilders.put("$URL/$userId")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        val updatedUser = userRepository.getOne(userId)
        Assertions.assertEquals("Marcos", updatedUser.nome)
        Assertions.assertEquals("213", updatedUser.documento)
        Assertions.assertNotNull(updatedUser.dataCriacao)
        Assertions.assertTrue(updatedUser.dataAtualizacao!! > updatedUser.dataCriacao)
    }

    @Test
    fun shouldThrowUserNotFoundWhenPut() {
        val userId = 1
        val dto = User("Marcos", "213")
        mockMvc.perform(MockMvcRequestBuilders.put("$URL/$userId")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User of id $userId not found"))
    }

    @Test
    fun shouldGet() {
        val user = User("Joao", "123456")
        user.dataAtualizacao = ZonedDateTime.now()
        val userId = userRepository.save(user).id
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$userId")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Joao"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.documento").value("123456"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.dataAtualizacao").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.dataCriacao").exists())
    }

    @Test
    fun shouldThrowUserNotFoundWhenGet() {
        val userId = 1
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$userId")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User of id $userId not found"))
    }
}