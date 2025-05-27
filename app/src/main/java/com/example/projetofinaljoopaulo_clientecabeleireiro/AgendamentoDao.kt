package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AgendamentoDao {

    @Insert
    suspend fun insert(agendamento: Agendamento)

    @Query("SELECT * FROM agendamento")
    suspend fun listarTodos(): List<Agendamento>

    @Query("SELECT * FROM agendamento WHERE dataHora = :dataHora LIMIT 1")
    suspend fun getAgendamentoByDateTime(dataHora: String): Agendamento?

    @Query("SELECT * FROM agendamento WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp")
    suspend fun getAgendamentosBetween(inicioTimestamp: String, fimTimestamp: String): List<Agendamento>

    @Update
    suspend fun update(agendamento: Agendamento)

    @Delete
    suspend fun delete(agendamento: Agendamento)
}

