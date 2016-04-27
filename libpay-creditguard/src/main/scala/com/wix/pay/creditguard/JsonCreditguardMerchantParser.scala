package com.wix.pay.creditguard

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class JsonCreditguardMerchantParser() extends CreditguardMerchantParser {
  private implicit val formats = DefaultFormats

  override def parse(merchantKey: String): CreditguardMerchant = {
    Serialization.read[CreditguardMerchant](merchantKey)
  }

  override def stringify(merchant: CreditguardMerchant): String = {
    Serialization.write(merchant)
  }
}
