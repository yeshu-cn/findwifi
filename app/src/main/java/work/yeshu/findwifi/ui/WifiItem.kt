package work.yeshu.findwifi.ui

data class WifiItem(
    // wifi名称
    val name: String,
    // mac地址
    val mac: String,
    // wifi强度
    val level: Int,
    // 是不是目标wifi
    var target: Boolean = false
)