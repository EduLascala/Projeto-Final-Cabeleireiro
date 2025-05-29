package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class VerTodosAgendamentosActivity : AppCompatActivity() {

    private lateinit var layoutAgendamentos: LinearLayout
    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_todos_agendamentos)

        layoutAgendamentos = findViewById(R.id.layoutAgendamentos)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        carregarAgendamentos()
    }

    private fun carregarAgendamentos() {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao() // supondo que você tenha UserDao

        lifecycleScope.launch {
            val agendamentos = withContext(Dispatchers.IO) {
                db.agendamentoDao().listarTodosAgendamentos()
            }

            if (agendamentos.isEmpty()) {
                val tv = TextView(this@VerTodosAgendamentosActivity).apply {
                    text = "Nenhum agendamento encontrado."
                    setTextColor(resources.getColor(android.R.color.white, theme))
                    textSize = 16f
                    setPadding(0, 16, 0, 0)
                }
                layoutAgendamentos.addView(tv)
            } else {
                for (agendamento in agendamentos) {
                    val user = withContext(Dispatchers.IO) {
                        userDao.getUserByUsername(agendamento.username)
                    }

                    val fullName = user?.fullName ?: agendamento.username

                    val tv = TextView(this@VerTodosAgendamentosActivity).apply {
                        val millis = agendamento.dataHora.toLongOrNull()
                        val dataFormatada = if (millis != null) SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(millis)) else "Data inválida"
                        text = "Cliente: $fullName\nServiço: ${agendamento.servico}\nHorário: $dataFormatada"
                        setTextColor(resources.getColor(android.R.color.white, theme))
                        textSize = 16f
                        setPadding(24, 16, 0, 16)
                    }
                    layoutAgendamentos.addView(tv)
                }
            }
        }
    }

}
