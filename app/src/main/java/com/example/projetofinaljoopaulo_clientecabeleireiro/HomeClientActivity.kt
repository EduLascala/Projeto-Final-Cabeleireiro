package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_client)


        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        Log.d("DEBUG", "tvWelcome: $tvWelcome")


        // Recuperar nome completo do SharedPreferences
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val fullName = sharedPref.getString("fullName", null)

        if (!fullName.isNullOrEmpty()) {
            tvWelcome.text = "Olá, $fullName!"
        } else {
            tvWelcome.text = "Olá, Cliente!"
        }

        val btnAgendar = findViewById<Button>(R.id.btnAgendar)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnSair = findViewById<Button>(R.id.btnSair)
        val btnMeusAgendamentos = findViewById<Button>(R.id.btnMeusAgendamentos)
        val btnPrecos = findViewById<Button>(R.id.btnPrecos)

        btnAgendar.setOnClickListener {
            Toast.makeText(this, "Agendamento", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AgendarServicoActivity::class.java)
            startActivity(intent)
        }

        btnPerfil.setOnClickListener {
            val username = sharedPref.getString("username", "") ?: ""

            if (username.isNotEmpty()) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        btnMeusAgendamentos.setOnClickListener {
            val intent = Intent(this, ListaAgendamentosActivity::class.java)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnPrecos.setOnClickListener {
            val intent = Intent(this, PrecosActivity::class.java)
            startActivity(intent)
        }
    }
}
