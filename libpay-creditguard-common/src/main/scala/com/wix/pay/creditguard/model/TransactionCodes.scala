package com.wix.pay.creditguard.model

object TransactionCodes {
  /** Swiping of magnetic card. */
  val regular = "Regular"

  /** Self service. */
  val selfService = "SelfService"

  /** Fuel self service. */
  val fuelSelfService = "FuelSelfService"

  val contactless = "Contactless"

  val contactlessSelfService = "ContactlessSelfService"

  val contactlessSelfServiceInGasStation = "ContactlessSelfServiceInGasStation"

  /** Transaction through Internet/phone with card number. */
  val phone = "Phone"

  /** Card holder is present, however card is not swiped. */
  val signature = "Signature"
}
