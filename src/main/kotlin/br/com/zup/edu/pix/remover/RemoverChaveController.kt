package br.com.zup.edu.pix.remover

import br.com.zup.edu.PixKeyManagerServiceGrpc
import br.com.zup.edu.RemoverChaveRequest
import br.com.zup.edu.pix.compartilhado.validacoes.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.inject.Inject

@Validated
@Controller("/api/v1/")
class RemoverChaveController(
    @Inject private val gRpcClient: PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub
) {
    @Delete("/cliente/{idCliente}/pix/{idPix}")
    fun remover(
        @PathVariable @ValidUUID idPix: String,
        @PathVariable @ValidUUID idCliente: String,
    ): HttpResponse<Unit>{
        val grpcRequest = RemoverChaveRequest.newBuilder()
            .setIdChave(idPix)
            .setIdCliente(idCliente)
            .build()
        gRpcClient.remover(grpcRequest)
        return HttpResponse.ok()
    }
}