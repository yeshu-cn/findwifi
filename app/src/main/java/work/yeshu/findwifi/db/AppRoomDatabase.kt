package work.yeshu.findwifi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(TargetWifi::class), version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun targetWifiDao(): TargetWifiDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppRoomDatabase::class.java, "wifi_database"
                ).addCallback(AppDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }


    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(database.targetWifiDao())
//                }
//            }
        }

        suspend fun populateDatabase(targetWifiDao: TargetWifiDao) {
            targetWifiDao.deleteAll()

            // add sample data
        }
    }
}