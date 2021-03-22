package br.com.zup.edu.pix.compartilhado.validacoes

import javax.validation.Constraint
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Pattern(
    regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[1-5][a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$",
    flags = [Pattern.Flag.CASE_INSENSITIVE],
    message = "UUID inválido"
)
@Constraint(validatedBy = [])
annotation class ValidUUID(
    val message: String = "UUID inválido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = []
)
