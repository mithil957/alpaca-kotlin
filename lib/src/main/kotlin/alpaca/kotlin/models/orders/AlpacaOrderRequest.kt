package alpaca.kotlin.models.orders

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlpacaOrderRequest(
    @SerialName("symbol") val symbol: String,
    @SerialName("qty") val quantity: Float? = null,
    @SerialName("notional") val notional: Float? = null,
    @SerialName("side") val orderSide: OrderSide,
    @SerialName("type") val orderType: OrderType,
    @SerialName("time_in_force") val timeInForce: TimeInForce,
    @SerialName("limit_price") val limitPrice: Float? = null,
    @SerialName("stop_price") val stopPrice: Float? = null,
    @SerialName("trail_price") val trailPrice: Float? = null,
    @SerialName("trail_percent") val trailPercent: Float? = null,
    @SerialName("extended_hours") val extendedHours: Boolean? = null,
    @SerialName("client_order_id") val clientOrderID: String? = null,
    @SerialName("order_class") val orderClass: OrderClass? = null,
    @SerialName("take_profit") val takeProfit: Float? = null,
    @SerialName("stop_loss") val stopLoss: Float? = null
) {
    companion object {
        fun marketOrder(symbol: String, quantity: Int, orderSide: OrderSide, timeInForce: TimeInForce) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.MARKET,
                timeInForce = timeInForce
            )

        fun fractionalMarketOrder(symbol: String, quantity: Float, orderSide: OrderSide) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity,
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.MARKET,
                timeInForce = TimeInForce.DAY
            )

        fun notionalMarketOrder(symbol: String, notional: Float, orderSide: OrderSide) =
            AlpacaOrderRequest(
                symbol = symbol,
                notional = notional,
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.MARKET,
                timeInForce = TimeInForce.DAY
            )

        fun limitOrder(symbol: String, quantity: Int, limitPrice: Float,
                       orderSide: OrderSide, timeInForce: TimeInForce, extendedHours: Boolean) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                limitPrice = limitPrice,
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.LIMIT,
                timeInForce = timeInForce,
                extendedHours = extendedHours
            )

        fun stopOrder(symbol: String, quantity: Int, stopPrice: Float,
                      orderSide: OrderSide, timeInForce: TimeInForce, extendedHours: Boolean) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                stopPrice = stopPrice,
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.STOP,
                timeInForce = timeInForce,
                extendedHours = extendedHours
            )

        fun stopLimitOrder(symbol: String, quantity: Int, limitPrice: Float, stopPrice: Float,
                           orderSide: OrderSide, timeInForce: TimeInForce, extendedHours: Boolean) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                limitPrice = limitPrice,
                stopPrice = stopPrice,
                orderClass = OrderClass.SIMPLE,
                orderSide = orderSide,
                orderType = OrderType.STOP_LIMIT,
                timeInForce = timeInForce,
                extendedHours = extendedHours
            )

        fun marketBracketOrder(symbol: String, quantity: Int,
                               orderSide: OrderSide, timeInForce: TimeInForce,
                               takeProfit: Float, stopPrice: Float, stopLoss: Float? = null) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                orderClass = OrderClass.BRACKET,
                orderType = OrderType.MARKET,
                orderSide = orderSide,
                timeInForce = timeInForce,
                takeProfit = takeProfit,
                stopPrice = stopPrice,
                stopLoss = stopLoss
            )

        fun limitBracketOrder(symbol: String, quantity: Int, limitPrice: Float,
                              orderSide: OrderSide, timeInForce: TimeInForce,
                              takeProfit: Float, stopPrice: Float, stopLoss: Float? = null) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                limitPrice = limitPrice,
                orderClass = OrderClass.BRACKET,
                orderType = OrderType.LIMIT,
                orderSide = orderSide,
                timeInForce = timeInForce,
                takeProfit = takeProfit,
                stopPrice = stopPrice,
                stopLoss = stopLoss
            )

        fun trailingStopOrder(symbol: String, quantity: Int, trailPrice: Float,
                              orderSide: OrderSide, timeInForce: TimeInForce, extendedHours: Boolean) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                orderType = OrderType.TRAILING_STOP,
                orderSide = orderSide,
                timeInForce = timeInForce,
                trailPrice = trailPrice,
                extendedHours = extendedHours
            )

        fun trailingStopPercentOrder(symbol: String, quantity: Int, trailPercent: Float,
                                     orderSide: OrderSide, timeInForce: TimeInForce, extendedHours: Boolean) =
            AlpacaOrderRequest(
                symbol = symbol,
                quantity = quantity.toFloat(),
                orderType = OrderType.TRAILING_STOP,
                orderSide = orderSide,
                timeInForce = timeInForce,
                trailPercent = trailPercent,
                extendedHours = extendedHours
            )
    }
}