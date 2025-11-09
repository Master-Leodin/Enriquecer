package com.btcemais.enriquecer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.btcemais.enriquecer.databinding.FragmentSobreBinding

class SobreFragment : Fragment() {

    private var _binding: FragmentSobreBinding? = null
    private val binding get() = _binding!!

    // Métodos de doação
    private val paypalPixEmail = "leonardo132@gmail.com"
    private val wiseId = "leonardot1427"
    private val wiseLink = "https://wise.com/pay/me/leonardot1427"
    private val lightningAddress = "mightynepal82@walletofsatoshi.com"
    private val bitcoinAddress = "bc1qjahmm3qtzpn9kc86j8uedejjuvtlp66z8w3wek"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSobreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupAppInfo()
    }

    private fun setupAppInfo() {
        binding.tvAppVersion.text = "Versão 1.0"
        binding.tvAppDescription.text = "Enriquecer - Seu aplicativo de controle financeiro pessoal"
    }

    private fun setupClickListeners() {
        // Botão para copiar email (PayPal e PIX)
        binding.btnCopiarEmail.setOnClickListener {
            copiarParaAreaTransferencia(paypalPixEmail, "Email copiado para PayPal e PIX!")
        }

        // Botão de doação via Wise
        binding.btnDoacaoWise.setOnClickListener {
            abrirLinkExterno(wiseLink)
        }

        // Botão para copiar ID Wise
        binding.btnCopiarWise.setOnClickListener {
            copiarParaAreaTransferencia(wiseId, "ID Wise copiado!")
        }

        // Botão de doação via Lightning
        binding.btnDoacaoLightning.setOnClickListener {
            copiarParaAreaTransferencia(lightningAddress, "Endereço Lightning copiado!")
        }

        // Botão de doação via Bitcoin (On Chain)
        binding.btnDoacaoBitcoin.setOnClickListener {
            copiarParaAreaTransferencia(bitcoinAddress, "Endereço Bitcoin copiado!")
        }

        // Botão de compartilhar app
        binding.btnCompartilhar.setOnClickListener {
            compartilharApp()
        }
    }

    private fun abrirLinkExterno(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Não foi possível abrir o link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copiarParaAreaTransferencia(texto: String, mensagemSucesso: String) {
        try {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Doação", texto)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), mensagemSucesso, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao copiar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun compartilharApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Enriquecer - App de Finanças")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Baixe o Enriquecer - App de controle financeiro na página: https://leonportfolio.netlify.app/projects"
            )
            startActivity(Intent.createChooser(shareIntent, "Compartilhar app"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao compartilhar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}