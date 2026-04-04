package org.example.cm.exer_3

fun main(){
    print("Type The Starting Height: ") //allow the user to type the starting height
    val maxH: Double
    try {
        maxH = readln().toDouble() //read line and transform to Double
        if (maxH < 0) { //exception in case of negative number
            throw IllegalArgumentException("Error: Input must be non-negative!") //will exit the program and show red text
        }
    } catch (e: NumberFormatException) { //exception in case of not being a number (symbols or letters)
        println("Error: You must type a number! ${e.message}")
        return
    }

    val bounces = generateSequence(maxH){ //define the seed as the starting height defined
        if(it*0.6<1) null else it*0.6 //when the next it goes below 1 it will return null to stop
    }

    println("List of Max Height of Each Bounce:\n"
            + bounces.drop(1).take(15) //drop skips the first bounce(starting height) and take takes the 15 numbers
        .joinToString("\n"){"%.2f".format(it)}) //uses format to define the current it as %.2f (2 decimals)
}