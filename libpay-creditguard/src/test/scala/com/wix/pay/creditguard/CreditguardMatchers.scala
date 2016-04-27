package com.wix.pay.creditguard

import org.specs2.matcher.{AlwaysMatcher, Matcher, Matchers}

trait CreditguardMatchers extends Matchers {
  def authorizationParser: CreditguardAuthorizationParser

  def beMerchant(user: Matcher[String] = AlwaysMatcher(),
                 password: Matcher[String] = AlwaysMatcher(),
                 terminalNumber: Matcher[String] = AlwaysMatcher(),
                 supplierNumber: Matcher[String] = AlwaysMatcher()): Matcher[CreditguardMerchant] = {
    user ^^ { (_: CreditguardMerchant).user aka "user" } and
      password ^^ { (_: CreditguardMerchant).password aka "password" } and
      terminalNumber ^^ { (_: CreditguardMerchant).terminalNumber aka "terminal number" } and
      supplierNumber ^^ { (_: CreditguardMerchant).supplierNumber aka "supplier number" }
  }

  def beAuthorization(authNumber: Matcher[String] = AlwaysMatcher(),
                      currency: Matcher[String] = AlwaysMatcher(),
                      tranId: Matcher[String] = AlwaysMatcher(),
                      cardId: Matcher[String] = AlwaysMatcher(),
                      cardExpiration: Matcher[String] = AlwaysMatcher()): Matcher[CreditguardAuthorization] = {
    authNumber ^^ { (_: CreditguardAuthorization).authNumber aka "authorization number" } and
      currency ^^ { (_: CreditguardAuthorization).currency aka "currency" } and
      tranId ^^ { (_: CreditguardAuthorization).tranId aka "transaction ID" } and
      cardId ^^ { (_: CreditguardAuthorization).cardId aka "card ID" } and
      cardExpiration ^^ { (_: CreditguardAuthorization).cardExpiration aka "card expiration" }
  }

  def beAuthorizationKey(authorization: Matcher[CreditguardAuthorization]): Matcher[String] = {
    authorization ^^ { authorizationParser.parse(_: String) aka "parsed authorization"}
  }
}

object CreditguardMatchers extends CreditguardMatchers {
  override val authorizationParser = new JsonCreditguardAuthorizationParser()
}