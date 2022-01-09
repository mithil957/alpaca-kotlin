package chimeratrader.exceptions

import io.ktor.http.*

class PositionNotFound(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

class UnableToClosePosition(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

class ProvideEitherQuantityXorPercentage : Throwable()