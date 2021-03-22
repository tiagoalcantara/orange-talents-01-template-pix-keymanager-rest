package br.com.zup.edu.pix.cadastrar

import br.com.zup.edu.CadastrarChaveRequest
import br.com.zup.edu.PixKeyManagerServiceGrpc
import br.com.zup.edu.pix.TipoChave
import br.com.zup.edu.pix.TipoConta
import br.com.zup.edu.pix.compartilhado.validacoes.ValidPixKey
import br.com.zup.edu.pix.compartilhado.validacoes.ValidUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Controller("/api/v1/")
@Validated
class CadastrarChaveController(
    @Inject private val gRpcClient: PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub
) {
    @Post("/cliente/{idCliente}/pix")
    fun cadastrar(
        @Valid request: CadastrarChaveHttpRequest,
        @PathVariable @ValidUUID idCliente: String
    ): HttpResponse<*> {
        val response = gRpcClient.cadastrar(request.paraGrpc(idCliente))
        return HttpResponse.created(CadastrarChaveHttpResponse(response.pixId))
    }
}

@Introspected
@ValidPixKey
data class CadastrarChaveHttpRequest(
    @field:NotNull
    val tipoChave: TipoChave?,
    @field:NotNull
    val tipoConta: TipoConta?,
    @field:Size(max = 77)
    val chave: String?,
) {
    fun paraGrpc(idCliente: String): CadastrarChaveRequest? {
        return CadastrarChaveRequest.newBuilder()
            .setChave(chave)
            .setIdCliente(idCliente)
            .setTipoChave(br.com.zup.edu.TipoChave.valueOf(tipoChave!!.name))
            .setTipoConta(br.com.zup.edu.TipoConta.valueOf(tipoConta!!.name))
            .build()
    }
}

data class CadastrarChaveHttpResponse(
    val pixId: String,
)