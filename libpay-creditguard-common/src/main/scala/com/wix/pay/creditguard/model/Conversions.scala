package com.wix.pay.creditguard.model

import java.math.{BigDecimal => JBigDecimal}

object Conversions {
  def toCreditguardAmount(amount: Double): Int = {
    JBigDecimal.valueOf(amount).movePointRight(2).intValueExact()
  }

  def toCreditguardYearMonth(year: Int, month: Int): String = {
    f"$month%02d${year % 100}%02d"
  }
}
