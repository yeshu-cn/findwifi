package work.yeshu.findwifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION: Int = 1
    val adapter = WifiAdapter();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter

        loadData()
    }

    private fun loadData() {
        registerPermission()
    }

    private fun getWifiList(): List<ScanResult>? {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val scanWifiList = wifiManager.scanResults
        val wifiList: MutableList<ScanResult> = ArrayList()
        if (scanWifiList != null && scanWifiList.size > 0) {
            val signalStrength = HashMap<String, Int?>()
            for (i in scanWifiList.indices) {
                val scanResult = scanWifiList[i]
                if (scanResult.SSID.isNotEmpty()) {
                    val key = scanResult.SSID + " " + scanResult.capabilities
                    if (!signalStrength.containsKey(key)) {
                        signalStrength[key] = i
                        wifiList.add(scanResult)
                    }
                }
            }
        }
        return wifiList
    }

    private fun toWifiItem(scanResult: ScanResult): WifiItem {
        return WifiItem(scanResult.SSID, scanResult.BSSID, scanResult.level)
    }

    private fun registerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION)
        } else {
            getWifiList()?.let { it ->
                adapter.data = it.map { toWifiItem(it) }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            getWifiList()?.let { it ->
                adapter.data = it.map { toWifiItem(it) }
                adapter.notifyDataSetChanged()
            }
        }
    }

}
