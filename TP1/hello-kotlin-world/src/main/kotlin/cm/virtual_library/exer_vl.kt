package org.example.cm.virtual_library

import org.example.cm.virtual_library.Library.Companion.getTotalBooksCreated


enum class DigitalFormat{ //I'm using an enum class solely as a preference decision
    PDF, EPUB, MOBI
}
abstract class Book( //open allows for inheritance -> this was later changed for abstract to allow for abstract methods
           val title: String,
           val author: String,
           val publicationYear: Int,
           availableCopies: Int){

                init{
                    println("The Book $title Written by $author has been Created.")
                }

                val pubYearCategory: String
                    get() { //custom getter for publicationYear
                        return if (publicationYear < 1980){
                            "Classic"
                        } else if (publicationYear in 1980..2010){
                            "Modern"
                        } else {
                            "Contemporary"
                        }
                    }
                var availableCopies: Int = availableCopies
                    set(quantity) { //custom setter for availableCopies
                        if (quantity < 0) {
                            println("Error: Available Copies can't be Negative!")
                        } else {
                            if (quantity == 0) {
                                println("Warning: Book is now out of stock!")
                            }
                            field = quantity //set value if it's indeed possible (wihtout going through the setter and entering an infinite loop)
                        }
                    }
    abstract fun getStorageInfo(): String //abstract function that'll be overridden on each subclass

    override fun toString(): String{ //overide the toString function for a custom output
        return "Title: $title || Author: $author || Year: $publicationYear || Category: $pubYearCategory || Copies: $availableCopies"
    }
}

class DigitalBook( //Digital Book subclass
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val fileSize: Double,
    val format: DigitalFormat) : Book(title, author, publicationYear, availableCopies){ //inherits Book's title, author, publicationYear and avaibleCopies as well as the setter, getter and init
    override fun getStorageInfo(): String{
        return "Stored digitally: $fileSize MB, Format: $format"
    }
    override fun toString(): String{
        return super.toString() + " || File size: $fileSize MB || Format: $format" //using super we don't need to write the properties of the Book class again
    }
}

class PhysicalBook( //Physical Book subclass
    title: String,
    author: String,
    publicationYear: Int,
    availableCopies: Int,
    val weight: Int,//since I'm already using grams I think it's unnecessary to use Double
    val hasHardcover: Boolean = true) : Book(title, author, publicationYear, availableCopies){
    override fun getStorageInfo(): String{
        return "Physical book: $weight g, Hardcover: $hasHardcover"
    }
    override fun toString(): String{
        return super.toString() + " || Weight: $weight g || Hardcover: $hasHardcover" //using super we don't need to write the properties of the Book class again
    }
}

class Library(private val libraryName: String){
    private val books = mutableListOf<Book>() //list of books (mutableList allows it to add and remove(not used in this project) books)

    companion object{ //companion object for seeing number of books added
        private var totalBooksCreated: Int = 0 //"Class members can access private members of their corresponding companion object"

        fun getTotalBooksCreated(): Int{
            return totalBooksCreated
        }
    }

    fun addBook(book: Book){
        books.add(book) //ads book to the end of the list
        println("The Book ${book.title} has been added to the Library.")
        totalBooksCreated++
    }
    fun borrowBook(title: String){
        val book = books.find {it.title == title} //uses 'find' to find the book
        if (book == null){ //if it failed to find it will return null
            println("The Book $title was not found in the Library.")
        } else {
            if (book.availableCopies > 0){
                println("You're now borrowing '${book.title}'. Available copies left: ${book.availableCopies-1}")
                book.availableCopies -= 1 //decrease avaiableCopies by 1 (it's after the previous line so the warning only appears after the borrowing is completed)

            } else {
                println("'${book.title}' has currently no available copies.")
            }
        }
    }
    fun returnBook(title: String){
        val book = books.find{it.title == title}
        if (book == null){
            println("The Book $title was not found in the Library.")
        } else {
            book.availableCopies += 1
            println("The Book $title has been Returned.")
        }
    }
    fun showBooks(){ //!!!It is worth noting that a simpler version of this function I discovered after concluding this program can be done by using the toString functions overridden!!! (I will provide this version at the end of the code)
        if (books.isEmpty()){ //isEmpty tells us if the list is empty (meaning there are no books to print)
            println("The Library currently has no Books.")
            return //end the function early if the list is empty (stopping it from doing unnecessary prints)
        }
        println("\nBooks in the Library:")
        for (book in books){ //simple iteration to run through the list
            println("Title: ${book.title}")
            println("Author: ${book.author}")
            println("Year: ${book.publicationYear}")
            println("Category: ${book.pubYearCategory}")
            println("Available Copies: ${book.availableCopies}")
            println("Storage: ${book.getStorageInfo()}")
            println("-----------------------------------------") //this line is used to determine the limits in between printing books
        }
    }
    fun searchByAuthor(author: String) {

        val results = books.filter{ it.author == author }//using filter we can select only books of the desired author
        //the rest is truly almost the same, the only note worthy change was the filter
        if (results.isEmpty()){
            println("The Library currently has no Books of this Author.")
            return
        }
        println("\nBooks in the Library written by $author:")
        for (book in results){
            println("Title: ${book.title}")
            println("Author: ${book.author}")
            println("Year: ${book.publicationYear}")
            println("Category: ${book.pubYearCategory}")
            println("Available Copies: ${book.availableCopies}")
            println("-----------------------------------------")
        }
    }
}

data class LibraryMember(
    val name: String,
    val membershipId: Int,
    val borrowedBooks: MutableList<String> = mutableListOf() //create empty list so it only needs to be added
)

fun main(){
    val library = Library (" Central Library ")
    val digitalBook = DigitalBook (
        " Kotlin in Action " ,
        " Dmitry Jemerov " ,
        2017 ,
        5,
        4.5 ,
        DigitalFormat.PDF
    )
    val physicalBook = PhysicalBook (
        " Clean Code " ,
        " Robert C. Martin " ,
        2008 ,
        3,
        650 ,
        true
    )
    val classicBook = PhysicalBook (
        " 1984 " ,
        " George Orwell " ,
        1949 ,
        2,
        400 ,
        false
    )
    library . addBook ( digitalBook )
    library . addBook ( physicalBook )
    library . addBook ( classicBook )
    library . showBooks ()
    println ("\n --- Borrowing Books ---")
    library . borrowBook (" Clean Code ")
    library . borrowBook (" 1984 ")
    library . borrowBook (" 1984 ")
    library . borrowBook (" 1984 ") // Should fail - no copies left
    println ("\n --- Returning Books ---")
    library . returnBook (" 1984 ")
    println ("\n --- Search by Author ---")
    library . searchByAuthor (" George Orwell ")

    println("Books created: ${getTotalBooksCreated()}")

    val member = LibraryMember("Martim", 707,)
    member.borrowedBooks.add("Tokyo Ghoul")
    member.borrowedBooks.add("O principezinho")
    println(member)
}

/*
Cleaner showBooks and showByAuthor Functions using toString:

fun showBooks(){
    if (books.isEmpty()){
        println("The library currently has no books.")
        return
    }
    for (book in books){
        println(book) //this should print the toString
    }
}


fun searchByAuthor(author: String){
    val results = books.filter{it.author == author}
    if (results.isEmpty()){
            println("The Library currently has no Books of this Author.")
            return
        }
    for (book in results){
        println(book) //prints toString
    }
}

*/