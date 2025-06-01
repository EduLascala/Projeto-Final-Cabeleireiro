package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.*

@Dao
interface AgendamentoDao {

    @Insert
    suspend fun insert(agendamento: Agendamento)

    @Query("SELECT * FROM agendamento WHERE username = :username")
    suspend fun listarPorUsuario(username: String): List<Agendamento>

    @Query("SELECT * FROM agendamento WHERE dataHora = :dataHora LIMIT 1")
    suspend fun getAgendamentoByDateTime(dataHora: String): Agendamento?

    @Query("SELECT * FROM agendamento WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp AND username = :username")
    suspend fun getAgendamentosBetween(inicioTimestamp: String, fimTimestamp: String, username: String): List<Agendamento>

    @Update
    suspend fun update(agendamento: Agendamento)

    @Delete
    suspend fun delete(agendamento: Agendamento)

    @Query("SELECT * FROM agendamento ORDER BY dataHora")
    suspend fun listarTodosAgendamentos(): List<Agendamento>

    @Query("SELECT * FROM agendamento WHERE dataHora BETWEEN :inicio AND :fim")
    suspend fun getAgendamentosEntreTodos(inicio: String, fim: String): List<Agendamento>



}
