package com.orozco.netreport.model

import io.requery.Entity
import io.requery.Persistable

@Entity
data class History(val operator: String,
                   val signal: String,
                   val bandwidth: String = "0",
                   val connectionType: String,
                   val createdDate: Long) : Persistable