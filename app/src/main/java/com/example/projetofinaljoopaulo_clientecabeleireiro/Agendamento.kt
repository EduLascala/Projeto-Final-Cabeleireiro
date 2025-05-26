package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agendamento")
data class Agendamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val servico: String,
    val dataHora: String   // Ex: "2025-05-25T15:30"
)


