package br.com.case.configuration

import br.com.case.Exception.BaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.ZonedDateTime


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(BaseException::class)
    fun handleAllExceptions(ex: BaseException, request: WebRequest?): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(ex.message!!, ex::class.toString(), ex.status.value(), ZonedDateTime.now().toString())
        return ResponseEntity<ErrorResponse>(error, ex.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest?): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(ex.message!!, ex::class.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ZonedDateTime.now().toString())
        return ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

class ErrorResponse(
    val message: String,
    val error: String,
    val status: Number,
    val timestamp: String
)