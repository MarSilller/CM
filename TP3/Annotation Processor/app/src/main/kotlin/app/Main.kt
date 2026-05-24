package app

fun main() {
    // Greeting Wrapper
    println("=== Running Greeting Wrapper ===")
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass)
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()

    println()

    // Regex Extractor
    println("=== Running Regex Extractor ===")
    val input = "Name: John Address: 123 Street"

    val extractor = DataProcessorExtractor(input)

    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}