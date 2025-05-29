package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agendamento")
data class Agendamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String, // Associar o agendamento ao usuário
    val servico: String,
    val dataHora: String
)
