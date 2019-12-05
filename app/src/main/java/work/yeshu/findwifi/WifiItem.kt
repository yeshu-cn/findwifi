package work.yeshu.findwifi

data class WifiItem(
    val name: String,
    val mac: String,
    val level: Int,
    var target: Boolean = false
)