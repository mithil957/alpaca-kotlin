package alpaca.kotlin.adapter

import alpaca.kotlin.models.AlpacaEndpoints
import alpaca.kotlin.models.AlpacaEnvironment
import alpaca.kotlin.models.orders.*
import chimeratrader.exceptions.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.util.*

class AlpacaOrdersAPI(private val client: HttpClient, environment: AlpacaEnvironment) {
    private val endpoint: AlpacaEndpoints = if (environment == AlpacaEnvironment.PAPER) AlpacaEndpoints.PAPER else AlpacaEndpoints.LIVE

    suspend fun getListOfOrders(alpacaGetListOfOrdersRequest: AlpacaGetListOfOrdersRequest): List<AlpacaOrder> {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders") {
            method = HttpMethod.Get
            parameter("status", alpacaGetListOfOrdersRequest.status.value)
            parameter("limit", alpacaGetListOfOrdersRequest.limit)
            parameter("after", alpacaGetListOfOrdersRequest.after.toString())
            parameter("until", alpacaGetListOfOrdersRequest.until.toString())
            parameter("direction", alpacaGetListOfOrdersRequest.direction.value)
            parameter("nested", alpacaGetListOfOrdersRequest.nested)
            parameter("symbols", alpacaGetListOfOrdersRequest.symbols
                .joinToString(",") { it.trim() })
        }

        return when (httpResponse.status) {
             HttpStatusCode.OK -> httpResponse.receive<String>().let {
                Json.parseToJsonElement(it)
                    .jsonArray
                    .map { jsonElement ->  jsonElement.toAlpacaOrder() }
            }
            else -> throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun requestNewOrder(alpacaOrderRequest: AlpacaOrderRequest): AlpacaOrder {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders") {
            method = HttpMethod.Post
            body = Json.encodeToString(alpacaOrderRequest)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it).toAlpacaOrder() }
            HttpStatusCode.Forbidden ->
                throw InsufficientBuyingPowerOrShares(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw InputParametersNotRecognized(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getOrder(orderId: UUID, nested: Boolean = false): AlpacaOrder {
        val httpResponse =  client.request<HttpResponse>("${endpoint.value}/orders/$orderId") {
            method = HttpMethod.Get
            parameter("nested", nested)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it).toAlpacaOrder() }
            HttpStatusCode.NotFound ->
                throw OrderNotFound(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getOrderByClientOrderID(clientOrderId: String): AlpacaOrder {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders") {
            method = HttpMethod.Get
            parameter("client_order_id", clientOrderId)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it).toAlpacaOrder() }
            HttpStatusCode.NotFound ->
                throw OrderNotFound(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun replaceOrder(orderId: UUID, alpacaReplaceOrderRequest: AlpacaReplaceOrderRequest): AlpacaOrder {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders/$orderId") {
            method = HttpMethod.Patch
            body = Json.encodeToString(alpacaReplaceOrderRequest)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it).toAlpacaOrder() }
            HttpStatusCode.NotFound ->
                throw OrderNotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden ->
                throw InsufficientBuyingPowerOrShares(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw InputParametersNotRecognized(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun cancelAllOrders(): List<CancelledOrder> {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders") {
            method = HttpMethod.Delete
        }

        return when (httpResponse.status) {
            HttpStatusCode.MultiStatus -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it) }
                .jsonArray
                .map { it.toCancelledOrder() }
            HttpStatusCode.InternalServerError ->
                throw FailedToCancelOrder(httpResponse.status, httpResponse.receive())
            else -> throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun cancelOrder(orderId: UUID): Boolean {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/orders/$orderId") {
            method = HttpMethod.Delete
        }

        return when (httpResponse.status) {
            HttpStatusCode.NoContent -> true
            HttpStatusCode.NotFound ->
                throw OrderNotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw OrderNotCancelable(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }
}