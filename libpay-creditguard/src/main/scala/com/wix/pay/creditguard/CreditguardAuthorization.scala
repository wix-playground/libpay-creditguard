package com.wix.pay.creditguard

case class CreditguardAuthorization(authNumber: String,
                                    currency: String,
                                    tranId: String,
                                    cardId: String,
                                    cardExpiration: String)
