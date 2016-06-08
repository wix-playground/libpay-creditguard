package com.wix.pay.creditguard


import com.wix.pay.creditguard.CreditguardMatchers._
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope


class JsonCreditguardAuthorizationParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val authorizationParser: CreditguardAuthorizationParser = new JsonCreditguardAuthorizationParser
  }

  "stringify and then parse" should {
    "yield an authorization similar to the original one" in new Ctx {
      val someAuthorization = CreditguardAuthorization(
        authNumber = "some authorization number",
        currency = "some currency",
        tranId = "some transaction ID",
        cardId = "some card ID",
        cardExpiration = "some card expiration",
        user = "some user"
      )

      val authorizationKey = authorizationParser.stringify(someAuthorization)
      authorizationParser.parse(authorizationKey) must beAuthorization(
        authNumber = ===(someAuthorization.authNumber),
        currency = ===(someAuthorization.currency),
        tranId = ===(someAuthorization.tranId),
        cardId = ===(someAuthorization.cardId),
        cardExpiration = ===(someAuthorization.cardExpiration),
        user = ===(someAuthorization.user)
      )
    }
  }
}
