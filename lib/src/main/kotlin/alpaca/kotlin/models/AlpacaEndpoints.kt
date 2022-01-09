package alpaca.kotlin.models

enum class AlpacaEndpoints(val value: String) {

    PAPER("https://paper-api.alpaca.markets/v2"),
    LIVE("https://api.alpaca.markets/v2"),
    DATA("https://data.alpaca.markets/v2")
}