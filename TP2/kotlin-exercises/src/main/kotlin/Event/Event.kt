package org.example.Event

sealed class Event {
    data class Login( //upon testing it was found that using data classes automatically generates a toString for the correct output in the filterByUser functiion
        val username: String,
        val timestamp: Long
    ) : Event()

    data class Purchase(
        val username: String,
        val amount: Double,
        val timestamp: Long
    ) : Event()

    data class Logout(
        val username: String,
        val timestamp: Long
    ) : Event()
}

fun List<Event>.filterByUser(username: String): List<Event> { //returns List<Event> filtered by username
    return this.filter { //filters list (goes through one element at a time and keeps only what's defined)
        when(it){
            is Event.Login -> it.username == username
            is Event.Purchase -> it.username == username
            is Event.Logout -> it.username == username
        } //this will essentially only keep the elements whose username corresponds to the argument
    }
}

fun List<Event>.totalSpent(username: String): Double { //returns Double of the total value a user has spent
    return this.filterIsInstance<Event.Purchase>() //filter list to only keep the Purchase element
        .filter{it.username == username} //filter again to keep only elements of the username on the argument
        .sumOf{it.amount} //sums all the purchases done (stored in val amount inside the class)
}

fun processEvents(events: List<Event>, handler: (Event) -> Unit) { //receives the List of events and a lambda function
    for (event in events) {
        handler(event)
    }
}

fun main() {
    val events = listOf ( //Sample data
        Event . Login ("alice" , 1_000 ) ,
        Event . Purchase ("alice" , 49.99 , 1_100 ) ,
        Event . Purchase ("bob" , 19.99 , 1_200 ) ,
        Event . Login ("bob" , 1_050 ) ,
        Event . Purchase ("alice" , 15.00 , 1_300 ) ,
        Event . Logout ("alice" , 1_400 ) ,
        Event . Logout ("bob" , 1_500 )
    )

    processEvents(events){
        when(it){
            is Event.Login -> println   ("[LOGIN]      ${it.username} logged in at t=${it.timestamp}")
            is Event.Purchase -> println("[PURCHASE]   ${it.username} spent $${it.amount} at t=${it.timestamp}")
            is Event.Logout -> println  ("[LOGOUT]     ${it.username} logged out at t=${it.timestamp}")
        }
    }

    println("Total spent by alice: ${"%.2f".format(events.totalSpent("alice"))}")
    println("Total spent by bob: ${"%.2f".format(events.totalSpent("bob"))}")

    println("Events for alice:\n${events.filterByUser("alice")}")
}
