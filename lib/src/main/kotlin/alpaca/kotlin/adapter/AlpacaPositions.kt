package alpaca.kotlin.adapter

import alpaca.kotlin.models.AlpacaEndpoints
import alpaca.kotlin.models.AlpacaEnvironment
import alpaca.kotlin.models.orders.AlpacaOrder
import alpaca.kotlin.models.orders.toAlpacaOrder
import alpaca.kotlin.models.positions.*
import chimeratrader.exceptions.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AlpacaPositions(private val client: HttpClient, environment: AlpacaEnvironment) {
    private val endpoint: AlpacaEndpoints = if (environment == AlpacaEnvironment.PAPER) AlpacaEndpoints.PAPER else AlpacaEndpoints.LIVE

    suspend fun getAllPositions(): List<AlpacaPosition> {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/positions") {
            method = HttpMethod.Get
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.decodeFromString(it) }
            else -> throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getOpenPosition(symbol: String): AlpacaPosition {
        val httpResponse = client.request<HttpResponse>("${endpoint.value}/positions/$symbol") {
            method = HttpMethod.Get
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.decodeFromString(it) }
            HttpStatusCode.NotFound -> throw PositionNotFound(httpResponse.status, httpResponse.receive())
            else -> throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun closeAllPositions() {
        TODO()
    }

    suspend fun closePosition(symbol: String, quantity: Float? = null, percentage: Float? = null): AlpacaOrder {
        if (!((quantity == null) xor (percentage == null)))
            throw ProvideEitherQuantityXorPercentage()

        val httpResponse = client.request<HttpResponse>("${endpoint.value}/positions/$symbol") {
            method = HttpMethod.Delete
            quantity?.let { parameter("qty", quantity) }
            percentage?.let { parameter("percentage", percentage) }
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.parseToJsonElement(it).toAlpacaOrder() }
            HttpStatusCode.NotFound -> throw PositionNotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden -> throw UnableToClosePosition(httpResponse.status, httpResponse.receive())
            else -> throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }
}