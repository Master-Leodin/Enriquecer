package com.btcemais.enriquecer

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transacao: Transacao)

    @Query("SELECT * FROM transacoes ORDER BY data DESC")
    fun getAllTransacoes(): Flow<List<Transacao>>

    @Query("SELECT SUM(CASE WHEN tipo = 'GANHO' THEN valor ELSE 0 END) FROM transacoes")
    fun getTotalGanhos(): Flow<Double?>

    @Query("SELECT SUM(CASE WHEN tipo = 'GASTO' THEN valor ELSE 0 END) FROM transacoes")
    fun getTotalGastos(): Flow<Double?>

    // Query corrigida para o resumo mensal usando timestamp em milissegundos
    @Query("""
        SELECT SUM(CASE WHEN tipo = 'GANHO' THEN valor ELSE 0 END) 
        FROM transacoes 
        WHERE strftime('%Y-%m', datetime(data / 1000, 'unixepoch')) = strftime('%Y-%m', 'now')
    """)
    fun getTotalGanhosMesAtual(): Flow<Double?>

    @Query("""
        SELECT SUM(CASE WHEN tipo = 'GASTO' THEN valor ELSE 0 END) 
        FROM transacoes 
        WHERE strftime('%Y-%m', datetime(data / 1000, 'unixepoch')) = strftime('%Y-%m', 'now')
    """)
    fun getTotalGastosMesAtual(): Flow<Double?>

    // Adicionar método para deletar transação
    @Delete
    suspend fun delete(transacao: Transacao)
}