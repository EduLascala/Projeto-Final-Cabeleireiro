package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AgendamentoDao {

    @Insert
    suspend fun insert(agendamento: Agendamento)

    @Query("SELECT * FROM agendamento")
    suspend fun listarTodos(): List<Agendamento>

    // Busca um agendamento pela data/hora exata (timestamp)
    @Query("SELECT * FROM agendamento WHERE dataHora = :dataHora LIMIT 1")
    suspend fun getAgendamentoByDateTime(dataHora: String): Agendamento?

    // Busca agendamentos num intervalo de timestamps (ex: todos do dia)
    @Query("SELECT * FROM agendamento WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp")
    suspend fun getAgendamentosBetween(inicioTimestamp: String, fimTimestamp: String): List<Agendamento>
}
