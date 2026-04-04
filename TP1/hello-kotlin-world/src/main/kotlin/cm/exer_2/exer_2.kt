package org.example.cm.exer_2

fun main(){
    print("Type:\n '+' for addition\n" +
            " '-' for subtraction\n" +
            " '*' for multiplication\n" +
            " '/' for division:\n" +
            "Boolean Operations:\n" +
            " 'AND' , 'OR' , 'NOT'(uses first number)\n" +
            "Bitwise Shift Operations (uses decimals):\n" +
            " 'SHL' (shift left), 'SHR' (shift right)\n" +
            "OPTION: ") //print menu (println wouldn't place the cursor on the right place)
    val option = readln() //read the option (in String)
    val validOptions = arrayOf("+","-","*","/","and","AND","OR","or","NOT","not","SHL","shl","SHR","shr") //array with valid options
    if(option !in validOptions){ //exception in case of invalid option
        throw IllegalArgumentException("Error: Invalid Option!")
    }

    println("\n(For Boolean operations: (1) = True and (0) = False)\n" +
            "Type the first number (a):")
    val a: Int //define the first variable
    try {
        a = readln().toInt() //read line and transform to int
        if (a < 0) { //exception in case of negative number
            throw IllegalArgumentException("Error: Input must be non-negative!") //will exit the program and show red text
        }
    } catch (e: NumberFormatException) { //exception in case of not being a number (symbols or letters)
        println("Error: You must type a number! ${e.message}")
        return
    }
    println("Type the second number (b):")
    val b: Int
    try {
        b = readln().toInt()
        if (b < 0) {
            throw IllegalArgumentException("Error: Input must be non-negative!")
        }
    } catch (e: NumberFormatException) {
        println("Error: You must type a number! ${e.message}")
        return
    }

    when (option){
        "+" -> println("$a + $b = ${a+b} Hexadecimal: ${(a+b).toHexString()}") //$ -> show value inside string
        "-" -> println("$a - $b = ${a-b} Hexadecimal: ${(a-b).toHexString()}") //${...} -> calculates operation inside string
        "*" -> println("$a * $b = ${a*b} Hexadecimal: ${(a*b).toHexString()}") //toHexString transforms string to a string of hex numbers
        "/" -> {
            if(b == 0){
                println("Error: On Division you mustn't divide by 0!")
            } else {
                println("$a / $b = Decimal: ${a/b} Hexadecimal: ${(a/b).toHexString()}")
            }
        }
        "AND", "and" -> {
            if((a == 0|| a == 1) && (b == 0||b == 1)){ //confirm variables only include 1s and 0s
                val boolA = a == 1 //a == 1 will return true or false (boolean variable)
                val boolB = b == 1
                println("$boolA && $boolB = ${boolA&&boolB}")
            } else {
                println("On Boolean Operations you must only use 1s and 0s!")
            }
        }
        "OR", "or" -> {
            if((a == 0|| a == 1) && (b == 0 ||b == 1)){
                val boolA = a == 1
                val boolB = b == 1
                println("$boolA || $boolB = ${boolA||boolB}")
            } else {
                println("On Boolean Operations you must only use 1s and 0s!")
            }
        }
        "NOT", "not" -> {
            if(a == 0|| a == 1){
                val boolA = a == 1
                println("NOT $boolA = ${!boolA}")
            } else {
                println("On Boolean Operations you must only use 1s and 0s!")
            }
        }
        "SHL", "shl" -> {
            println("$a (${a.toString(2)}) shl $b = Decimal: ${a shl b} Binary: ${(a shl b).toString(2)}")
        } //toString(2) converts to binary since there's no toBinaryString (wasn't mandatory)
        "SHR", "shr" -> {
            println("$a (${a.toString(2)}) shr $b = Decimal: ${a shr b} Binary: ${(a shr b).toString(2)}")
        }
        else -> println("Error: Invalid Option!") //validation in case of failure (redundant since it's already verified at the moment of input)
    }
}