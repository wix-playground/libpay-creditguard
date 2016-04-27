package com.wix.pay.creditguard.model

object Validations {
  /**
   * J1: Verifies card locally.
   * If the card is ok and the total amount of the deal is under the ceiling, a debit is made without communication to Shva.
   * If it's above the ceiling, an error occurs.
   */
  val noComm = "NoComm"

  /**
   * J2: A local check on the CG Gateway for the validity of the credit card number and if it exist in the blocked cards list.
   * No actual debit occurs.
   */
  val normal = "Normal"

  /**
   * J3: Same as J2 (Normal).
   * It also returns ceiling limit in the total field. for Israeli cards Only
   * A positive response results in actual settlement.
   */
  val creditLimit = "CreditLimit"

  /**
   * J4: Verifies card locally or in credit company; depends on ceiling ZFL terminal parameters
   * A positive response results in actual settlement.
   */
  val autoComm = "AutoComm"

  /**
   * J5: Verifies card by credit company regardless of the ceiling ZFL terminal parameters.
   * No settlement is performed; the amount of verify without settlement is held in card holder's obligor.
   * (This is used for authorization purposes only.)
   */
  val verify = "Verify"

  /**
   * J6: Verifies card by credit company regardless of the ceiling ZFL terminal parameters; settlement is performed.
   */
  val dealer = "Dealer"

  /**
   * J9: Performs a J4 transaction. Yet the transaction will not be deposited.
   * The method of depositing the transactions can be configured per merchant or by releasing the transaction with AutoCommRelease command.
   */
  val autoCommHold = "AutoCommHold"

  /**
   * J102: A local check on the CG Gateway for the validity of the credit card number for tokenization purposes.
   * Perform an actual J2 request and return cardId when terminal is configured to do so.
   */
  val token = "Token"

  /**
   * J109: Used for releasing a transaction (previously performed by using J9).
   * Realeasing a transaction can be done by using the original card number, the cardId (when supported on the terminal)
   * or track2 when the original transaction was performed with track2.
   */
  val autoCommRelease = "AutoCommRelease"

  /**
   * J201: Used for retrieving card number of an existing card id that was generated for the merchant.
   * This option is configuration dependent.
   */
  val cardNo = "cardNo"
}
