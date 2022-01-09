package chimeratrader.exceptions

import io.ktor.http.*

class InvalidValueForQueryParameter(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

class Unauthorized(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

class NotFound(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

class TooManyRequests(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)
