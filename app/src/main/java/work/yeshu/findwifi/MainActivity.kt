package work.yeshu.findwifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.recycler_view


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION: Int = 1
    private val adapter = WifiAdapter()
    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(applicationContext,
            LinearLayoutManager.VERTICAL))
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, TargetWifiListActivity::class.java)
            startActivity(intent)
        }

        wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private fun scanWifi() {
        wifiManager.startScan()
        recycler_view.postDelayed({
            scanWifi()
        }, 2000L)
    }

    private fun toWifiItem(scanResult: ScanResult): WifiItem {
        return WifiItem(scanResult.SSID, scanResult.BSSID, scanResult.level)
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
            adapter.notifyDataSetChanged()
        }
    }


}
