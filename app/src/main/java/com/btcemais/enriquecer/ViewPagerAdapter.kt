package com.btcemais.enriquecer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GanhosFragment()
            1 -> GastosFragment()
            2 -> ResumoFragment()
            else -> throw IllegalStateException("Posição inválida no ViewPager")
        }
    }
}
