package com.wix.pay.creditguard.it


import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.pay.creditcard.{CreditCard, CreditCardOptionalFields, YearMonth}
import com.wix.pay.creditguard.CreditguardMatchers._
import com.wix.pay.creditguard.model._
import com.wix.pay.creditguard.testkit.CreditguardDriver
import com.wix.pay.creditguard.{CreditguardAuthorization, CreditguardMerchant, _}
import com.wix.pay.model.{CurrencyAmount, Deal}
import com.wix.pay.shva.model.StatusCodes
import com.wix.pay.{PaymentErrorException, PaymentGateway, PaymentRejectedException}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope


class CreditguardGatewayIT extends SpecWithJUnit {
  val creditguardPort = 10010

  val requestFactory = new NetHttpTransport().createRequestFactory()
  val driver = new CreditguardDriver(port = creditguardPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val merchantParser = new JsonCreditguardMerchantParser()
    val authorizationParser = new JsonCreditguardAuthorizationParser()

    val someMerchant = CreditguardMerchant(
      user = "some user",
      password = "some password",
      terminalNumber = "some terminal number",
      supplierNumber = "some supplier number"
    )
    val merchantKey = merchantParser.stringify(someMerchant)

    val someCurrencyAmount = CurrencyAmount("ILS", 33.3)
    val someCreditCard = CreditCard(
      number = "4012888818888",
      expiration = YearMonth(2020, 12),
      additionalFields = Some(CreditCardOptionalFields.withFields(
        csc = Some("123"),
        holderId = Some("some holder ID"))
      )
    )

    val someDeal = Deal(id = "some deal ID")

    private val helper = new CreditguardHelper

    val someAuthorization = CreditguardAuthorization(
      authNumber = "someAuthorizationNumber",
      currency = "someCurrency",
      tranId = "someTransactionId",
      cardId = "someCardId",
      cardExpiration = "someCardExpiration"
    )
    val authorizationKey = authorizationParser.stringify(someAuthorization)
    val someCaptureAmount = 11.1

    def aSaleRequest(): AshraitRequest = {
      helper.createSaleRequest(
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = someCurrencyAmount
      )
    }

    def anAuthorizeRequest(): AshraitRequest = {
      helper.createAuthorizeRequest(
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = someCurrencyAmount
      )
    }

    def aCaptureRequest(): AshraitRequest = {
      helper.createCaptureRequest(
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        authNumber = someAuthorization.authNumber,
        currency = someAuthorization.currency,
        amount = someCaptureAmount,
        cardId = someAuthorization.cardId,
        cardExpiration = someAuthorization.cardExpiration
      )
    }

    def anInvalidMerchantResponse(errorMessage: String): AshraitResponse = {
      val doDeal = new DoDealResponse
      doDeal.status = "405"
      doDeal.statusText = errorMessage

      val response = new Response
      response.doDeal = doDeal

      val ashrait = new AshraitResponse
      ashrait.response = response
      ashrait
    }

    def aPaymentRejectedResponse(errorMessage: String): AshraitResponse = {
      val doDeal = new DoDealResponse
      doDeal.status = StatusCodes.rejected
      doDeal.statusText = errorMessage

      val response = new Response
      response.doDeal = doDeal

      val ashrait = new AshraitResponse
      ashrait.response = response
      ashrait
    }

    def aSuccessfulResponse(): AshraitResponse = {
      val doDeal = new DoDealResponse
      doDeal.status = StatusCodes.success
      doDeal.authNumber = someAuthorization.authNumber
      doDeal.cardId = someAuthorization.cardId
      doDeal.cardExpiration = someAuthorization.cardExpiration
      doDeal.currency = someAuthorization.currency

      val response = new Response
      response.doDeal = doDeal
      response.tranId = someAuthorization.tranId

      val ashrait = new AshraitResponse
      ashrait.response = response
      ashrait
    }

    val creditguard: PaymentGateway = new CreditguardGateway(
      requestFactory = requestFactory,
      endpointUrl = s"http://localhost:$creditguardPort/",
      merchantParser = merchantParser,
      authorizationParser = authorizationParser
    )

    driver.resetProbe()
  }

  "sale request via CreditGuard gateway" should {
    "gracefully fail on invalid merchant" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = aSaleRequest()
      ) returns anInvalidMerchantResponse(someErrorMessage)

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "gracefully fail on rejected card" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = aSaleRequest()
      ) returns aPaymentRejectedResponse(someErrorMessage)

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentRejectedException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield a transaction ID on valid request" in new Ctx {
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = aSaleRequest()
      ) returns aSuccessfulResponse()

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beASuccessfulTry(
        check = ===(someAuthorization.tranId)
      )
    }
  }

  "authorize request via CreditGuard gateway" should {
    "gracefully fail on invalid merchant" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = anAuthorizeRequest()
      ) returns anInvalidMerchantResponse(someErrorMessage)

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "gracefully fail on rejected card" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = anAuthorizeRequest()
      ) returns aPaymentRejectedResponse(someErrorMessage)

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentRejectedException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield an authorization key on valid request" in new Ctx {
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = anAuthorizeRequest()
      ) returns aSuccessfulResponse()

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        currencyAmount = someCurrencyAmount,
        deal = Some(someDeal)
      ) must beASuccessfulTry(
        check = beAuthorizationKey(
          authorization = beAuthorization(
            authNumber = ===(someAuthorization.authNumber),
            currency = ===(someAuthorization.currency),
            tranId = ===(someAuthorization.tranId),
            cardId = ===(someAuthorization.cardId),
            cardExpiration = ===(someAuthorization.cardExpiration)
          )
        )
      )
    }
  }

  "capture request via CreditGuard gateway" should {
    "gracefully fail on invalid merchant" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = aCaptureRequest()
      ) returns anInvalidMerchantResponse(someErrorMessage)

      creditguard.capture(
        merchantKey = merchantKey,
        authorizationKey = authorizationKey,
        amount = someCaptureAmount
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield a transaction ID on valid request" in new Ctx {
      driver.aRequestFor(
        user = someMerchant.user,
        password = someMerchant.password,
        request = aCaptureRequest()
      ) returns aSuccessfulResponse()

      creditguard.capture(
        merchantKey = merchantKey,
        authorizationKey = authorizationKey,
        amount = someCaptureAmount
      ) must beASuccessfulTry(
        check = ===(someAuthorization.tranId)
      )
    }
  }

  step {
    driver.stopProbe()
  }
}
