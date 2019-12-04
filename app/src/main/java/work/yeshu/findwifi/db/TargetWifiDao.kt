package work.yeshu.findwifi.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TargetWifiDao {

    @Query("SELECT * from target_wifi ORDER BY mac ASC")
    fun getTargetWifiList(): LiveData<List<TargetWifi>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(targetWifi: TargetWifi)

    @Query("DELETE FROM target_wifi")
    suspend fun deleteAll()
}