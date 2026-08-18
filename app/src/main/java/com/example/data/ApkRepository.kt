package com.example.data

import com.example.domain.ApkProject
import com.example.domain.BuildMutation
import com.example.domain.SwarmLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApkRepository(private val apkDao: ApkDao) {

    val allProjects: Flow<List<ApkProject>> = apkDao.getAllProjects().map { list ->
        list.map { it.toDomain() }
    }

    val allMutations: Flow<List<BuildMutation>> = apkDao.getAllMutations().map { list ->
        list.map { it.toDomain() }
    }

    val swarmLogs: Flow<List<SwarmLog>> = apkDao.getSwarmLogs().map { list ->
        list.map { it.toDomain() }
    }

    fun getMutationsForProject(projectId: Long): Flow<List<BuildMutation>> {
        return apkDao.getMutationsForProject(projectId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getProjectById(id: Long): ApkProject? {
        return apkDao.getProjectById(id)?.toDomain()
    }

    suspend fun saveProject(project: ApkProject): Long {
        val entity = ApkProjectEntity.fromDomain(project)
        return if (project.id == 0L) {
            apkDao.insertProject(entity)
        } else {
            apkDao.updateProject(entity)
            project.id
        }
    }

    suspend fun deleteProject(id: Long) {
        apkDao.deleteProjectById(id)
    }

    suspend fun addMutation(mutation: BuildMutation): Long {
        return apkDao.insertMutation(BuildMutationEntity.fromDomain(mutation))
    }

    suspend fun addSwarmLog(log: SwarmLog): Long {
        return apkDao.insertSwarmLog(SwarmLogEntity.fromDomain(log))
    }

    suspend fun clearLogs() {
        apkDao.clearSwarmLogs()
    }
}
