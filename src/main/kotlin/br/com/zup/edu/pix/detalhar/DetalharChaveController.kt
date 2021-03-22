package br.com.zup.edu.pix.detalhar

import br.com.zup.edu.BuscarChaveRequest
import br.com.zup.edu.BuscarChaveResponse
import br.com.zup.edu.PixKeyManagerServiceGrpc
import br.com.zup.edu.pix.TipoChave
import br.com.zup.edu.pix.TipoConta
import br.com.zup.edu.pix.compartilhado.validacoes.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@Validated
@Controller("/api/v1/")
class DetalharChaveController(
    @Inject private val gRpcClient: PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub
) {

    @Get("/cliente/{idCliente}/pix/{idPix}")
    fun detalhar(
        @PathVariable @ValidUUID idCliente: String,
        @PathVariable @ValidUUID idPix: String,
    ): HttpResponse<BuscarChaveHttpResponse> {
        val response: BuscarChaveResponse = gRpcClient.buscar(
            BuscarChaveRequest.newBuilder()
                .setPixId(
                    BuscarChaveRequest.BuscaPorPixId.newBuilder()
                        .setIdChave(idPix)
                        .setIdCliente(idCliente)
                ).build()
        )

        println(response)

        return HttpResponse.ok(response.toHttpResponse())
    }
}

data class BuscarChaveHttpResponse(
    val idCliente: String,
    val idPix: String,
    val chave: ChaveResponse,
)

data class ContaResponse(
    val tipo: TipoConta,
    val instituicao: String,
    val titularNome: String,
    val titularCpf: String,
    val agencia: String,
    val numero: String,
)

data class ChaveResponse(
    val tipo: TipoChave,
    val chave: String,
    val criadaEm: LocalDateTime,
    val conta: ContaResponse,
)

fun BuscarChaveResponse.toHttpResponse(): BuscarChaveHttpResponse {
    return BuscarChaveHttpResponse(
        idCliente = this.idCliente,
        idPix = this.idPix,
        chave = ChaveResponse(
            tipo = TipoChave.valueOf(this.chave.tipo.name),
            chave = this.chave.chave,
            criadaEm = this.chave.criadaEm.let {
                Instant.ofEpochSecond(it.seconds, it.nanos.toLong())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            },
            conta = ContaResponse(
                tipo = TipoConta.valueOf(this.chave.conta.tipo.name),
                instituicao = this.chave.conta.instituicao,
                titularNome = this.chave.conta.titularNome,
                titularCpf = this.chave.conta.titularCpf,
                agencia = this.chave.conta.agencia,
                numero = this.chave.conta.conta,
            )
        )
    )
}
