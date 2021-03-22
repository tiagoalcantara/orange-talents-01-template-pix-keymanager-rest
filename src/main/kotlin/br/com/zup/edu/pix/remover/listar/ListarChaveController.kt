package br.com.zup.edu.pix.remover.listar

import br.com.zup.edu.ListarChaveRequest
import br.com.zup.edu.ListarChaveResponse
import br.com.zup.edu.PixKeyManagerServiceGrpc
import br.com.zup.edu.pix.TipoChave
import br.com.zup.edu.pix.TipoConta
import br.com.zup.edu.pix.compartilhado.validacoes.ValidUUID
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@Validated
@Controller("/api/v1/")
class ListarChaveController(
    @Inject private val gRpcClient: PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub
) {

    @Get("/cliente/{idCliente}/pix")
    fun listar(
        @PathVariable @ValidUUID idCliente: String,
    ): HttpResponse<ListarChaveHttpResponse> {
        val response = gRpcClient.listar(
            ListarChaveRequest.newBuilder()
                .setIdCliente(idCliente)
                .build()
        )

        return HttpResponse.ok(ListarChaveHttpResponse(response.toHttpResponse(), idCliente))
    }
}

@Introspected
data class ListarChaveHttpResponse(
    val chaves: List<Chave>,
    val idCliente: String,
) {
    data class Chave(
        val idPix: String,
        val tipoChave: TipoChave,
        val tipoConta: TipoConta,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        val criadaEm: LocalDateTime,
    )
}

fun ListarChaveResponse.toHttpResponse(): List<ListarChaveHttpResponse.Chave> {
    return this.chavesList.map { chave ->
        ListarChaveHttpResponse.Chave(
            idPix = chave.idPix,
            tipoChave = TipoChave.valueOf(chave.tipoChave.name),
            tipoConta = TipoConta.valueOf(chave.tipoConta.name),
            criadaEm = chave.criadaEm.let {
                Instant
                    .ofEpochSecond(it.seconds, it.nanos.toLong())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
        )
    }
}