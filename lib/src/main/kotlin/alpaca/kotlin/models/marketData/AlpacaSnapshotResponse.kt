package alpaca.kotlin.models.marketData

import alpaca.kotlin.models.marketData.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class AlpacaSnapshot(
    @JsonNames("latestTrade") val lastTrade: AlpacaTrade,
    @JsonNames("latestQuote") val lastQuote: AlpacaQuote,
    val minuteBar: AlpacaBar,
    val dailyBar: AlpacaBar,
    val prevDailyBar: AlpacaBar
)
