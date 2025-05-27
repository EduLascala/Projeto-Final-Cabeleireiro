package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AgendamentoAdapter(
    private val agendamentos: List<Agendamento>,
    private val onEditar: (Agendamento) -> Unit,
    private val onCancelar: (Agendamento) -> Unit
) : RecyclerView.Adapter<AgendamentoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricao)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnCancelar: Button = view.findViewById(R.id.btnCancelar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agendamento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agendamento = agendamentos[position]
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val texto = "${agendamento.servico} - ${formato.format(Date(agendamento.dataHora.toLong()))}"
        holder.tvDescricao.text = texto

        holder.btnEditar.setOnClickListener { onEditar(agendamento) }
        holder.btnCancelar.setOnClickListener { onCancelar(agendamento) }
    }

    override fun getItemCount() = agendamentos.size
}
