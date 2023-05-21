package com.example.tfg

class DataClass(private var imageURL: String, private var caption: String) {

    constructor() : this("", "")

    fun getImageURL(): String {
        return imageURL
    }

    fun setImageURL(imageURL: String) {
        this.imageURL = imageURL
    }

    fun getCaption(): String {
        return caption
    }

    fun setCaption(caption: String) {
        this.caption = caption
    }

}