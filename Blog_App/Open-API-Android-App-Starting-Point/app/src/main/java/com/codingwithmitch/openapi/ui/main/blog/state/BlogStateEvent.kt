package com.codingwithmitch.openapi.ui.main.blog.state

sealed class BlogStateEvent {

    data class BlogSearchEvent(var query : String): BlogStateEvent()

    class None : BlogStateEvent()

}