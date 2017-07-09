package com.orozco.netreport.service

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.orozco.netreport.flux.action.DataCollectionActionCreator
import com.orozco.netreport.flux.model.DataCollectionModel
import com.orozco.netreport.service.job.DataCollectionJob


/**
 * @author A-Ar Andrew Concepcion
 * @createdOn 09/07/2017
 */

class BASSJobCreator(val dataCollectionActionCreator: DataCollectionActionCreator, val dataCollectionModel: DataCollectionModel) : JobCreator {

    override fun create(tag: String): Job? {
        when (tag) {
            DataCollectionJob.TAG -> return DataCollectionJob(dataCollectionActionCreator, dataCollectionModel)
            else -> return null
        }
    }
}