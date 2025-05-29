package com.example.projetofinaljoopaulo_clientecabeleireiro

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Agendamento::class, PrecoServico::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun agendamentoDao(): AgendamentoDao
    abstract fun precoServicoDao(): PrecoServicoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Inserir preços padrão ao criar o banco
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).precoServicoDao().apply {
                                    inserirOuAtualizar(PrecoServico("Cabelo", 30.0))
                                    inserirOuAtualizar(PrecoServico("Barba", 30.0))
                                    inserirOuAtualizar(PrecoServico("Cabelo e Barba", 45.0))
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
