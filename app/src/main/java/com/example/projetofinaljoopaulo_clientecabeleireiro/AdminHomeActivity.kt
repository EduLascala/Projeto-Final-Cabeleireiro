package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AdminHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val btnVerAgendamentos = findViewById<Button>(R.id.btnVerAgendamentos)
        val btnAlterarPrecos = findViewById<Button>(R.id.btnAlterarPrecos)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnVerAgendamentos.setOnClickListener {
            startActivity(Intent(this, VerTodosAgendamentosActivity::class.java))
        }

        btnAlterarPrecos.setOnClickListener {
            startActivity(Intent(this, AlterarPrecosActivity::class.java))
        }
    }
}
