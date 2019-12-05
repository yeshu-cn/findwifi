package work.yeshu.findwifi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_target_wifi.btn_save
import kotlinx.android.synthetic.main.activity_add_target_wifi.et_mac
import kotlinx.android.synthetic.main.activity_target_wifi_list.toolbar

class AddTargetWifiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_target_wifi)
        setSupportActionBar(toolbar)
        toolbar.title = "add target wifi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_save.setOnClickListener{
            val replyIntent = Intent()
            if (TextUtils.isEmpty(et_mac.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val mac = et_mac.text.toString().trim()
                replyIntent.putExtra(EXTRA_REPLY, mac)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "add_target_wifi_reply"
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
