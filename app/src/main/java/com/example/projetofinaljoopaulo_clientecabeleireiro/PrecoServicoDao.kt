package com.example.projetofinaljoopaulo_clientecabeleireiro

import androidx.room.*

@Dao
interface PrecoServicoDao {
    @Query("SELECT * FROM precos")
    suspend fun getTodos(): List<PrecoServico>

    @Query("SELECT * FROM precos WHERE servico = :servico")
    suspend fun getPreco(servico: String): PrecoServico?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAtualizar(preco: PrecoServico)
}
