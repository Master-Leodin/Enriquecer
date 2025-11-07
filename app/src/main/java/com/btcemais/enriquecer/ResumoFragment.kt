package com.btcemais.enriquecer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.btcemais.enriquecer.databinding.FragmentResumoBinding
import java.text.NumberFormat
import java.util.Locale

class ResumoFragment : Fragment() {

    private var _binding: FragmentResumoBinding? = null
    private val binding get() = _binding!!

    // Corrigir a forma de obter o ViewModel
    private val viewModel: TransacaoViewModel by activityViewModels {
        val repository = (requireActivity() as MainActivity).repository
        TransacaoViewModelFactory(repository)
    }

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.totalGanhosMesAtual.observe(viewLifecycleOwner) { totalGanhos ->
            val ganhos = totalGanhos ?: 0.0
            binding.tvTotalGanhos.text = currencyFormat.format(ganhos)
            updateSaldo()
        }

        viewModel.totalGastosMesAtual.observe(viewLifecycleOwner) { totalGastos ->
            val gastos = totalGastos ?: 0.0
            binding.tvTotalGastos.text = currencyFormat.format(gastos)
            updateSaldo()
        }
    }

    private fun updateSaldo() {
        val ganhos = viewModel.totalGanhosMesAtual.value ?: 0.0
        val gastos = viewModel.totalGastosMesAtual.value ?: 0.0
        val saldo = ganhos - gastos

        binding.tvSaldoMensal.text = currencyFormat.format(saldo)

        val context = context ?: return

        // Limpar texto anterior
        val currentText = binding.tvSaldoMensal.text.toString()
        val baseText = currentText.substringBefore(" (") ?: currentText
        binding.tvSaldoMensal.text = baseText

        if (saldo > 0) {
            binding.tvSaldoMensal.append(" (Sobrando)")
            binding.tvSaldoMensal.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else if (saldo < 0) {
            binding.tvSaldoMensal.append(" (Devendo)")
            binding.tvSaldoMensal.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            binding.tvSaldoMensal.append(" (Zerado)")
            binding.tvSaldoMensal.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}