package work.yeshu.findwifi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import work.yeshu.findwifi.db.AppRoomDatabase
import work.yeshu.findwifi.db.TargetWifi
import work.yeshu.findwifi.db.TargetWifiRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: TargetWifiRepo
    val targetWifiList: LiveData<List<TargetWifi>>

    init {
        val targetWifiDao = AppRoomDatabase.getDatabase(application, viewModelScope).targetWifiDao()
        repo = TargetWifiRepo(targetWifiDao)
        targetWifiList = repo.targetWifiList
    }
}