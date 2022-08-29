package br.com.case

import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
    var nome: String,
    var documento: String
) {
    @Id @GeneratedValue
    var id: Int? = null
    var dataAtualizacao: ZonedDateTime? = null
    var dataCriacao: ZonedDateTime = ZonedDateTime.now( ZoneOffset.UTC )
}