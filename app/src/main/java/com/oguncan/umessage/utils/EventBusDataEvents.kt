package com.oguncan.umessage.utils

import com.oguncan.umessage.Model.Account

class EventBusDataEvents {
    internal class SendTelephoneOrMail(var telNo : String?, var email : String?, var verificationID : String?, var code : String?, var isMail : Boolean?)
    internal class SendAccountInfo(var account : Account)
}