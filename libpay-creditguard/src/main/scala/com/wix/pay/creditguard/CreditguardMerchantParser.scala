package com.wix.pay.creditguard

trait CreditguardMerchantParser {
  def parse(merchantKey: String): CreditguardMerchant
  def stringify(merchant: CreditguardMerchant): String
}
