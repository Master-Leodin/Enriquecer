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
            showDeleteConfirmationDialog(transacao, getString(R.string.expenses))
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
            Toast.makeText(context, R.string.error_fill_value_description, Toast.LENGTH_SHORT).show()
            return
        }

        val valor = parseCurrencyValue(valorText)
        if (valor == null || valor <= 0) {
            Toast.makeText(context, R.string.error_invalid_value, Toast.LENGTH_SHORT).show()
            return
        }

        if (descricao.length < 2) {
            Toast.makeText(context, R.string.error_description_length, Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, R.string.success_expenses_saved, Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseCurrencyValue(value: String): Double? {
        return try {
            // Remove qualquer símbolo de moeda e espaços, e substitui vírgula por ponto se necessário
            var cleanValue = value.replace("[R$]", "").replace("\\s".toRegex(), "").trim()

            // Para locale pt-BR, substitui vírgula por ponto
            if (Locale.getDefault() == Locale("pt", "BR")) {
                cleanValue = cleanValue.replace(',', '.')
            }

            val format = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
            format.isParseBigDecimal = true

            val parsedValue = format.parse(cleanValue)?.toDouble()
            parsedValue
        } catch (e: ParseException) {
            null
        }
    }

    private fun showDeleteConfirmationDialog(transacao: Transacao, tipo: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_delete_title)
            .setMessage(getString(R.string.confirm_delete_message, tipo, transacao.descricao))
            .setPositiveButton(R.string.delete) { dialog, which ->
                lifecycleScope.launch {
                    viewModel.delete(transacao)
                    Toast.makeText(context, getString(R.string.success_expenses_deleted), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}