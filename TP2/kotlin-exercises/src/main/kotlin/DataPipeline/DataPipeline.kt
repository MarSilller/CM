package org.example.DataPipeline

class Pipeline {
    data class Stage(  //class with the properties of a stage
        val name: String,
        val transform: (List<String>) -> List<String>
    )

    val stages = mutableListOf<Stage>() //create a mutuable list to add stages

    fun addStage(name: String, transform: (List<String>) -> List<String>){
        stages.add(Stage(name, transform))
    }

    fun execute(input: List<String>): List<String>{
        var result = input

        for (stage in stages){
            result = stage.transform(result) //result will save the transformation of the previous result
        }

        return result
    }

    fun describe(){
        var counter = 0
        for (stage in stages){
            println("Stage $counter: ${stage.name}")
            counter++
        }
    }

    fun compose(stage1Name: String, stage2Name: String, newName: String) {
        var stage1: Stage? = null
        var stage2: Stage? = null

        for (stage in stages){
            if(stage.name == stage1Name){
                stage1 = stage
            }
        }

        for (stage in stages){
            if(stage.name == stage2Name){
                stage2 = stage
            }
        }

        if(stage1 == null || stage2 == null){
            return
        }

        stages.remove(stage1)
        stages.remove(stage2)

        val newTransform: (List<String>) -> List<String> = { input ->
            stage2.transform(stage1.transform(input))
        }

        //val newTransform = stage1.transform.andThen(stage2.transform) in theory should work but isn't
        addStage(newName, newTransform)
    }
}

fun buildPipeline(lambda: Pipeline.() -> Unit): Pipeline { //lambda with Pipeline as it's receiver
    val pipeline = Pipeline() //create pipeline instance
    pipeline.apply(lambda)
    return pipeline
}

fun main() {
    val logPipeline = buildPipeline {
        addStage("Trim") { lines -> //receives list of strings
            val result = mutableListOf<String>()

            for (line in lines) {
                result.add(line.trim())
            }

            result //value returned inside the lambda
        }
        addStage("Filter errors") { lines -> //receives list of strings
            val result = mutableListOf<String>()

            for (line in lines) {
                if (line.contains("ERROR")) {
                    result.add(line)
                }
            }

            result
        }
        addStage("Uppercase") { lines -> //receives list of strings
            val result = mutableListOf<String>()

            for (line in lines){
                result.add(line.uppercase())
            }

            result
        }
        addStage("Add index") { lines -> //receives list of strings
            val result = mutableListOf<String>()
            var counter = 1

            for (line in lines){
                result.add("$counter. $line")
                counter++
            }

            result
        }
    }

    val logs = listOf ( //sample input
        " INFO : server started " ,
        " ERROR : disk full " ,
        " DEBUG : checking config " ,
        " ERROR : out of memory " ,
        " INFO : request received " ,
        " ERROR : connection timeout "
    )

    println("Pipeline stages:")
    logPipeline.describe()

    logPipeline.compose("Trim","Filter errors","Trim and Filter errors")
    println("\nNew Pipeline stages:")
    logPipeline.describe()

    val result = logPipeline.execute(logs)
    println("\nresult:")   // Expected output
    for (line in result) { //1. ERROR : DISK FULL
        println(line)      //2. ERROR : OUT OF MEMORY
    }                      //3. ERROR : CONNECTION TIMEOUT
}
