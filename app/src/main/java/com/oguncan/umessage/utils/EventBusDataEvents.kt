package com.oguncan.umessage.utils

class EventBusDataEvents {
    internal class SendTelephoneOrMail(var telNo : String?, var email : String?, var verificationID : String?, var code : String?, var isMail : Boolean?)

}