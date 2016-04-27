package com.wix.pay.creditguard.model

object CreditTypes {
  /** Single payment debit. */
  val regularCredit = "RegularCredit"

  /** "Isracredit", "AMEX credit", "VisaAdif/30+", "Diners Adif/30+" (local Israeli payment method). */
  val israCredit = "IsraCredit"

  /** Ad hock debit- "Hiyuv Miyadi" (local Israeli payment method). */
  val adHock = "AdHock"

  /** Club deal (local Israeli payment method). */
  val clubDeal = "ClubDeal"

  /**
   * Special alpha – "super credit" (local Israeli payment method).
   * Tag numberOfPayments is mandatory
   */
  val specialAlpha = "SpecialAlpha"

  /**
   * Special credit - "credit"/"fixed payments credit" (local Israeli payment method).
   * Tag numberOfPayments is mandatory
   */
  val specialCredit = "SpecialCredit"

  /**
   * Multiple payments debit (installments).
   * Tags numberOfPayments, periodicalPayment and firstPayment are mandatory
   */
  val payments = "Payments"

  /** Payment club (local Israeli payment method). */
  val paymentsClub = "PaymentsClub"
}
