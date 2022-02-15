package com.machiav3lli.discus.client.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.machiav3lli.discus.client.data.Database
import com.machiav3lli.discus.client.data.Project
import com.machiav3lli.discus.client.projectsDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditSheetViewModel(val db: Database, val id: Long) : ViewModel() {
    val project = MediatorLiveData<Project>()

    init {
        project.addSource(db.projectDao.get(id), project::setValue)
    }

    fun deleteProject() {
        viewModelScope.launch {
            delete()
        }
    }

    private suspend fun delete() {
        withContext(Dispatchers.IO) {
            db.projectDao.deleteById(id)
        }
    }

    fun createFile(context: Context) {
        viewModelScope.launch {
            create(context)
        }
    }

    private suspend fun create(context: Context) {
        withContext(Dispatchers.IO) {
            context.projectsDirectory.mkdirs()
            val projectFile =
                File(context.projectsDirectory, "project_${project.value?.id}.json")
            if (!projectFile.exists()) projectFile.createNewFile()
            projectFile.writeText(project.value?.toJSON() ?: "")
        }
    }

    fun updateProject(newValue: Project?) {
        newValue?.let {
            viewModelScope.launch {
                update(it)
            }
        }
    }

    private suspend fun update(newValue: Project) {
        withContext(Dispatchers.IO) {
            db.projectDao.update(newValue)
        }
    }

    class Factory(val db: Database, val id: Long) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditSheetViewModel::class.java)) {
                return EditSheetViewModel(db, id) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
