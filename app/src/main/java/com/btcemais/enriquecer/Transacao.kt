package com.btcemais.enriquecer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valor: Double,
    val descricao: String,
    val tipo: TipoTransacao, // GANHO ou GASTO
    val data: Long = System.currentTimeMillis() // Timestamp para a data
)

enum class TipoTransacao {
    GANHO,
    GASTO
}
