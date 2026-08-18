package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ApkDao {

    @Query("SELECT * FROM apk_projects ORDER BY lastUpdatedTime DESC")
    fun getAllProjects(): Flow<List<ApkProjectEntity>>

    @Query("SELECT * FROM apk_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Long): ApkProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ApkProjectEntity): Long

    @Update
    suspend fun updateProject(project: ApkProjectEntity)

    @Query("DELETE FROM apk_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("SELECT * FROM build_mutations WHERE projectId = :projectId ORDER BY appliedTimestamp DESC")
    fun getMutationsForProject(projectId: Long): Flow<List<BuildMutationEntity>>

    @Query("SELECT * FROM build_mutations ORDER BY appliedTimestamp DESC LIMIT 50")
    fun getAllMutations(): Flow<List<BuildMutationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMutation(mutation: BuildMutationEntity): Long

    @Query("SELECT * FROM swarm_logs ORDER BY timestamp DESC LIMIT 100")
    fun getSwarmLogs(): Flow<List<SwarmLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwarmLog(log: SwarmLogEntity): Long

    @Query("DELETE FROM swarm_logs")
    suspend fun clearSwarmLogs()
}
