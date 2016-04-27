package com.wix.pay.creditguard

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class JsonCreditguardAuthorizationParser() extends CreditguardAuthorizationParser {
  private implicit val formats = DefaultFormats

  override def parse(authorizationKey: String): CreditguardAuthorization = {
    Serialization.read[CreditguardAuthorization](authorizationKey)
  }

  override def stringify(authorization: CreditguardAuthorization): String = {
    Serialization.write(authorization)
  }
}
