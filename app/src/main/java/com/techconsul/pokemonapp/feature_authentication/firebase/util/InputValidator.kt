package com.techconsul.pokemonapp.feature_authentication.firebase.util


object InputValidator {
    fun emailValidator(email: String?): String? {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        if (email.isNullOrBlank()) {
            return "Email was left empty"
        }
        if (!EMAIL_REGEX.toRegex().matches(email)) {
            return "This is not a valid Email Format"
        }
        return null
    }

    fun passwordValidator(password: String?): String? {
        if (password.isNullOrBlank())
            return "Password left blank"

        if (password.length < 8) {
            return "Password too Short"
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return "Password needs to contain a letter and a digit"
        }
        return null

    }

}
