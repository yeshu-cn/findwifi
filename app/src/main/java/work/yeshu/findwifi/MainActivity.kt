package work.yeshu.findwifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.activity_target_wifi_list.toolbar
import work.yeshu.findwifi.db.TargetWifi


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION: Int = 1
    private val adapter = WifiAdapter()
    private lateinit var wifiManager: WifiManager
    private lateinit var viewModel: MainViewModel
    private var targetList = emptyList<TargetWifi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.title = "scan wifi"

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(applicationContext,
            LinearLayoutManager.VERTICAL))
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, TargetWifiListActivity::class.java)
            startActivity(intent)
        }

        wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.targetWifiList.observe(this, Observer {
            targetList = it
        })

        adapter.onItemClickListener = object : WifiAdapter.OnItemClickListener {
            override fun onItemClick(item: WifiItem, position: Int) {
                // 点击复制mac地址到剪切板
                val cm = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("mac", item.mac)
                cm.primaryClip = clipData
            }
        }
    }

    private fun scanWifi() {
        wifiManager.startScan()
        recycler_view.postDelayed({
            scanWifi()
        }, 5000L)
    }

    private fun toWifiItem(scanResult: ScanResult): WifiItem {
        return WifiItem(scanResult.SSID, scanResult.BSSID, WifiManager.calculateSignalLevel
            (scanResult.level, 10))
    }

    private fun registerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
            )
        } else {
            scanWifi()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            scanWifi()
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(broadcastReceiver, filter)
        registerPermission()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = ScanResultBroadcastReceiver()

    inner class ScanResultBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val scanResults = wifiManager.scanResults
            Log.d("yeshu", "on Receive data size is ${scanResults.size}")
            adapter.data = scanResults.map { toWifiItem(it) }
            adapter.data.map {
                it.target = isTarget(it)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun isTarget(wifiItem: WifiItem) : Boolean{
        for (item in targetList) {
            if (TextUtils.equals(item.mac, wifiItem.mac)) {
                // 播放音频
                playAudio(wifiItem)
                return true
            }
        }
        return false
    }

    private var mSoundPool: SoundPool? = null
    private var mSoundId: Int = -1

    private fun playAudio(wifiItem: WifiItem) {
        if (!enableVoice) {
            return
        }
        if (null == mSoundPool || mSoundId == -1) {
            initPlay()
        }
        val volume = computeVolume(wifiItem.level)
        mSoundPool?.play(mSoundId, volume, volume, 1, 2, 1f)
    }

    private fun initPlay() {
        val spb = SoundPool.Builder()
        spb.setMaxStreams(10)
        spb.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
        mSoundPool = spb.build()
        mSoundId = mSoundPool!!.load(applicationContext, R.raw.qq, 1)
    }

    private fun computeVolume(level: Int) : Float{
        return level / 10f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val switchItem = menu?.findItem(R.id.app_bar_switch)
        val switch = switchItem?.actionView?.findViewById(R.id.switch_enable_voice) as Switch
        switch.setOnCheckedChangeListener { _, isChecked ->
            enableVoice = isChecked
        }
        return true
    }


    private var enableVoice = true


}
