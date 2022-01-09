package alpaca.kotlin.models.orders

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

data class AlpacaOrder(
    val id: UUID,
    val clientOrderId: String,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val submittedAt: Instant?,
    val filledAt: Instant?,
    val expiredAt: Instant?,
    val canceledAt: Instant?,
    val failedAt: Instant?,
    val replacedAt: Instant?,
    val replacedBy: UUID?,
    val replaces: UUID?,
    val assetId: UUID,
    val symbol: String,
    val assetClass: String,
    val notional: Float?,
    val quantity: Float,
    val filledQuantity: Float,
    val filledAveragePrice: Float?,
    val orderClass: OrderClass?,
    val orderType: OrderType,
    val orderSide: OrderSide,
    val timeInForce: TimeInForce,
    val limitPrice: Float?,
    val stopPrice: Float?,
    val orderLifecycle: OrderLifecycle,
    val extendedHours: Boolean,
    val legs: List<AlpacaOrder>?,
    val trailPercent: Float?,
    val trailPrice: Float?,
    val highestWaterMark: Float?
)

fun String.toInstant(): Instant = OffsetDateTime.parse(this).toInstant()
fun String.notNull(): String? = if (this == "null") null else this
fun JsonElement.notJsonNull(): JsonElement? = if (this is JsonNull) null else this

// TODO: 1/6/2022 there is probably a better way of doing this
fun JsonElement.toAlpacaOrder(): AlpacaOrder {
    return AlpacaOrder(
        id = this.jsonObject["id"]?.jsonPrimitive?.content.let { UUID.fromString(it) },
        clientOrderId = this.jsonObject["client_order_id"]?.jsonPrimitive?.content.toString(),
        createdAt = this.jsonObject["created_at"]?.jsonPrimitive?.content.toString().toInstant(),
        updatedAt = this.jsonObject["updated_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        submittedAt = this.jsonObject["submitted_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        filledAt = this.jsonObject["filled_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        expiredAt = this.jsonObject["expired_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        canceledAt = this.jsonObject["canceled_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        failedAt = this.jsonObject["failed_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        replacedAt = this.jsonObject["replaced_at"]?.jsonPrimitive?.content?.notNull()?.toInstant(),
        replacedBy = this.jsonObject["replaced_by"]?.jsonPrimitive?.content?.notNull()?.let { UUID.fromString(it) },
        replaces = this.jsonObject["replaces"]?.jsonPrimitive?.content?.notNull()?.let { UUID.fromString(it) },
        assetId = this.jsonObject["asset_id"]?.jsonPrimitive?.content.toString().let { UUID.fromString(it) },
        symbol = this.jsonObject["symbol"]?.jsonPrimitive?.content.toString(),
        assetClass = this.jsonObject["asset_class"]?.jsonPrimitive?.content.toString(),
        notional = this.jsonObject["notional"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        quantity = this.jsonObject["qty"]?.jsonPrimitive?.content.toString().toFloat(),
        filledQuantity = this.jsonObject["filled_qty"]?.jsonPrimitive?.content.toString().toFloat(),
        filledAveragePrice = this.jsonObject["filled_avg_price"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        orderClass = this.jsonObject["order_class"]?.jsonPrimitive?.content?.notNull()?.let { OrderClass.fromValue(it) },
        orderType = this.jsonObject["type"]?.jsonPrimitive?.content.toString().let { OrderType.fromValue(it) },
        orderSide = this.jsonObject["side"]?.jsonPrimitive?.content.toString().let { OrderSide.fromValue(it) },
        timeInForce = this.jsonObject["time_in_force"]?.jsonPrimitive?.content.toString().let { TimeInForce.fromValue(it) },
        limitPrice = this.jsonObject["limit_price"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        stopPrice = this.jsonObject["stop_price"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        orderLifecycle = this.jsonObject["status"]?.jsonPrimitive?.content.toString().let { OrderLifecycle.fromValue(it) },
        extendedHours = this.jsonObject["extended_hours"]?.jsonPrimitive?.content.toString().toBoolean(),
        legs = this.jsonObject["legs"]?.notJsonNull()?.jsonArray?.mapNotNull { it.toAlpacaOrder() },
        trailPercent = this.jsonObject["trail_percent"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        trailPrice = this.jsonObject["trail_price"]?.jsonPrimitive?.content?.notNull()?.toFloat(),
        highestWaterMark = this.jsonObject["hwm"]?.jsonPrimitive?.content?.notNull()?.toFloat()
    )
}

@Serializable
enum class OrderClass(val value: String) {
    @SerialName("simple") SIMPLE("simple"),
    @SerialName("bracket") BRACKET("bracket"),
    @SerialName("oco") OCO("oco"),
    @SerialName("oto") OTO("oto");

    companion object {
        fun fromValue(value: String) = values().firstOrNull { it.value == value }
    }
}

@Serializable
enum class OrderType(val value: String) {
    @SerialName("market") MARKET("market"),
    @SerialName("limit") LIMIT("limit"),
    @SerialName("stop") STOP("stop"),
    @SerialName("stop_limit") STOP_LIMIT("stop_limit"),
    @SerialName("trailing_stop") TRAILING_STOP("trailing_stop");

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}

@Serializable
enum class TimeInForce(val value:String) {
    @SerialName("day") DAY("day"),
    @SerialName("gtc") GOOD_TIL_CANCELED("gtc"),
    @SerialName("opg") OPG("opg"), // MARKET ON OPEN and LIMIT ON OPEN
    @SerialName("cls") CLS("cls"),  // MARKET ON CLOSE and LIMIT ON CLOSE
    @SerialName("ioc") IMMEDIATE_OR_CANCEL("ioc"),
    @SerialName("fok") FILL_OR_KILL("fok");

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}

@Serializable
enum class OrderSide(val value: String) {
    @SerialName("buy") BUY("buy"),
    @SerialName("sell") SELL("sell");

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}

@Serializable
enum class OrderLifecycle(val value: String) {
    @SerialName("new") NEW("new"),
    @SerialName("partially_filled") PARTIALLY_FILLED("partially_filled"),
    @SerialName("filled") FILLED("filled"),
    @SerialName("done_for_day") DONE_FOR_DAY("done_for_day"),
    @SerialName("canceled") CANCELED("canceled"),
    @SerialName("expired") EXPIRED("expired"),
    @SerialName("replaced") REPLACED("replaced"),
    @SerialName("pending_cancel") PENDING_CANCEL("pending_cancel"),
    @SerialName("pending_replace") PENDING_REPLACE("pending_replace"),
    @SerialName("accepted") ACCEPTED("accepted"),
    @SerialName("pending_new") PENDING_NEW("pending_new"),
    @SerialName("accepted_for_bidding") ACCEPTED_FOR_BIDDING("accepted_for_bidding"),
    @SerialName("stopped") STOPPED("stopped"),
    @SerialName("rejected") REJECTED("rejected"),
    @SerialName("suspended") SUSPENDED("suspended"),
    @SerialName("calculated") CALCULATED("calculated");

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}