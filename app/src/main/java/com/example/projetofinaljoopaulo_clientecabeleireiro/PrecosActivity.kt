package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrecosActivity : AppCompatActivity() {

    private lateinit var tvPrecoCabelo: TextView
    private lateinit var tvPrecoBarba: TextView
    private lateinit var tvPrecoCabeloBarba: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precos)

        tvPrecoCabelo = findViewById(R.id.tvPrecoCabelo)
        tvPrecoBarba = findViewById(R.id.tvPrecoBarba)
        tvPrecoCabeloBarba = findViewById(R.id.tvPrecoCabeloBarba)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        carregarPrecos()
    }

    private fun carregarPrecos() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.precoServicoDao()

        lifecycleScope.launch {
            try {
                val precoCabelo = withContext(Dispatchers.IO) { dao.getPreco("Cabelo") }
                val precoBarba = withContext(Dispatchers.IO) { dao.getPreco("Barba") }
                val precoCabeloBarba = withContext(Dispatchers.IO) { dao.getPreco("Cabelo e Barba") }

                tvPrecoCabelo.text = "R$ ${precoCabelo?.preco ?: "N/D"}"
                tvPrecoBarba.text = "R$ ${precoBarba?.preco ?: "N/D"}"
                tvPrecoCabeloBarba.text = "R$ ${precoCabeloBarba?.preco ?: "N/D"}"
            } catch (e: Exception) {
                Toast.makeText(this@PrecosActivity, "Erro ao carregar preços.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
