package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "precos")
data class PrecoServico(
    @PrimaryKey val servico: String,
    val preco: Double
)
