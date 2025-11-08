package com.btcemais.enriquecer

import kotlinx.coroutines.flow.Flow

class TransacaoRepository(private val transacaoDao: TransacaoDao) {

    val allTransacoes: Flow<List<Transacao>> = transacaoDao.getAllTransacoes()
    val totalGanhos: Flow<Double?> = transacaoDao.getTotalGanhos()
    val totalGastos: Flow<Double?> = transacaoDao.getTotalGastos()
    val totalGanhosMesAtual: Flow<Double?> = transacaoDao.getTotalGanhosMesAtual()
    val totalGastosMesAtual: Flow<Double?> = transacaoDao.getTotalGastosMesAtual()

    suspend fun insert(transacao: Transacao) {
        transacaoDao.insert(transacao)
    }

    // Adicionar método para deletar transação
    suspend fun delete(transacao: Transacao) {
        transacaoDao.delete(transacao)
    }
}