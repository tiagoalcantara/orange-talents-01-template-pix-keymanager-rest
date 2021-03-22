package br.com.zup.edu.pix

import io.micronaut.validation.validator.constraints.EmailValidator

enum class TipoChave(val valorNumerico: Int) {
    CPF(1) {
        override fun validate(chave: String?): Boolean {
            if(chave.isNullOrBlank())
                return false

            return chave.matches("^[0-9]{11}\$".toRegex())
        }
    },
    CELULAR(2) {
        override fun validate(chave: String?): Boolean {
            if(chave.isNullOrBlank())
                return false

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(3) {
        override fun validate(chave: String?): Boolean {
            if(chave.isNullOrBlank())
                return false

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    ALEATORIA(4) {
        override fun validate(chave: String?): Boolean {
           return chave.isNullOrBlank()
        }
    };

    abstract fun validate(chave: String?): Boolean
}
