package com.square.repos.view.bind

interface BindableAdapter<T> {
    fun setData(items: List<T>)
}