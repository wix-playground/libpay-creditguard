package com.wix.pay.creditguard.testkit

import java.util.{List => JList}

import com.google.api.client.http.UrlEncodedParser
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.pay.creditcard.CreditCard
import com.wix.pay.creditguard.model._
import com.wix.pay.creditguard.{CreditguardHelper, RequestParser, ResponseParser}
import com.wix.pay.model.CurrencyAmount
import spray.http._

import scala.collection.JavaConversions._
import scala.collection.mutable


class CreditguardDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)

  def start() {
    probe.doStart()
  }

  def stop() {
    probe.doStop()
  }

  def reset() {
    probe.handlers.clear()
  }

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
      currencyAmount = currencyAmount
    )

    new RequestCtx(
      user = user,
      password = password,
      request = request
    )
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
      currencyAmount = currencyAmount
    )

    new RequestCtx(
      user = user,
      password = password,
      request = request
    )
  }

  def aCaptureFor(user: String,
                  password: String,
                  terminalNumber: String,
                  supplierNumber: String,
                  authNumber: String,
                  currency: String,
                  amount: Double,
                  cardId: String,
                  cardExpiration: String): RequestCtx = {
    val request = CreditguardHelper.createCaptureRequest(
      terminalNumber = terminalNumber,
      supplierNumber = supplierNumber,
      authNumber = authNumber,
      currency = currency,
      amount = amount,
      cardId = cardId,
      cardExpiration = cardExpiration
    )

    new RequestCtx(
      user = user,
      password = password,
      request = request
    )
  }

  class RequestCtx(user: String, password: String, request: AshraitRequest) {
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

    def returns(response: AshraitResponse): Unit = {
      val responseXml = ResponseParser.stringify(response)

      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/"),
        _,
        entity,
        _) if isStubbedRequestEntity(entity) =>
          HttpResponse(
            status = StatusCodes.OK,
            entity = HttpEntity(ContentType(MediaTypes.`application/xml`, HttpCharsets.`UTF-8`), responseXml))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestParams = urlDecode(entity.asString)

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
