package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projetofinaljoopaulo_clientecabeleireiro.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userDao: UserDao
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        // Supondo que você passe o username via Intent para identificar o usuário que está editando
        val username = intent.getStringExtra("username") ?: ""

        // Carrega os dados do usuário para preencher os campos
        lifecycleScope.launch {
            currentUser = withContext(Dispatchers.IO) {
                userDao.getUserByUsername(username)
            }

            currentUser?.let { user ->
                binding.etUsername.setText(user.username)  // só leitura, mas pode mostrar
                binding.etEmail.setText(user.email)        // só leitura
                binding.etFullName.setText(user.fullName)
            } ?: run {
                Toast.makeText(this@EditProfileActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnSave.setOnClickListener {
            val newFullName = binding.etFullName.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString()
            val confirmNewPassword = binding.etConfirmNewPassword.text.toString()

            if (newFullName.isEmpty()) {
                Toast.makeText(this, "Nome completo não pode ficar vazio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.isNotEmpty() && newPassword != confirmNewPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(username)
                }

                if (user != null) {
                    val updatedUser = user.copy(
                        fullName = newFullName,
                        passwordHash = if (newPassword.isNotEmpty()) PasswordUtil.hashPassword(newPassword) else user.passwordHash
                    )

                    withContext(Dispatchers.IO) {
                        userDao.updateUser(updatedUser)
                    }

                    Toast.makeText(this@EditProfileActivity, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
