package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ListaAgendamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_agendamentos)

        val listaAgendamentos = findViewById<ListView>(R.id.listViewAgendamentos)

        val agendamentosExemplo = listOf(
            "Cabelo - 10/06/2025 às 14:00",
            "Barba - 12/06/2025 às 16:30",
            "Cabelo + Barba - 15/06/2025 às 11:00"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, agendamentosExemplo)
        listaAgendamentos.adapter = adapter
    }
}
