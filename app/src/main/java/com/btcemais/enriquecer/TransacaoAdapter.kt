package com.btcemais.enriquecer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btcemais.enriquecer.databinding.ItemTransacaoBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransacaoAdapter(
    private val onDeleteClick: (Transacao) -> Unit
) : ListAdapter<Transacao, TransacaoAdapter.TransacaoViewHolder>(TransacaoDiffCallback()) {

    // Usar formato numérico sem símbolo de moeda específico
    private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val binding = ItemTransacaoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransacaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = getItem(position)
        holder.bind(transacao)
    }

    fun submitFilteredList(list: List<Transacao>, filter: (Transacao) -> Boolean) {
        submitList(list.filter(filter))
    }

    inner class TransacaoViewHolder(private val binding: ItemTransacaoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transacao: Transacao) {
            binding.tvDescricao.text = transacao.descricao
            binding.tvData.text = dateFormat.format(Date(transacao.data))

            // Formatar valor com $ sempre
            val valorFormatado = numberFormat.format(transacao.valor)
            val prefixo = if (transacao.tipo == TipoTransacao.GANHO) "+ $" else "- $"
            binding.tvValor.text = prefixo + valorFormatado

            val context = binding.root.context
            val cor = if (transacao.tipo == TipoTransacao.GANHO) {
                ContextCompat.getColor(context, R.color.green)
            } else {
                ContextCompat.getColor(context, R.color.red)
            }
            binding.tvValor.setTextColor(cor)

            // Delete button
            binding.btnDelete.setOnClickListener {
                onDeleteClick(transacao)
            }
        }
    }

    private class TransacaoDiffCallback : DiffUtil.ItemCallback<Transacao>() {
        override fun areItemsTheSame(oldItem: Transacao, newItem: Transacao): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transacao, newItem: Transacao): Boolean {
            return oldItem == newItem
        }
    }
}