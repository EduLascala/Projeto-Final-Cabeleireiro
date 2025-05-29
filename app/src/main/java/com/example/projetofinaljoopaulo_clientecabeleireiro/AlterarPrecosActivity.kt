package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlterarPrecosActivity : AppCompatActivity() {

    private lateinit var etPrecoCabelo: EditText
    private lateinit var etPrecoBarba: EditText
    private lateinit var etPrecoCabeloBarba: EditText
    private lateinit var btnSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_precos)

        etPrecoCabelo = findViewById(R.id.etPrecoCabelo)
        etPrecoBarba = findViewById(R.id.etPrecoBarba)
        etPrecoCabeloBarba = findViewById(R.id.etPrecoCabeloBarba)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnResetPadrao = findViewById(R.id.btnResetPadrao)


        btnResetPadrao.setOnClickListener {
            resetarPrecosPadrao()
        }


        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        carregarPrecos()

        btnSalvar.setOnClickListener {
            salvarPrecos()
        }
    }



    private fun carregarPrecos() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.precoServicoDao()

        lifecycleScope.launch {
            val precos = withContext(Dispatchers.IO) {
                dao.getTodos()
            }

            precos.forEach {
                when (it.servico) {
                    "Cabelo" -> etPrecoCabelo.setText(it.preco.toString())
                    "Barba" -> etPrecoBarba.setText(it.preco.toString())
                    "Cabelo e Barba" -> etPrecoCabeloBarba.setText(it.preco.toString())
                }
            }
        }
    }

    private fun salvarPrecos() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.precoServicoDao()

        val precoCabelo = etPrecoCabelo.text.toString().toDoubleOrNull()
        val precoBarba = etPrecoBarba.text.toString().toDoubleOrNull()
        val precoCabeloBarba = etPrecoCabeloBarba.text.toString().toDoubleOrNull()

        if (precoCabelo == null || precoBarba == null || precoCabeloBarba == null) {
            Toast.makeText(this, "Preencha todos os preços corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                dao.inserirOuAtualizar(PrecoServico("Cabelo", precoCabelo))
                dao.inserirOuAtualizar(PrecoServico("Barba", precoBarba))
                dao.inserirOuAtualizar(PrecoServico("Cabelo e Barba", precoCabeloBarba))
            }

            Toast.makeText(this@AlterarPrecosActivity, "Preços atualizados!", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var btnResetPadrao: Button
    private fun resetarPrecosPadrao() {
        val db = AppDatabase.getDatabase(this)
        val dao = db.precoServicoDao()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                dao.inserirOuAtualizar(PrecoServico("Cabelo", 30.0))
                dao.inserirOuAtualizar(PrecoServico("Barba", 30.0))
                dao.inserirOuAtualizar(PrecoServico("Cabelo e Barba", 45.0))
            }

            etPrecoCabelo.setText("30.0")
            etPrecoBarba.setText("30.0")
            etPrecoCabeloBarba.setText("45.0")

            Toast.makeText(this@AlterarPrecosActivity, "Preços restaurados com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

}
