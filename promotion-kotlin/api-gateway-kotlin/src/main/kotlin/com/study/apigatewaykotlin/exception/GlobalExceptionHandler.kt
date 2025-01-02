package com.study.apigatewaykotlin.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.ws.rs.core.MediaType
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Slf4j
@Order(-1)
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper,
) : ErrorWebExceptionHandler{

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response

        if (response.isCommitted) {
            return Mono.error(ex)
        }

        response.headers.set("Content-Type", "application/json")

        if (ex is ResponseStatusException) {
            response.statusCode = ex.statusCode
        }

        return response.writeWith(
            Mono.fromSupplier {
                val bufferFactory: DataBufferFactory = response.bufferFactory()
                try {
                    val globalErrorResponse = GlobalErrorResponse.defaultBuild(ex.message ?: "Unexpected error")
                    val errorResponse = objectMapper.writeValueAsBytes(globalErrorResponse)
                    bufferFactory.wrap(errorResponse)
                } catch (e: Exception) {
                    bufferFactory.wrap(byteArrayOf())
                }
            }
        )
    }

    data class GlobalErrorResponse(
        val errorMessage: String,
        val localDateTime: LocalDateTime,
        val additionalInfos: MutableMap<String, Any> = mutableMapOf()
    ) {
        companion object {
            fun defaultBuild(errorMessage: String): GlobalErrorResponse {
                return GlobalErrorResponse(
                    errorMessage = errorMessage,
                    localDateTime = LocalDateTime.now()
                )
            }
        }
    }
}
