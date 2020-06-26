package com.teguh.kawankombur.model


class Users {
    // deklrasi properti
    private var uid: String = ""
    private var username: String = ""
    private var picture: String = ""
    private var cover: String = ""
    private var status: String = ""
    private var search: String = ""
    private var facebook: String = ""
    private var instagram: String = ""
    private var website: String = ""

    constructor()

    constructor(
        uid: String,
        username: String,
        picture: String,
        cover: String,
        status: String,
        search: String,
        facebook: String,
        instagram: String,
        website: String
    ) {
        this.uid = uid
        this.username = username
        this.picture = picture
        this.cover = cover
        this.status = status
        this.search = search
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
    }

    fun getUID(): String{
        return uid
    }

    fun setUID(uid: String){
        this.uid = uid
    }

    fun getUsername(): String{
        return username
    }

    fun setUsername(username: String){
        this.username = username
    }

    fun getPicture(): String{
        return picture
    }

    fun setPicture(picture: String){
        this.picture = picture
    }

    fun getCover(): String {
        return cover
    }

    fun setCover(cover: String){
        this.cover = cover
    }

    fun getStatus(): String {
        return status
    }

    fun setStatus(status: String){
        this.status = status
    }

    fun getSearch(): String {
        return search
    }

    fun setSearch(search: String){
        this.search = search
    }

    fun getFacebook(): String{
        return facebook
    }

    fun setFacebook(facebook: String){
        this.facebook = facebook
    }

    fun getInstagram(): String {
        return instagram
    }

    fun serInstagram(instagram: String){
        this.instagram = instagram
    }

    fun getWebsite(): String {
        return website
    }

    fun setWebsite(website: String){
        this.website = website
    }

}