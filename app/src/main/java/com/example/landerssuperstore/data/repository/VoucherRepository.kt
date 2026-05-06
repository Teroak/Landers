package com.example.landerssuperstore.data.repository

import com.example.landerssuperstore.data.model.Voucher

object VoucherRepository {
    private val vouchers = listOf(
        Voucher(1, "LANDERS500", "Fixed", 500.0, 3000.0),
        Voucher(2, "WELCOME10", "Percentage", 10.0, 1000.0),
        Voucher(3, "FREESHIP", "Fixed", 99.0, 2000.0)
    )

    fun getVoucher(code: String): Voucher? {
        return vouchers.find { it.code.equals(code, ignoreCase = true) }
    }
}
