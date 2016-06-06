package com.wix.pay.creditguard

/**
  * @param idPrefix         Prefix for ''com.wix.pay.creditguard.model.DoDealRequest#user'' field
  */
case class CreditguardMerchant(user: String,
                               password: String,
                               terminalNumber: String,
                               supplierNumber: String,
                               idPrefix: String)
