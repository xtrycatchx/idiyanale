package com.orozco.netreport.ui.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.orozco.netreport.R
import com.orozco.netreport.core.Database
import com.orozco.netreport.model.History
import com.orozco.netreport.model.Models
import com.orozco.netreport.ui.BaseActivity
import io.requery.android.QueryRecyclerAdapter
import io.requery.kotlin.upper
import io.requery.query.Result
import kotlinx.android.synthetic.main.activity_history.*
import javax.inject.Inject

class HistoryActivity : BaseActivity() {

    @Inject lateinit internal var database: Database

    override val layoutRes: Int = R.layout.activity_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        back.setOnClickListener { finish() }
        historyList.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter()
        historyList.adapter = adapter
        adapter.queryAsync()
    }

    inner class DataViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        @BindView(R.id.netSpeed) lateinit var netSpeed: TextView
        @BindView(R.id.connectionType) lateinit var connectionType: TextView
        @BindView(R.id.signal) lateinit var signal: TextView

        init {
            ButterKnife.bind(this, v)
        }
    }

    inner class HistoryAdapter : QueryRecyclerAdapter<History, DataViewHolder>(Models.DEFAULT, History::class.java) {
        override fun onBindViewHolder(item: History, holder: DataViewHolder, position: Int) {
            holder.netSpeed.text = item.bandwidth
            holder.connectionType.text = item.connectionType
            holder.signal.text = item.signal
        }

        override fun performQuery(): Result<History> {
            return (database.store() select(History::class) orderBy History::createdDate.upper()).get()
        }


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_history, parent, false)
            val viewHolder = DataViewHolder(v)
            return viewHolder
        }

    }
}

