package com.wix.pay.creditguard.testkit


import scala.collection.JavaConversions._
import scala.collection.mutable
import java.util.{List => JList}
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.{StatusCodes => HttpStatusCodes}
import akka.http.scaladsl.model._
import com.google.api.client.http.UrlEncodedParser
import com.wix.e2e.http.api.StubWebServer
import com.wix.e2e.http.client.extractors.HttpMessageExtractors._
import com.wix.e2e.http.server.WebServerFactory.aStubWebServer
import com.wix.pay.creditcard.CreditCard
import com.wix.pay.creditguard.model._
import com.wix.pay.creditguard.{CreditguardHelper, RequestParser, ResponseParser}
import com.wix.pay.model.CurrencyAmount
import com.wix.pay.shva.model.StatusCodes


class CreditguardDriver(port: Int) {
  private val server: StubWebServer = aStubWebServer.onPort(port).build

  def start(): Unit = server.start()
  def stop(): Unit = server.stop()
  def reset(): Unit = server.replaceWith()


  def aSaleFor(user: String,
               password: String,
               terminalNumber: String,
               supplierNumber: String,
               idPrefix: String,
               orderId: Option[String],
               card: CreditCard,
               currencyAmount: CurrencyAmount): RequestCtx = {
    val request = CreditguardHelper.createSaleRequest(
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      idPrefix = idPrefix,
      orderId = orderId,
      card = card,
      currencyAmount = currencyAmount)

    new RequestCtx(
      user = user,
      password = password,
      request = request)
  }

  def anAuthorizeFor(user: String,
                     password: String,
                     terminalNumber: String,
                     supplierNumber: String,
                     idPrefix: String,
                     orderId: Option[String],
                     card: CreditCard,
                     currencyAmount: CurrencyAmount): RequestCtx = {
    val request = CreditguardHelper.createAuthorizeRequest(
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      idPrefix = idPrefix,
      orderId = orderId,
      card = card,
      currencyAmount = currencyAmount)

    new RequestCtx(
      user = user,
      password = password,
      request = request)
  }

  def aCaptureFor(user: String,
                  password: String,
                  terminalNumber: String,
                  supplierNumber: String,
                  authNumber: String,
                  currency: String,
                  amount: Double,
                  cardId: String,
                  cardExpiration: String,
                  userField: String): RequestCtx = {
    val request = CreditguardHelper.createCaptureRequest(
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      authNumber = authNumber,
      currency = currency,
      amount = amount,
      cardId = cardId,
      cardExpiration = cardExpiration,
      user = userField)

    new RequestCtx(
      user = user,
      password = password,
      request = request)
  }

  class RequestCtx(user: String, password: String, request: AshraitRequest) {
    def returns(transactionId: String): Unit = {
      val doDeal = new DoDealResponse
      doDeal.status = StatusCodes.success

      val response = new Response
      response.doDeal = doDeal
      response.tranId = transactionId

      val ashrait = new AshraitResponse
      ashrait.response = response

      returns(ashrait)
    }

    def returns(authNumber: String,
                cardId: String,
                cardExpiration: String,
                currency: String,
                transactionId: String): Unit = {
      val doDeal = new DoDealResponse
      doDeal.status = StatusCodes.success
      doDeal.authNumber = authNumber
      doDeal.cardId = cardId
      doDeal.cardExpiration = cardExpiration
      doDeal.currency = currency

      val response = new Response
      response.doDeal = doDeal
      response.tranId = transactionId

      val ashrait = new AshraitResponse
      ashrait.response = response

      returns(ashrait)
    }

    def failsOnInvalidMerchant(errorMessage: String): Unit = {
      val doDeal = new DoDealResponse
      doDeal.status = "405"
      doDeal.statusText = errorMessage

      val response = new Response
      response.doDeal = doDeal

      val ashrait = new AshraitResponse
      ashrait.response = response

      returns(ashrait)
    }

    def getsRejected(errorMessage: String): Unit = {
      val doDeal = new DoDealResponse
      doDeal.status = StatusCodes.rejected
      doDeal.statusText = errorMessage

      val response = new Response
      response.doDeal = doDeal

      val ashrait = new AshraitResponse
      ashrait.response = response

      returns(ashrait)
    }

    private def returns(response: AshraitResponse): Unit = {
      val responseXml = ResponseParser.stringify(response)

      server.appendAll {
        case HttpRequest(
          HttpMethods.POST,
          Path("/"),
          _,
          entity,
          _) if isStubbedRequestEntity(entity) =>
            HttpResponse(
              status = HttpStatusCodes.OK,
              entity = HttpEntity(ContentType(MediaTypes.`application/xml`, HttpCharsets.`UTF-8`), responseXml))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestParams = urlDecode(entity.extractAsString)

      requestParams.get(Fields.user).contains(user) &&
        requestParams.get(Fields.password).contains(password) &&
        requestParams.get(Fields.int_in).map { RequestParser.parse }.contains(request)
    }

    private def urlDecode(str: String): Map[String, String] = {
      val params = mutable.LinkedHashMap[String, JList[String]]()
      UrlEncodedParser.parse(str, mutableMapAsJavaMap(params))
      params.mapValues( _.head ).toMap
    }
  }
}
