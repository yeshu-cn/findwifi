package work.yeshu.findwifi.db

class TargetWifiRepo(private val targetWifiDao: TargetWifiDao) {
    val targetWifiList = targetWifiDao.getTargetWifiList()

    suspend fun insert(targetWifi: TargetWifi) {
        targetWifiDao.insert(targetWifi)
    }

    suspend fun delete(targetWifi: TargetWifi) {
        targetWifiDao.delete(targetWifi)
    }
}