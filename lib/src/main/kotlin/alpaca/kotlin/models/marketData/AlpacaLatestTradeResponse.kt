package alpaca.kotlin.models.marketData

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class AlpacaLatestTradeResponse(
    @JsonNames("symbol") val symbol: String,
    @JsonNames("trade") val alpacaTrade: AlpacaTrade
    )

@Serializable
data class AlpacaTrade(
    @JsonNames("t") val timestamp: String,
    @JsonNames("x") val exchange: String,
    @JsonNames("p") val price: Float,
    @JsonNames("s") val size: Long,
    @JsonNames("c") val conditions: List<String>,
    @JsonNames("i") val tradeId: Int,
    @JsonNames("z") val tape: String,
    @JsonNames("tks") val takerSide: String
)