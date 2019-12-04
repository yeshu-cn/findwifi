package work.yeshu.findwifi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import work.yeshu.findwifi.db.AppRoomDatabase
import work.yeshu.findwifi.db.TargetWifi
import work.yeshu.findwifi.db.TargetWifiRepo

class TargetWifiViewModel (application: Application): AndroidViewModel(application) {
    private val repo: TargetWifiRepo
    val targetWifiList: LiveData<List<TargetWifi>>

    init {
        val targetWifiDao = AppRoomDatabase.getDatabase(application).targetWifiDao()
        repo = TargetWifiRepo(targetWifiDao)
        targetWifiList = repo.targetWifiList
    }

}