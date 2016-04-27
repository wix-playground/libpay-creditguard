package com.wix.pay.creditguard.model

object ResponseTransactionTypes {
  /** Card holder is charged. */
  val blocked = "Blocked"
  /** Card holder is charged. */
  val regularDebit = "RegularDebit"
  /** Card holder is charged. */
  val authDebit = "AuthDebit"
  /** Card holder is charged. */
  val forcedDebit = "ForcedDebit"

  /** Card holder is credited. */
  val regularCredit = "RegularCredit"
  /** Card holder is credited. */
  val refund = "Refund"
  /** Card holder is credited. */
  val authCredit = "AuthCredit"
}
