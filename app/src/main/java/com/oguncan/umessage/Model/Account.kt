package com.oguncan.umessage.Model

class Account {

    var accountMailAddress : String? = null
    var accountNameAndSurname : String? = null
    var accountPassword : String? = null
    var accountUsername : String? = null
    var accountTelephoneNumber : String? = null
    var telephoneUseAsEmail : String? = null
    var accountUID : String? = null
    var accountDetails : AccountDetails? =  null

    constructor() {}

    constructor(accountMailAddress: String,
                accountNameAndSurname : String,
                accountPassword : String,
                accountUsername : String,
                accountTelephoneNumber: String,
                telephoneUseAsEmail:String,
                accountUID: String,
                accountDetails:AccountDetails){

        this.accountMailAddress = accountMailAddress
        this.accountNameAndSurname = accountNameAndSurname
        this.accountPassword = accountPassword
        this.accountUsername = accountUsername
        this.accountTelephoneNumber = accountTelephoneNumber
        this.telephoneUseAsEmail = telephoneUseAsEmail
        this.accountUID = accountUID
        this.accountDetails = accountDetails

    }
}