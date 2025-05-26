package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AgendarServicoActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var spinnerServico: Spinner
    private lateinit var tvDataSelecionada: TextView
    private lateinit var tvHoraSelecionada: TextView
    private lateinit var btnSelecionarData: Button
    private lateinit var btnSelecionarHora: Button
    private lateinit var btnConfirmar: Button

    private var dataSelecionada: Calendar? = null
    private var horaSelecionada: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_servico)

        btnBack = findViewById(R.id.btnBack)
        spinnerServico = findViewById(R.id.spinnerServico)
        tvDataSelecionada = findViewById(R.id.tvDataSelecionada)
        tvHoraSelecionada = findViewById(R.id.tvHoraSelecionada)
        btnSelecionarData = findViewById(R.id.btnSelecionarData)
        btnSelecionarHora = findViewById(R.id.btnSelecionarHora)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        val servicos = listOf("Cabelo", "Barba", "Cabelo e Barba")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServico.adapter = adapter

        btnBack.setOnClickListener { finish() }

        btnSelecionarData.setOnClickListener {
            showDatePicker()
        }

        btnSelecionarHora.setOnClickListener {
            if (dataSelecionada == null) {
                Toast.makeText(this, "Selecione a data primeiro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val diaSemana = dataSelecionada!!.get(Calendar.DAY_OF_WEEK)
            if (diaSemana == Calendar.MONDAY) {
                Toast.makeText(this, "Cabeleireiro fechado às segundas-feiras.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            carregarHorariosDisponiveis()
        }

        btnConfirmar.setOnClickListener {
            confirmarAgendamento()
        }
    }

    private fun showDatePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())  // só datas futuras e hoje

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection

            // Verifica se caiu em segunda-feira, já avisa e não aceita
            val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)
            if (diaSemana == Calendar.MONDAY) {
                Toast.makeText(this, "Cabeleireiro fechado às segundas-feiras.", Toast.LENGTH_SHORT).show()
                return@addOnPositiveButtonClickListener
            }

            dataSelecionada = calendar
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvDataSelecionada.text = formato.format(calendar.time)

            // Limpa hora selecionada ao trocar data
            horaSelecionada = null
            tvHoraSelecionada.text = ""
        }
    }

    private fun carregarHorariosDisponiveis() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val inicioDia = dataSelecionada!!.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis.toString()

            val fimDia = dataSelecionada!!.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis.toString()

            val agendamentosDia = withContext(Dispatchers.IO) {
                db.agendamentoDao().getAgendamentosBetween(inicioDia, fimDia)
            }

            val horariosDisponiveis = mutableListOf<String>()
            for (hora in 8..19) {
                for (minuto in listOf(0, 30)) {
                    val dataHoraCalendar = Calendar.getInstance().apply {
                        time = dataSelecionada!!.time
                        set(Calendar.HOUR_OF_DAY, hora)
                        set(Calendar.MINUTE, minuto)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    val dataHoraStr = dataHoraCalendar.timeInMillis.toString()

                    val jaAgendado = agendamentosDia.any { it.dataHora.trim() == dataHoraStr }
                    if (!jaAgendado) {
                        val horaFormatada = String.format("%02d:%02d", hora, minuto)
                        horariosDisponiveis.add(horaFormatada)
                    }
                }
            }

            if (horariosDisponiveis.isEmpty()) {
                Toast.makeText(this@AgendarServicoActivity, "Nenhum horário disponível.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            AlertDialog.Builder(this@AgendarServicoActivity)
                .setTitle("Escolha um horário")
                .setItems(horariosDisponiveis.toTypedArray()) { _, which ->
                    val horaEscolhida = horariosDisponiveis[which]
                    tvHoraSelecionada.text = horaEscolhida

                    horaSelecionada = Calendar.getInstance().apply {
                        time = dataSelecionada!!.time
                        set(Calendar.HOUR_OF_DAY, horaEscolhida.substringBefore(":").toInt())
                        set(Calendar.MINUTE, horaEscolhida.substringAfter(":").toInt())
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                }
                .show()
        }
    }

    private fun confirmarAgendamento() {
        val servicoSelecionado = spinnerServico.selectedItem.toString()
        if (dataSelecionada == null) {
            Toast.makeText(this, "Por favor, selecione a data.", Toast.LENGTH_SHORT).show()
            return
        }
        if (horaSelecionada == null) {
            Toast.makeText(this, "Por favor, selecione a hora.", Toast.LENGTH_SHORT).show()
            return
        }

        val calendarioCompleto = Calendar.getInstance().apply {
            set(Calendar.YEAR, dataSelecionada!!.get(Calendar.YEAR))
            set(Calendar.MONTH, dataSelecionada!!.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, dataSelecionada!!.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, horaSelecionada!!.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, horaSelecionada!!.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val novoAgendamento = Agendamento(
            servico = servicoSelecionado,
            dataHora = calendarioCompleto.timeInMillis.toString()
        )

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@AgendarServicoActivity)

            val existente = withContext(Dispatchers.IO) {
                db.agendamentoDao().getAgendamentoByDateTime(novoAgendamento.dataHora)
            }

            if (existente != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AgendarServicoActivity,
                        "Essa data e hora já está ocupada. Escolha outro horário.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }

            withContext(Dispatchers.IO) {
                db.agendamentoDao().insert(novoAgendamento)
            }

            withContext(Dispatchers.Main) {
                val millis = novoAgendamento.dataHora.toLongOrNull()
                val dataFormatada = if (millis != null) {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(millis))
                } else {
                    "data inválida"
                }

                Toast.makeText(
                    this@AgendarServicoActivity,
                    "Agendamento de '${novoAgendamento.servico}' para $dataFormatada confirmado!",
                    Toast.LENGTH_LONG
                ).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 2000)
            }
        }
    }
}
