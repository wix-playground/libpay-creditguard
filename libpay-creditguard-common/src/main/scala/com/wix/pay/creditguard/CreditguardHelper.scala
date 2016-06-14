package com.wix.pay.creditguard

import com.wix.pay.creditcard.CreditCard
import com.wix.pay.creditguard.model._
import com.wix.pay.model.CurrencyAmount

object CreditguardHelper {
  def createAuthorizeRequest(terminalNumber: String,
                             supplierNumber: String,
                             idPrefix: String,
                             orderId: Option[String] = None,
                             card: CreditCard,
                             currencyAmount: CurrencyAmount): AshraitRequest = {
    createAuthorizeOrSaleRequest(
      validation = Validations.verify,
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      idPrefix = idPrefix,
      orderId = orderId,
      card = card,
      currencyAmount = currencyAmount
    )
  }

  def createCaptureRequest(terminalNumber: String,
                           supplierNumber: String,
                           authNumber: String,
                           currency: String,
                           amount: Double,
                           cardId: String,
                           cardExpiration: String,
                           user: String): AshraitRequest = {
    val doDeal = new DoDealRequest
    doDeal.terminalNumber = terminalNumber
    doDeal.cardId = cardId
    doDeal.cardExpiration = cardExpiration
    doDeal.creditType = CreditTypes.regularCredit
    doDeal.currency = currency
    doDeal.transactionCode = TransactionCodes.phone
    doDeal.transactionType = RequestTransactionTypes.debit
    doDeal.total = Conversions.toCreditguardAmount(amount)
    doDeal.validation = Validations.autoComm
    doDeal.authNumber = authNumber
    doDeal.supplierNumber = supplierNumber
    doDeal.user = user

    val request = new Request
    request.command = Commands.doDeal
    request.version = Versions.standard
    request.language = Languages.english
    request.mayBeDuplicate = MayBeDuplicates.`true`
    request.doDeal = doDeal

    val ashrait = new AshraitRequest
    ashrait.request = request
    ashrait
  }

  def createSaleRequest(terminalNumber: String,
                        supplierNumber: String,
                        idPrefix: String,
                        orderId: Option[String] = None,
                        card: CreditCard,
                        currencyAmount: CurrencyAmount): AshraitRequest = {
    createAuthorizeOrSaleRequest(
      validation = Validations.autoComm,
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      idPrefix = idPrefix,
      orderId = orderId,
      card = card,
      currencyAmount = currencyAmount
    )
  }

  def createAuthorizeOrSaleRequest(validation: String,
                                   terminalNumber: String,
                                   supplierNumber: String,
                                   idPrefix: String,
                                   orderId: Option[String] = None,
                                   card: CreditCard,
                                   currencyAmount: CurrencyAmount): AshraitRequest = {
    val doDeal = new DoDealRequest
    doDeal.terminalNumber = terminalNumber
    doDeal.cardNo = card.number
    doDeal.cardExpiration = Conversions.toCreditguardYearMonth(
      year = card.expiration.year,
      month = card.expiration.month
    )
    doDeal.cvv = card.csc.getOrElse(Cvvs.notProvided)
    card.holderId.foreach { doDeal.id = _ }
    doDeal.creditType = CreditTypes.regularCredit
    doDeal.currency = currencyAmount.currency
    doDeal.transactionCode = TransactionCodes.phone
    doDeal.transactionType = RequestTransactionTypes.debit
    doDeal.total = Conversions.toCreditguardAmount(currencyAmount.amount)
    doDeal.validation = validation
    doDeal.user = s"$idPrefix${orderId.getOrElse("")}".take(DoDealRequest.userFieldLength)
    doDeal.supplierNumber = supplierNumber

    val request = new Request
    request.command = Commands.doDeal
    request.version = Versions.standard
    request.language = Languages.english
    request.mayBeDuplicate = MayBeDuplicates.`true`
    request.doDeal = doDeal

    val ashrait = new AshraitRequest
    ashrait.request = request
    ashrait
  }
}
