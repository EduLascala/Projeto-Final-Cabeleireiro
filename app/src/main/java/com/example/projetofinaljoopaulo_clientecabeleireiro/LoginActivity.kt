package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageButton

class LoginActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish() // volta para a tela anterior
        }

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(username)
                }

                if (user == null) {
                    Toast.makeText(this@LoginActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val isPasswordCorrect = PasswordUtil.verifyPassword(password, user.passwordHash)
                if (isPasswordCorrect) {
                    // Salva o username na sessão
                    val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("username", user.username)
                        apply()
                    }

                    Toast.makeText(this@LoginActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, HomeClientActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
