package com.wix.pay.creditguard


import com.google.api.client.http._
import com.wix.pay.creditcard.CreditCard
import com.wix.pay.creditguard.model.Fields
import com.wix.pay.model.{CurrencyAmount, Customer, Deal}
import com.wix.pay.shva.model.{IsShvaRejectedStatusCode, StatusCodes}
import com.wix.pay.{PaymentErrorException, PaymentException, PaymentGateway, PaymentRejectedException}

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val test = "https://cguat2.creditguard.co.il/xpo/Relay"
  val caspitProduction = "https://payment.caspitgroup.co.il:8443/xpo/Relay"
}

class CreditguardGateway(requestFactory: HttpRequestFactory,
                         connectTimeout: Option[Duration] = None,
                         readTimeout: Option[Duration] = None,
                         numberOfRetries: Int = 0,
                         endpointUrl: String = Endpoints.caspitProduction,
                         merchantParser: CreditguardMerchantParser = new JsonCreditguardMerchantParser,
                         authorizationParser: CreditguardAuthorizationParser = new JsonCreditguardAuthorizationParser) extends PaymentGateway {
  private val helper = new CreditguardHelper

  override def authorize(merchantKey: String, creditCard: CreditCard, currencyAmount: CurrencyAmount, customer: Option[Customer], deal: Option[Deal]): Try[String] = {
    Try {
      val merchant = merchantParser.parse(merchantKey)

      val request = helper.createAuthorizeRequest(
        terminalNumber = merchant.terminalNumber,
        supplierNumber = merchant.supplierNumber,
        idPrefix = merchant.idPrefix,
        orderId = deal.map { _.id },
        card = creditCard,
        currencyAmount = currencyAmount
      )

      val requestXml = RequestParser.stringify(request)
      val responseXml = doRequest(
        user = merchant.user,
        password = merchant.password,
        requestXml = requestXml
      )
      val response = ResponseParser.parse(responseXml)
      verifyShvaStatusCode(
        statusCode = response.response.doDeal.status,
        errorMessage = response.response.doDeal.statusText
      )

      val authorization = CreditguardAuthorization(
        authNumber = response.response.doDeal.authNumber,
        currency = response.response.doDeal.currency,
        tranId = response.response.tranId,
        cardId = response.response.doDeal.cardId,
        cardExpiration = response.response.doDeal.cardExpiration
      )
      authorizationParser.stringify(authorization)
    } match {
      case Success(authorizationKey) => Success(authorizationKey)
      case Failure(e: PaymentException) => Failure(e)
      case Failure(e) => Failure(new PaymentErrorException(e.getMessage, e))
    }
  }

  override def capture(merchantKey: String, authorizationKey: String, amount: Double): Try[String] = {
    Try {
      val merchant = merchantParser.parse(merchantKey)
      val authorization = authorizationParser.parse(authorizationKey)

      val request = helper.createCaptureRequest(
        terminalNumber = merchant.terminalNumber,
        supplierNumber = merchant.supplierNumber,
        cardId = authorization.cardId,
        cardExpiration = authorization.cardExpiration,
        authNumber = authorization.authNumber,
        currency = authorization.currency,
        amount = amount
      )

      val requestXml = RequestParser.stringify(request)
      val responseXml = doRequest(
        user = merchant.user,
        password = merchant.password,
        requestXml = requestXml
      )
      val response = ResponseParser.parse(responseXml)
      verifyShvaStatusCode(
        statusCode = response.response.doDeal.status,
        errorMessage = response.response.doDeal.statusText
      )

      response.response.tranId
    } match {
      case Success(transactionId) => Success(transactionId)
      case Failure(e: PaymentException) => Failure(e)
      case Failure(e) => Failure(new PaymentErrorException(e.getMessage, e))
    }
  }

  override def sale(merchantKey: String, creditCard: CreditCard, currencyAmount: CurrencyAmount, customer: Option[Customer], deal: Option[Deal]): Try[String] = {
    Try {
      val merchant = merchantParser.parse(merchantKey)

      val request = helper.createSaleRequest(
        terminalNumber = merchant.terminalNumber,
        supplierNumber = merchant.supplierNumber,
        idPrefix = merchant.idPrefix,
        orderId = deal.map { _.id },
        card = creditCard,
        currencyAmount = currencyAmount
      )

      val requestXml = RequestParser.stringify(request)
      val responseXml = doRequest(
        user = merchant.user,
        password = merchant.password,
        requestXml = requestXml
      )
      val response = ResponseParser.parse(responseXml)
      verifyShvaStatusCode(
        statusCode = response.response.doDeal.status,
        errorMessage = response.response.doDeal.statusText
      )

      response.response.tranId
    } match {
      case Success(transactionId) => Success(transactionId)
      case Failure(e: PaymentException) => Failure(e)
      case Failure(e) => Failure(new PaymentErrorException(e.getMessage, e))
    }
  }

  override def voidAuthorization(merchantKey: String, authorizationKey: String): Try[String] = {
    Try {
//      val merchant = merchantParser.parse(merchantKey)
      val authorization = authorizationParser.parse(authorizationKey)

      // CreditGuard doesn't support voiding an authorization. Authorizations should be automatically voided after a while.
      authorization.tranId
    }
  }

  private def doRequest(user: String, password: String, requestXml: String): String = {
    val params = Map(
      Fields.user -> user,
      Fields.password -> password,
      Fields.int_in -> requestXml
    )

    val httpRequest = requestFactory.buildPostRequest(
      new GenericUrl(endpointUrl),
      new UrlEncodedContent(mapAsJavaMap(params))
    )

    connectTimeout foreach (duration => httpRequest.setConnectTimeout(duration.toMillis.toInt))
    readTimeout foreach (duration => httpRequest.setReadTimeout(duration.toMillis.toInt))
    httpRequest.setNumberOfRetries(numberOfRetries)

    val httpResponse = httpRequest.execute()
    try {
      httpResponse.parseAsString()
    } finally {
      httpResponse.ignore()
    }
  }

  private def verifyShvaStatusCode(statusCode: String, errorMessage: String): Unit = {
    statusCode match {
      case StatusCodes.success => // Operation successful.
      case IsShvaRejectedStatusCode(rejectedStatusCode) => throw new PaymentRejectedException(s"$errorMessage (code = $statusCode)")
      case _ => throw new PaymentErrorException(s"$errorMessage (code = $statusCode)")
    }
  }
}
