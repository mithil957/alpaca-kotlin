package alpaca.kotlin.models.marketData

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class AlpacaBarsResponse(
    val bars: List<AlpacaBar>,
    @JsonNames("symbol") val symbol: String,
    @JsonNames("next_page_token") val nextPageToken: String?)


@Serializable
data class AlpacaBar(
    @JsonNames("t") val timestamp: String,
    @JsonNames("o") val open: Float,
    @JsonNames("h") val high: Float,
    @JsonNames("l") val low: Float,
    @JsonNames("c") val close: Float,
    @JsonNames("v") val volume: Long,
    @JsonNames("n") val numberOfTrades: Long,
    @JsonNames("vw") val volumeWeightedAveragePrice: Float)