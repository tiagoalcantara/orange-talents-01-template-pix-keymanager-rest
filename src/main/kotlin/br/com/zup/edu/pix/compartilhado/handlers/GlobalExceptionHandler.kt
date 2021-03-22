package br.com.zup.edu.pix.compartilhado.handlers

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler: ExceptionHandler<StatusRuntimeException, HttpResponse<*>> {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<*> {
        val statusCodigo = exception.status.code
        val statusDescricao = exception.status.description?.split(": ")?.last() ?: ""
        val (statusHttp, mensagem) = when(statusCodigo) {
            Status.Code.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, statusDescricao)
            Status.Code.FAILED_PRECONDITION -> Pair(HttpStatus.BAD_REQUEST, statusDescricao)
            Status.Code.INVALID_ARGUMENT -> Pair(HttpStatus.BAD_REQUEST, statusDescricao)
            Status.Code.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, statusDescricao)
            else -> {
                logger.error("Erro inesperado (${exception.javaClass.name})")
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado")
            }
        }

        return HttpResponse.status<JsonError>(statusHttp).body(JsonError(mensagem))
    }


}