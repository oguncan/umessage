package com.oguncan.umessage.Model

class AccountDetails {

    var detailsPosts : String? = null
    var detailsFollower : String? = null
    var detailsFollow : String? = null
    var detailsProfilePicture : String? = null
    var detailsWebsite : String? =  null
    var detailsComment : String? = null

    constructor(){}
    constructor(detailsPosts : String,
                detailsFollower:String,
                detailsFollow : String,
                detailsProfilePicture:String,
                detailsWebsite:String,
                detailsComment:String){
        this.detailsPosts = detailsPosts
        this.detailsFollower = detailsFollower
        this.detailsFollow = detailsFollow
        this.detailsProfilePicture = detailsProfilePicture
        this.detailsWebsite = detailsWebsite
        this.detailsComment = detailsComment
    }
}