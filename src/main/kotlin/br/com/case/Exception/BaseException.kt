package br.com.case.Exception

import org.springframework.http.HttpStatus

open class BaseException(message: String, val status: HttpStatus): Exception(message)