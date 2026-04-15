package org.example.VectorLib

import kotlin.math.sqrt //allows the use of square root

data class Vec2(
    val x: Double,
    val y: Double
) : Comparable<Vec2> {
    operator fun plus(otherVec: Vec2): Vec2{
        return Vec2(x + otherVec.x, y + otherVec.y)
    }
    operator fun minus(otherVec: Vec2): Vec2{
        return Vec2(x - otherVec.x, y - otherVec.y)
    }
    operator fun times(value: Double): Vec2{ //parameter Double ex: Vector * 6.7
        return Vec2(x * value, y * value)
    }
    operator fun unaryMinus(): Vec2{ //-x, -y
        return Vec2(-x,-y)
    }
    override operator fun compareTo(otherVec: Vec2): Int{
        val thisVecLength = sqrt(x * x + y * y)
        val otherVecLength = sqrt(otherVec.x * otherVec.x + otherVec.y * otherVec.y)

        return thisVecLength.compareTo(otherVecLength)
        //return this.magnitude().compareTo(otherVec.magnitude()) //simpler verson where it calls the magnitude function directly
    }
    fun magnitude(): Double{
        return sqrt(x * x + y * y)
    }
    fun dot(otherVec: Vec2): Double{ //x1*x2+y1*y2
        return x * otherVec.x + y * otherVec.y
    }
    fun normalized(): Vec2{ //vector / length (removes size and focuses on length? need to search more abt this)
        if(x == 0.0 && y == 0.0){
            throw IllegalStateException("ERROR: The zero vector cannot be normalized")
        }
        val lenght = magnitude()
        return Vec2(x / lenght, y / lenght)
    }
    operator fun get(index: Int): Double{
        when(index){
            0 -> return x
            1 -> return y
            else -> throw IllegalStateException("ERROR: The index must be 0(x) or 1(y)")
        }
    }
    /*operator fun component1(): Double { // These two will enter into conflict with the class because this overload IS ALREADY ESTABLISHED USING THE **DATA** CLASS (essentialy we'd be overloading twice)
        return x
    }

    operator fun component2(): Double {
        return y
    }*/
}

operator fun Double.times(otherVec: Vec2): Vec2{ //recieves Double (and parameter Vector AFTER that) ex: 1.0 * Vector
    return Vec2(this * otherVec.x, this * otherVec.y)
}

fun main () {
    val a = Vec2 (3.0 , 4.0)
    val b = Vec2 (1.0 , 2.0)
    println ("a = $a ") // a = Vec2 (x =3.0 , y =4.0)
    println ("b = $b ") // b = Vec2 (x =1.0 , y =2.0)
    println ("a + b = ${a + b}") // a + b = Vec2 (x =4.0 , y =6.0)
    println ("a - b = ${a - b}") // a - b = Vec2 (x =2.0 , y =2.0)
    println ("a * 2.0 = ${a * 2.0} ") // a * 2.0 = Vec2 (x =6.0 , y =8.0)
    println ("2.0 * a = ${2.0 * a} ") // a * 2.0 = Vec2 (x =6.0 , y =8.0) (same as above)
    println (" -a = ${-a}") // -a = Vec2 (x = -3.0 , y = -4.0)
    println ("|a| = ${a. magnitude () }") // |a| = 5.0
    println ("a dot b = ${a. dot (b)}") // a dot b = 11.0
    println (" norm (a) = ${a. normalized () }") // norm (a) = Vec2 (x =0.6 , y =0.8)
    println ("a [0] = ${a [0]} ") // a [0] = 3.0
    println ("a [1] = ${a [1]} ") // a [1] = 4.0
    println ("a > b = ${a > b}") // a > b = true
    println ("a < b = ${a < b}") // a < b = false
    val vectors = listOf ( Vec2 (1.0 , 0.0) , Vec2 (3.0 , 4.0) , Vec2 (0.0 , 2.0) )
    println (" Longest = ${ vectors . max () }") // Longest = Vec2 (x =3.0 , y =4.0)
    println (" Shortest = ${ vectors . min () }") // Shortest = Vec2 (x =1.0 , y =0.0)
    val (x, y) = a
    println("ax= x") // ax= 3.0
    println("ay= y") // ay= 4.0
}
