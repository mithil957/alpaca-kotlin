package alpaca.kotlin.models.marketData

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class AlpacaLatestQuoteResponse(
    @JsonNames("symbol") val symbol: String,
    @JsonNames("quote") val alpacaQuote: AlpacaQuote
)

@Serializable
data class AlpacaQuote(
    @JsonNames("t") val timestamp: String,
    @JsonNames("x") val exchange: String = "No exchange",
    @JsonNames("ax") val askExchange: String = "No exchange",
    @JsonNames("ap") val askPrice: Float,
    @JsonNames("as") val askSize: Long,
    @JsonNames("bx") val bidExchange: String = "No exchange",
    @JsonNames("bp") val bidPrice: Float,
    @JsonNames("bs") val bidSize: Long,
    @JsonNames("c") val conditions: List<String> = listOf(),
    @JsonNames("z") val tape: String = "No tape")

