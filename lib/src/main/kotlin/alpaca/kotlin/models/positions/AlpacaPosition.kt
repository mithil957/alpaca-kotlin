package alpaca.kotlin.models.positions

import alpaca.kotlin.models.serializers.UUIDSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import java.util.*


import kotlinx.serialization.json.*
import kotlinx.serialization.encoding.*

@Serializable
data class AlpacaPosition(
    @Serializable(with = UUIDSerializer::class)
    @JsonNames("asset_id") val assetId: UUID,
    @JsonNames("symbol") val symbol: String,
    @JsonNames("exchange") val exchange: String,
    @JsonNames("asset_class") val assetClass: String,
    @JsonNames("asset_marginable") val assetMarginable: Boolean,
    @JsonNames("avg_entry_price") val averageEntryPrice: Float,
    @JsonNames("qty") val quantity: Float,
    @Serializable(with = PositionSideSerializer::class)
    @JsonNames("side") val side: PositionSide,
    @JsonNames("market_value") val marketValue: Float,
    @JsonNames("cost_basis") val costBasis: Float,
    @JsonNames("unrealized_pl") val unrealizedPnL: Float,
    @JsonNames("unrealized_plpc") val unrealizedPnLPercent: Float,
    @JsonNames("unrealized_intraday_pl") val unrealizedIntradayPnL: Float,
    @JsonNames("unrealized_intraday_plpc") val unrealizedIntradayPnLPercent: Float,
    @JsonNames("current_price") val currentPrice: Float,
    @JsonNames("lastday_price") val lastDayPrice: Float,
    @JsonNames("change_today") val changeToday: Float
)


enum class PositionSide(val value: String) {
    LONG("long"),
    SHORT("short");

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}

object PositionSideSerializer : KSerializer<PositionSide> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PositionSideSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: PositionSide) = encoder.encodeString(value.value)
    override fun deserialize(decoder: Decoder): PositionSide = PositionSide.fromValue(decoder.decodeString())
}