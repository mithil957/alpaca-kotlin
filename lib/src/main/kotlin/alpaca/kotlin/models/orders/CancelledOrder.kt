package alpaca.kotlin.models.orders

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

data class CancelledOrder(val id: UUID?, val status: Int?)

fun JsonElement.toCancelledOrder() = CancelledOrder(
    id = this.jsonObject["id"]?.jsonPrimitive?.content.let { UUID.fromString(it) },
    status = this.jsonObject["status"]?.jsonPrimitive?.content?.toInt(),
)