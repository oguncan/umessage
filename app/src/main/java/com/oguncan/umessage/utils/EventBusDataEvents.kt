package com.oguncan.umessage.utils

import android.net.Uri
import com.oguncan.umessage.Model.Account

class EventBusDataEvents {
    internal class SendTelephoneOrMail(var telNo : String?, var email : String?, var verificationID : String?, var code : String?, var isMail : Boolean?)
    internal class SendAccountInfo(var account : Account)
    internal class SendShareImage(var image : Uri)
}