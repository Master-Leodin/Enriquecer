package com.btcemais.enriquecer

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.btcemais.enriquecer.databinding.FragmentGastosBinding
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.ParseException
import java.util.Locale

class GastosFragment : Fragment() {

    private var _binding: FragmentGastosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransacaoViewModel by activityViewModels {
        val repository = (requireActivity() as MainActivity).repository
        TransacaoViewModelFactory(repository)
    }

    private lateinit var transacaoAdapter: TransacaoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGastosBinding.inflate(inflater, container, false)
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
        transacaoAdapter = TransacaoAdapter { transacao ->
            showDeleteConfirmationDialog(transacao, "gasto")
        }
        binding.rvGastos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transacaoAdapter
        }
    }

    private fun setupListeners() {
        binding.btnSalvarGasto.setOnClickListener {
            salvarGasto()
        }
    }

    private fun setupInputFilters() {
        binding.etValorGasto.filters = arrayOf(DecimalDigitsInputFilter(10, 2))
    }

    private fun observeViewModel() {
        viewModel.allTransacoes.observe(viewLifecycleOwner) { transacoes ->
            transacaoAdapter.submitFilteredList(transacoes) { it.tipo == TipoTransacao.GASTO }
        }
    }

    private fun salvarGasto() {
        val valorText = binding.etValorGasto.text.toString().trim()
        val descricao = binding.etDescricaoGasto.text.toString().trim()

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
            tipo = TipoTransacao.GASTO
        )

        lifecycleScope.launch {
            viewModel.insert(transacao)
            binding.etValorGasto.setText("")
            binding.etDescricaoGasto.setText("")
            Toast.makeText(context, "Gasto salvo com sucesso!", Toast.LENGTH_SHORT).show()
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

    private fun showDeleteConfirmationDialog(transacao: Transacao, tipo: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir este $tipo: ${transacao.descricao}?")
            .setPositiveButton("Excluir") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.delete(transacao)
                    Toast.makeText(context, "$tipo excluído com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}