package alpaca.kotlin.adapter

import alpaca.kotlin.models.AlpacaEndpoints
import alpaca.kotlin.models.marketData.*
import chimeratrader.exceptions.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

class AlpacaMarketData(private val client: HttpClient)  {

    suspend fun getTrades(symbol: String) {
        TODO()
    }

    suspend fun getLatestTrade(symbol: String): AlpacaLatestTradeResponse {
        val httpResponse = client.request<HttpResponse>("${AlpacaEndpoints.DATA.value}/stocks/$symbol/trades/latest") {
            method = HttpMethod.Get
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>().let { Json.decodeFromString(it) }
            HttpStatusCode.BadRequest ->
                throw InvalidValueForQueryParameter(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden ->
                throw Unauthorized(httpResponse.status, httpResponse.receive())
            HttpStatusCode.NotFound ->
                throw NotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.TooManyRequests ->
                throw TooManyRequests(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getQuotes(symbol: String) {
        TODO()
    }

    suspend fun getLatestQuote(symbol: String): AlpacaLatestQuoteResponse {
        val httpResponse = client.request<HttpResponse>("${AlpacaEndpoints.DATA.value}/stocks/$symbol/quotes/latest") {
            method = HttpMethod.Get
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>().let { Json.decodeFromString(it) }
            HttpStatusCode.BadRequest ->
                throw InvalidValueForQueryParameter(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden ->
                throw Unauthorized(httpResponse.status, httpResponse.receive())
            HttpStatusCode.NotFound ->
                throw NotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.TooManyRequests ->
                throw TooManyRequests(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw InputParametersNotRecognized(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getBars(alpacaBarsRequest: AlpacaBarsRequest): AlpacaBarsResponse {
        val httpResponse =  client.request<HttpResponse>("${AlpacaEndpoints.DATA.value}/stocks/${alpacaBarsRequest.symbol}/bars") {
            method = HttpMethod.Get
            parameter("start", alpacaBarsRequest.start.toString())
            parameter("end", alpacaBarsRequest.end.toString())
            parameter("timeframe", alpacaBarsRequest.timeframe.value)
            parameter("limit", alpacaBarsRequest.limit)
            parameter("page_token", alpacaBarsRequest.pageToken)
            parameter("adjustment", alpacaBarsRequest.adjustment?.value)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>().let { Json.decodeFromString(it) }
            HttpStatusCode.BadRequest ->
                throw InvalidValueForQueryParameter(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden ->
                throw Unauthorized(httpResponse.status, httpResponse.receive())
            HttpStatusCode.NotFound ->
                throw NotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.TooManyRequests ->
                throw TooManyRequests(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw InputParametersNotRecognized(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }

    suspend fun getSnapshot(symbols: List<String>): Map<String, AlpacaSnapshot> {
        val httpResponse = client.request<HttpResponse>("${AlpacaEndpoints.DATA.value}/stocks/snapshots") {
            method = HttpMethod.Get
            parameter("symbols", symbols.joinToString(","))
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> httpResponse.receive<String>()
                .let { Json.encodeToJsonElement(it) }
                .jsonObject
                .mapValues { Json.decodeFromJsonElement(it.value) }
            HttpStatusCode.BadRequest ->
                throw InvalidValueForQueryParameter(httpResponse.status, httpResponse.receive())
            HttpStatusCode.Forbidden ->
                throw Unauthorized(httpResponse.status, httpResponse.receive())
            HttpStatusCode.NotFound ->
                throw NotFound(httpResponse.status, httpResponse.receive())
            HttpStatusCode.TooManyRequests ->
                throw TooManyRequests(httpResponse.status, httpResponse.receive())
            HttpStatusCode.UnprocessableEntity ->
                throw InputParametersNotRecognized(httpResponse.status, httpResponse.receive())
            else ->
                throw UnexpectedHTTPCode(httpResponse.status, httpResponse.receive())
        }
    }
}