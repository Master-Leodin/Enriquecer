package com.btcemais.enriquecer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TransacaoViewModel(private val repository: TransacaoRepository) : ViewModel() {

    val allTransacoes = repository.allTransacoes.asLiveData()
    val totalGanhos = repository.totalGanhos.asLiveData()
    val totalGastos = repository.totalGastos.asLiveData()
    val totalGanhosMesAtual = repository.totalGanhosMesAtual.asLiveData()
    val totalGastosMesAtual = repository.totalGastosMesAtual.asLiveData()

    fun insert(transacao: Transacao) = viewModelScope.launch {
        repository.insert(transacao)
    }
}

class TransacaoViewModelFactory(private val repository: TransacaoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransacaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransacaoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}