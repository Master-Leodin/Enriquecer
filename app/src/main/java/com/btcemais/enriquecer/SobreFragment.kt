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
        binding.tvAppVersion.text = getString(R.string.about_app_version, "1.0")
        binding.tvAppDescription.text = getString(R.string.about_app_description)
    }

    private fun setupClickListeners() {
        binding.btnCopiarEmail.setOnClickListener {
            copyToClipboard(paypalPixEmail, getString(R.string.success_email_copied))
        }

        binding.btnDoacaoWise.setOnClickListener {
            openExternalLink(wiseLink)
        }

        binding.btnCopiarWise.setOnClickListener {
            copyToClipboard(wiseId, getString(R.string.success_wise_id_copied))
        }

        binding.btnDoacaoLightning.setOnClickListener {
            copyToClipboard(lightningAddress, getString(R.string.success_lightning_copied))
        }

        binding.btnDoacaoBitcoin.setOnClickListener {
            copyToClipboard(bitcoinAddress, getString(R.string.success_bitcoin_copied))
        }

        binding.btnCompartilhar.setOnClickListener {
            shareApp()
        }
    }

    private fun openExternalLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.error_open_link, Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToClipboard(text: String, successMessage: String) {
        try {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Donation", text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.error_copy, Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_app_name))
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_app_text)
            )
            startActivity(Intent.createChooser(shareIntent, getString(R.string.about_share_app)))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.error_share, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}