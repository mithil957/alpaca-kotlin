package chimeratrader.exceptions

import io.ktor.http.*

open class AlpacaAPIException(httpCode: HttpStatusCode, errorMessage: String): Throwable() {
    init {
        println("HTTP Code: $httpCode, Error Message: $errorMessage")
    }
}

class UnexpectedHTTPCode(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)