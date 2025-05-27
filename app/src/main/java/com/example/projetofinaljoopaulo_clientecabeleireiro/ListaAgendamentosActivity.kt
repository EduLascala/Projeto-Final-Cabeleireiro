package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

class ListaAgendamentosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var agendamentos: List<Agendamento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_agendamentos)

        // Botão voltar
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewAgendamentos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        carregarAgendamentos()
    }

    private fun carregarAgendamentos() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            agendamentos = withContext(Dispatchers.IO) {
                db.agendamentoDao().listarTodos()
            }

            if (agendamentos.isEmpty()) {
                Toast.makeText(this@ListaAgendamentosActivity, "Nenhum agendamento encontrado.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            recyclerView.adapter = AgendamentoAdapter(agendamentos,
                onEditar = { editarAgendamento(it) },
                onCancelar = { confirmarCancelamento(it) }
            )
        }
    }

    private fun editarAgendamento(agendamento: Agendamento) {
        val servicos = arrayOf("Cabelo", "Barba", "Cabelo e Barba")

        AlertDialog.Builder(this)
            .setTitle("Editar Serviço")
            .setSingleChoiceItems(servicos, servicos.indexOf(agendamento.servico)) { dialog, which ->
                val novoServico = servicos[which]
                val atualizado = agendamento.copy(servico = novoServico)

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        AppDatabase.getDatabase(this@ListaAgendamentosActivity).agendamentoDao().update(atualizado)
                    }
                    Toast.makeText(this@ListaAgendamentosActivity, "Serviço atualizado!", Toast.LENGTH_SHORT).show()
                    carregarAgendamentos()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarCancelamento(agendamento: Agendamento) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar Agendamento")
            .setMessage("Tem certeza que deseja cancelar?")
            .setPositiveButton("Sim") { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        AppDatabase.getDatabase(this@ListaAgendamentosActivity).agendamentoDao().delete(agendamento)
                    }
                    Toast.makeText(this@ListaAgendamentosActivity, "Agendamento cancelado!", Toast.LENGTH_SHORT).show()
                    carregarAgendamentos()
                }
            }
            .setNegativeButton("Não", null)
            .show()
    }

}
