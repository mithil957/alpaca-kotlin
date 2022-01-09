package alpaca.kotlin.models.orders

import alpaca.kotlin.models.orders.TimeInForce
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AlpacaReplaceOrderRequest(
    @SerialName("qty") val quantity: Float,
    @SerialName("time_in_force") val timeInForce: TimeInForce? = null,
    @SerialName("limit_price") val limitPrice: Float? = null,
    @SerialName("stop_price") val stopPrice: Float? = null,
    @SerialName("trail") val trailPriceOrPercent: Float? = null,
    @SerialName("client_order_id") val clientOrderId: String? = null
)