package work.yeshu.findwifi.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.recycler_view
import kotlinx.android.synthetic.main.activity_target_wifi_list.toolbar
import work.yeshu.findwifi.R
import work.yeshu.findwifi.db.TargetWifi


class TargetWifiListActivity : AppCompatActivity() {
    private val adapter = TargetWifiAdapter()
    private lateinit var viewModel: TargetWifiViewModel
    private val newWordActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_wifi_list)
        toolbar.title = getString(R.string.title_activity_target_wifi_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(
            DividerItemDecoration(
                applicationContext, LinearLayoutManager.VERTICAL
            )
        )
        fab.setOnClickListener {
            val intent = Intent(this@TargetWifiListActivity, AddTargetWifiActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        adapter.onItemClickListener = object : TargetWifiAdapter.OnItemClickListener {
            override fun onItemClick(item: TargetWifi, position: Int) {
                showDeleteDialog(item)
            }
        }

        viewModel = ViewModelProvider(this).get(TargetWifiViewModel::class.java)
        viewModel.targetWifiList.observe(this, Observer { list ->
            list?.let {
                adapter.updateData(list)
            }
        })
    }

    private fun showDeleteDialog(item: TargetWifi) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setPositiveButton(
            "delete"
        ) { _, _ -> viewModel.delete(item) }

        dialogBuilder.setNegativeButton("cancel") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.setTitle("confirm delete: ${item.mac}")
        dialogBuilder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddTargetWifiActivity.EXTRA_REPLY)?.let {
                val targetWifi = TargetWifi(it)
                viewModel.insert(targetWifi)
            }
        }
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
