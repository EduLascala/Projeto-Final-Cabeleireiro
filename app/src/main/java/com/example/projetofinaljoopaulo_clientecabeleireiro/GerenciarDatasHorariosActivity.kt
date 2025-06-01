package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class GerenciarDatasHorariosActivity : AppCompatActivity() {

    private lateinit var btnSelecionarData: Button
    private lateinit var tvDataSelecionada: TextView
    private lateinit var layoutHorarios: LinearLayout
    private lateinit var cbSelecionarTodos: CheckBox
    private lateinit var btnConfirmarIndisponivel: Button
    private lateinit var btnResetDisponivel: Button
    private lateinit var btnBack :ImageButton

    private var dataSelecionada: Calendar? = null
    private val checkBoxes = mutableListOf<Pair<CheckBox, Long>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciar_datas_horarios)

        btnSelecionarData = findViewById(R.id.btnSelecionarData)
        tvDataSelecionada = findViewById(R.id.tvDataSelecionada)
        layoutHorarios = findViewById(R.id.layoutHorarios)
        cbSelecionarTodos = findViewById(R.id.cbSelecionarTodos)
        btnConfirmarIndisponivel = findViewById(R.id.btnConfirmarIndisponivel)
        btnResetDisponivel = findViewById(R.id.btnResetDisponivel)
        btnBack = findViewById(R.id.btnBack)

        btnSelecionarData.setOnClickListener { abrirDatePicker() }

        cbSelecionarTodos.setOnCheckedChangeListener { _, isChecked ->
            checkBoxes.forEach { it.first.isChecked = isChecked }
        }

        btnConfirmarIndisponivel.setOnClickListener {
            bloquearHorariosSelecionados()
        }
        btnResetDisponivel.setOnClickListener {
            liberarHorariosSelecionados()
        }
        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun abrirDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Escolha uma data")
            .build()

        picker.show(supportFragmentManager, "DATE_PICKER")

        picker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                Toast.makeText(this, "Fechado às segundas.", Toast.LENGTH_SHORT).show()
                return@addOnPositiveButtonClickListener
            }

            dataSelecionada = calendar
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvDataSelecionada.text = "Data: ${formato.format(calendar.time)}"
            carregarHorarios(calendar)
        }
    }

    private fun carregarHorarios(data: Calendar) {
        layoutHorarios.removeAllViews()
        checkBoxes.clear()

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val inicio = data.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val fim = data.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            val bloqueios = withContext(Dispatchers.IO) {
                db.agendamentoDao().getAgendamentosEntreTodos(inicio.toString(), fim.toString())
            }

            val bloqueados = bloqueios.map { it.dataHora }

            for (hora in 8..19) {
                for (minuto in listOf(0, 30)) {
                    val c = Calendar.getInstance().apply {
                        timeInMillis = inicio
                        set(Calendar.HOUR_OF_DAY, hora)
                        set(Calendar.MINUTE, minuto)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    val timestamp = c.timeInMillis
                    val cb = CheckBox(this@GerenciarDatasHorariosActivity).apply {
                        val horarioStr = String.format("%02d:%02d", hora, minuto)
                        text = horarioStr
                        isChecked = bloqueados.contains(timestamp.toString())
                        setTextColor(resources.getColor(android.R.color.white, theme))
                    }
                    layoutHorarios.addView(cb)
                    checkBoxes.add(cb to timestamp)
                }
            }
        }
    }

    private fun bloquearHorariosSelecionados() {
        val db = AppDatabase.getDatabase(this)
        val lista = checkBoxes.filter { it.first.isChecked }.map {
            Agendamento(username = "bloqueado", servico = "BLOQUEIO", dataHora = it.second.toString())
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                lista.forEach {
                    val existe = db.agendamentoDao().getAgendamentoByDateTime(it.dataHora)
                    if (existe == null) {
                        db.agendamentoDao().insert(it)
                    }
                }
            }
            Toast.makeText(this@GerenciarDatasHorariosActivity, "Horários bloqueados!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun liberarHorariosSelecionados() {
        val db = AppDatabase.getDatabase(this)
        val selecionados = checkBoxes.filter { it.first.isChecked }.map { it.second.toString() }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                selecionados.forEach { dataHora ->
                    val agendamento = db.agendamentoDao().getAgendamentoByDateTime(dataHora)
                    if (agendamento != null && agendamento.username == "bloqueado" && agendamento.servico == "BLOQUEIO") {
                        db.agendamentoDao().delete(agendamento)
                    }
                }
            }
            Toast.makeText(this@GerenciarDatasHorariosActivity, "Horários liberados!", Toast.LENGTH_SHORT).show()
        }
    }
}
