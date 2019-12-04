package work.yeshu.findwifi.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target_wifi")
data class TargetWifi(
    @PrimaryKey @ColumnInfo(name = "mac")
    val mac: String
)