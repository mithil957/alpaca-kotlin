package alpaca.kotlin.models.marketData

import java.time.Instant

data class AlpacaBarsRequest(
    val symbol: String,
    val start: Instant,
    val end: Instant,
    val timeframe: TimeFrame,
    val limit: Int? = null,
    val pageToken: String? = null,
    val adjustment: Adjustment? = null
)

enum class TimeFrame(val value: String) {
    Minute("1Min"),
    Minute15("15Min"),
    Hour("1Hour"),
    Day("1Day");
}

enum class Adjustment(val value: String) {
    Raw("raw"),
    Split("split"),
    Dividend("dividend"),
    All("all")
}