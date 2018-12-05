package com.square.repos.view

interface BindableAdapter<T> {
    fun setData(items: List<T>)
}