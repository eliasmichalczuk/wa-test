package br.com.case.Exception

import org.springframework.http.HttpStatus

class UserNotFoundException(message: String): BaseException(message, HttpStatus.NOT_FOUND)