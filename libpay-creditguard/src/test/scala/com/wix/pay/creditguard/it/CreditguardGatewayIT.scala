package com.wix.pay.creditguard.it


import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.pay.creditcard.{CreditCard, CreditCardOptionalFields, YearMonth}
import com.wix.pay.creditguard.CreditguardMatchers._
import com.wix.pay.creditguard.testkit.CreditguardDriver
import com.wix.pay.creditguard.{CreditguardAuthorization, CreditguardMerchant, _}
import com.wix.pay.model.{CurrencyAmount, Deal, Payment}
import com.wix.pay.{PaymentErrorException, PaymentGateway, PaymentRejectedException}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope


class CreditguardGatewayIT extends SpecWithJUnit {
  val creditguardPort = 10010

  val requestFactory = new NetHttpTransport().createRequestFactory()
  val driver = new CreditguardDriver(port = creditguardPort)
  step {
    driver.start()
  }

  sequential

  trait Ctx extends Scope {
    val merchantParser = new JsonCreditguardMerchantParser()
    val authorizationParser = new JsonCreditguardAuthorizationParser()

    val someMerchant = CreditguardMerchant(
      user = "some user",
      password = "some password",
      terminalNumber = "some terminal number",
      supplierNumber = "some supplier number",
      idPrefix = "some ID prefix"
    )
    val merchantKey = merchantParser.stringify(someMerchant)

    val somePayment = Payment(currencyAmount = CurrencyAmount("ILS", 33.3))
    val someCreditCard = CreditCard(
      number = "4012888818888",
      expiration = YearMonth(2020, 12),
      additionalFields = Some(CreditCardOptionalFields.withFields(
        csc = Some("123"),
        holderId = Some("some holder ID"))
      )
    )

    val someDeal = Deal(id = "some deal ID")

    val someAuthorization = CreditguardAuthorization(
      authNumber = "someAuthorizationNumber",
      currency = "someCurrency",
      tranId = "someTransactionId",
      cardId = "someCardId",
      cardExpiration = "someCardExpiration",
      user = "someUser"
    )
    val authorizationKey = authorizationParser.stringify(someAuthorization)
    val someCaptureAmount = 11.1

    val creditguard: PaymentGateway = new CreditguardGateway(
      requestFactory = requestFactory,
      endpointUrl = s"http://localhost:$creditguardPort/",
      merchantParser = merchantParser,
      authorizationParser = authorizationParser
    )

    driver.reset()
  }

  "sale request via CreditGuard gateway" should {
    "gracefully fail on invalid merchant" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aSaleFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = somePayment.currencyAmount
      ) failsOnInvalidMerchant(someErrorMessage)

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "gracefully fail on rejected card" in new Ctx {
      val someErrorMessage = "some error message"
      driver.aSaleFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = somePayment.currencyAmount
      ) getsRejected(someErrorMessage)

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentRejectedException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield a transaction ID on valid request" in new Ctx {
      driver.aSaleFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        somePayment.currencyAmount
      ) returns(
        transactionId = someAuthorization.tranId
      )

      creditguard.sale(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
        deal = Some(someDeal)
      ) must beASuccessfulTry(
        check = ===(someAuthorization.tranId)
      )
    }
  }

  "authorize request via CreditGuard gateway" should {
    "gracefully fail on invalid merchant" in new Ctx {
      val someErrorMessage = "some error message"
      driver.anAuthorizeFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = somePayment.currencyAmount
      ) failsOnInvalidMerchant(someErrorMessage)

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "gracefully fail on rejected card" in new Ctx {
      val someErrorMessage = "some error message"
      driver.anAuthorizeFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        somePayment.currencyAmount
      ) getsRejected(someErrorMessage)

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
        deal = Some(someDeal)
      ) must beAFailedTry.like {
        case e: PaymentRejectedException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield an authorization key on valid request" in new Ctx {
      driver.anAuthorizeFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        idPrefix = someMerchant.idPrefix,
        orderId = Some(someDeal.id),
        card = someCreditCard,
        currencyAmount = somePayment.currencyAmount
      ) returns(
        authNumber = someAuthorization.authNumber,
        cardId = someAuthorization.cardId,
        cardExpiration = someAuthorization.cardExpiration,
        currency = someAuthorization.currency,
        transactionId = someAuthorization.tranId
      )

      creditguard.authorize(
        merchantKey = merchantKey,
        creditCard = someCreditCard,
        payment = somePayment,
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
      driver.aCaptureFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        authNumber = someAuthorization.authNumber,
        currency = someAuthorization.currency,
        amount = someCaptureAmount,
        cardId = someAuthorization.cardId,
        cardExpiration = someAuthorization.cardExpiration,
        userField = someAuthorization.user
      ) failsOnInvalidMerchant(someErrorMessage)

      creditguard.capture(
        merchantKey = merchantKey,
        authorizationKey = authorizationKey,
        amount = someCaptureAmount
      ) must beAFailedTry.like {
        case e: PaymentErrorException => e.message must contain(someErrorMessage)
      }
    }

    "successfully yield a transaction ID on valid request" in new Ctx {
      driver.aCaptureFor(
        user = someMerchant.user,
        password = someMerchant.password,
        terminalNumber = someMerchant.terminalNumber,
        supplierNumber = someMerchant.supplierNumber,
        authNumber = someAuthorization.authNumber,
        currency = someAuthorization.currency,
        amount = someCaptureAmount,
        cardId = someAuthorization.cardId,
        cardExpiration = someAuthorization.cardExpiration,
        userField = someAuthorization.user
      ) returns(
        transactionId = someAuthorization.tranId
      )

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
    driver.stop()
  }
}
