package org.example.cm.exer_1

fun main() {
    val list1 = IntArray(50) //create IntArray with size 50
    for (i in 0 until 50){ //iterate i to go through the Array's entire index
        list1[i] = (i+1)*(i+1) //multiply i*i to get i squared (i+1 since it starts at 0)
    }
    println("List1 IntArray: " + list1.joinToString()) //joinToString -> Creates String by connecting all the elements

    val list2 = (1..50).map{it*it}.toIntArray() // 1..50 creates the range, map squares that element and toIntArray saves the intArray
    println("List2 map:      " + list2.joinToString())

    val list3 = Array(50){i->(i+1)*(i+1)} //uses lambda function to fill the Array
    println("List3 Array:    " + list3.joinToString())
}