package br.com.case.dto

import br.com.case.User
import com.fasterxml.jackson.annotation.JsonProperty

class CreateUserDto(
    @JsonProperty("nome") val nome: String,
    @JsonProperty("documento") val documento: String
) {
    fun toUser(): User {
        return User(nome, documento)
    }
}