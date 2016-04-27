package com.wix.pay.creditguard.model

object Cvvs {
  /** Merchant chooses not to pass CVV. */
  val notProvided = "0"

  /** CVV is not readable. */
  val unreadable = "2"

  /** card does not have CVV. */
  val doesNotExist = "9"
}
