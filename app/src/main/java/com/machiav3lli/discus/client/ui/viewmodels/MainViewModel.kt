package com.machiav3lli.discus.client.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.machiav3lli.discus.client.data.Database
import com.machiav3lli.discus.client.data.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val db: Database) : ViewModel() {
    val projects = MediatorLiveData<List<Project>>()

    init {
        projects.addSource(db.projectDao.all, projects::setValue)
    }

    fun addProject() {
        viewModelScope.launch {
            addNewProject()
        }
    }

    private suspend fun addNewProject() {
        withContext(Dispatchers.IO) {
            db.projectDao.insert(Project().apply { id = 0 })
        }
    }

    class Factory(val db: Database) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
