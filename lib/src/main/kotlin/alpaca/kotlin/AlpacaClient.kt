package alpaca.kotlin

import alpaca.kotlin.adapter.AlpacaMarketData
import alpaca.kotlin.adapter.AlpacaOrdersAPI
import alpaca.kotlin.adapter.AlpacaPositions
import alpaca.kotlin.models.AlpacaEnvironment
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

class AlpacaClient {
    val client: HttpClient
    val environment: AlpacaEnvironment
    val alpacaMarketData: AlpacaMarketData
    val alpacaOrders: AlpacaOrdersAPI
    val alpacaPositions: AlpacaPositions

    constructor(keyId: String,
                secretKey: String,
                environment: AlpacaEnvironment = AlpacaEnvironment.PAPER) {
        this.client = setupClient(keyId, secretKey)
        this.environment = environment
        this.alpacaMarketData = AlpacaMarketData(client)
        this.alpacaOrders = AlpacaOrdersAPI(client, environment)
        this.alpacaPositions = AlpacaPositions(client, environment)
    }

    private fun setupClient(keyId: String, secretKey: String): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = false
            defaultRequest {
                headers {
                    append("APCA-API-KEY-ID", keyId)
                    append("APCA-API-SECRET-KEY", secretKey)
                }
            }
            install(JsonFeature) {
                serializer = GsonSerializer {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }
}