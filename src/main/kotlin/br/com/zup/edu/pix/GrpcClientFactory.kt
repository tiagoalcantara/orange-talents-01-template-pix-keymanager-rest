package br.com.zup.edu.pix

import br.com.zup.edu.PixKeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {
    @Singleton
    fun chavePixStub(@GrpcChannel("pix") channel: ManagedChannel)
    : PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub? {
        return PixKeyManagerServiceGrpc.newBlockingStub(channel)
    }
}