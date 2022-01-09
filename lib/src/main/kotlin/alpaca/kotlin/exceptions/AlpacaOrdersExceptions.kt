package chimeratrader.exceptions

import io.ktor.http.*


/**
 * Can be thrown when HTTP response is 404 Not Found
 * Order is not found
 */
class OrderNotFound(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

/**
 * Can be thrown when HTTP response is 422 Unprocessable
 * The order status is not cancelable
 */
class OrderNotCancelable(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

/**
 * Can be thrown when HTTP response is 500
 * Failed to cancel order
 */
class FailedToCancelOrder(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

/**
 * Can be thrown when HTTP response is 403 Forbidden
 * Buying power or shares is no sufficient
 */
class InsufficientBuyingPowerOrShares(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)

/**
 * Can be thrown when HTTP response is 422 Unprocessable
 * Input parameters are not recognized
 */
class InputParametersNotRecognized(httpCode: HttpStatusCode, errorMessage: String): AlpacaAPIException(httpCode, errorMessage)
