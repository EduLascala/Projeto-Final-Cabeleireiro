package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projetofinaljoopaulo_clientecabeleireiro.databinding.ActivityProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDao: UserDao
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        // Recupera o username da sessão (SharedPreferences)
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPref.getString("username", null)

        if (username == null) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Busca os dados do usuário
        lifecycleScope.launch {
            currentUser = withContext(Dispatchers.IO) {
                userDao.getUserByUsername(username)
            }

            currentUser?.let { user ->
                binding.tvUsername.text = user.username
                binding.tvEmail.text = user.email
                binding.tvFullName.text = user.fullName
            } ?: run {
                Toast.makeText(this@ProfileActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Ação do botão "Editar Perfil"
        binding.btnEditProfile.setOnClickListener {
            currentUser?.let { user ->
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.putExtra("username", user.username)
                startActivity(intent)
            }
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
