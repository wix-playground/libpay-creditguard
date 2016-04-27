package com.wix.pay.creditguard


import com.wix.pay.creditguard.CreditguardMatchers._
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope


class JsonCreditguardMerchantParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val merchantParser: CreditguardMerchantParser = new JsonCreditguardMerchantParser
  }


  "stringify and then parse" should {
    "yield a merchant similar to the original one" in new Ctx {
      val someMerchant = CreditguardMerchant(
        user = "some user",
        password = "some password",
        terminalNumber = "some terminal number",
        supplierNumber = "some supplier number"
      )

      val merchantKey = merchantParser.stringify(someMerchant)
      merchantParser.parse(merchantKey) must beMerchant(
        user = ===(someMerchant.user),
        password = === (someMerchant.password),
        terminalNumber = ===(someMerchant.terminalNumber)
      )
    }
  }
}
