package br.com.zup.edu.pix.compartilhado.validacoes

import br.com.zup.edu.pix.cadastrar.CadastrarChaveHttpRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "chave PIX inválida para o tipo informado",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = [],
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, CadastrarChaveHttpRequest> {
    override fun isValid(
        value: CadastrarChaveHttpRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value?.tipoChave == null)
            return false

        return value.tipoChave.validate(value.chave)
    }

}
