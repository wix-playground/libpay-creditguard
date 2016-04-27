package com.wix.pay.creditguard

trait CreditguardAuthorizationParser {
  def parse(authorizationKey: String): CreditguardAuthorization
  def stringify(authorization: CreditguardAuthorization): String
}
