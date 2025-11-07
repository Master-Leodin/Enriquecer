package com.btcemais.enriquecer

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(
    private val maxDigitsBeforeDecimal: Int,
    private val maxDigitsAfterDecimal: Int
) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest)
        builder.replace(dstart, dend, source.subSequence(start, end).toString())
        val newValue = builder.toString()

        // Permite valor vazio
        if (newValue.isEmpty()) return null

        // Padrão para números decimais (permite vírgula como separador decimal)
        val pattern = Pattern.compile("^\\d{0,$maxDigitsBeforeDecimal}([,\\.]\\d{0,$maxDigitsAfterDecimal})?$")

        return if (!pattern.matcher(newValue).matches()) {
            if (source.isEmpty()) dest.subSequence(dstart, dend) else ""
        } else null
    }
}