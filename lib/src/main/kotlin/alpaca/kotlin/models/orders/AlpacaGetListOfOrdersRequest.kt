package alpaca.kotlin.models.orders

import java.time.Instant


data class AlpacaGetListOfOrdersRequest(
    val status: OrderStatus,
    val limit: Int = 50,
    val after: Instant,
    val until: Instant,
    val direction: Direction = Direction.DESCENDING,
    val nested: Boolean = false,
    val symbols: List<String>
)

enum class OrderStatus(val value: String) {
    OPEN("open"),
    CLOSED("closed"),
    ALL("all")
}

enum class Direction(val value: String) {
    ASCENDING("asc"),
    DESCENDING("desc")
}