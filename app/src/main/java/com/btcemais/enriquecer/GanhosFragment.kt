package com.btcemais.enriquecer

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.btcemais.enriquecer.databinding.FragmentGanhosBinding
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.ParseException
import java.util.Locale

class GanhosFragment : Fragment() {

    private var _binding: FragmentGanhosBinding? = null
    private val binding get() = _binding!!

    // Corrigir a forma de obter o ViewModel
    private val viewModel: TransacaoViewModel by activityViewModels {
        val repository = (requireActivity() as MainActivity).repository
        TransacaoViewModelFactory(repository)
    }

    private lateinit var transacaoAdapter: TransacaoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGanhosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupInputFilters()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        transacaoAdapter = TransacaoAdapter()
        binding.rvGanhos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transacaoAdapter
        }
    }

    private fun setupListeners() {
        binding.btnSalvarGanho.setOnClickListener {
            salvarGanho()
        }
    }

    private fun setupInputFilters() {
        binding.etValorGanho.filters = arrayOf(DecimalDigitsInputFilter(10, 2))
    }

    private fun observeViewModel() {
        viewModel.allTransacoes.observe(viewLifecycleOwner) { transacoes ->
            transacaoAdapter.submitFilteredList(transacoes) { it.tipo == TipoTransacao.GANHO }
        }
    }

    private fun salvarGanho() {
        val valorText = binding.etValorGanho.text.toString().trim()
        val descricao = binding.etDescricaoGanho.text.toString().trim()

        if (valorText.isBlank() || descricao.isBlank()) {
            Toast.makeText(context, "Preencha o valor e a descrição.", Toast.LENGTH_SHORT).show()
            return
        }

        val valor = parseCurrencyValue(valorText)
        if (valor == null || valor <= 0) {
            Toast.makeText(context, "Valor inválido. Use formato: 1234,56", Toast.LENGTH_SHORT).show()
            return
        }

        if (descricao.length < 2) {
            Toast.makeText(context, "Descrição deve ter pelo menos 2 caracteres.", Toast.LENGTH_SHORT).show()
            return
        }

        val transacao = Transacao(
            valor = valor,
            descricao = descricao,
            tipo = TipoTransacao.GANHO
        )

        lifecycleScope.launch {
            viewModel.insert(transacao)
            binding.etValorGanho.setText("")
            binding.etDescricaoGanho.setText("")
            Toast.makeText(context, "Ganho salvo com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseCurrencyValue(value: String): Double? {
        return try {
            val format = DecimalFormat.getInstance(Locale("pt", "BR")) as DecimalFormat
            format.isParseBigDecimal = true
            format.parse(value)?.toDouble()
        } catch (e: ParseException) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}