package com.square.repos

interface BindableAdapter<T> {
    fun setData(items: List<T>)
}