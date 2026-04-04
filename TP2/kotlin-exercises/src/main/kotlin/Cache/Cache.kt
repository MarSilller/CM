package org.example.Cache

class Cache<K : Any, V : Any> {
    val stores: MutableMap<K, V> = mutableMapOf()

    fun put(key: K, value: V){
        stores[key] = value
    }

    fun get(key: K): V?{
        return stores[key]
    }

    fun evict(key: K){
        stores.remove(key)
    }

    fun size(): Int{
        return stores.size
    }

    fun getOrPut(key: K, default: () -> V): V{
        if (key in stores) {
            return stores[key]!! //!! does V? -> V
        } else {
            val value = default()
            stores[key] = value
            return value
        }
    }

    //fun getOrPut(key: K, default: () -> V): V { //another version using .getOrPut builtin function
    //    return stores.getOrPut(key, default)
    //}

        fun transform(key: K, action: (V) -> V): Boolean{
            if (key in stores) {
                val currentValue = stores[key]!!
                val newValue = action(currentValue)
                stores[key] = newValue
                return true
            } else {
                return false
            }
        }

        fun snapshot(): Map<K, V> {
            return stores.toMap() //map is read only so just returning a map version of stores (using toMAp) creates an immutable copy
        }

    fun filterValues(predicate: (V) -> Boolean) : Map<K, V>{
        return stores.filterValues(predicate) //filterValues returns map just like toMap
    }
}

fun main(){
    println("--- Word frequency cache ---")
    val wordCache = Cache<String, Int>()
    wordCache.put("kotlin", 1)
    wordCache.put("scala", 1)
    wordCache.put("haskell", 1)

    println("Size: ${wordCache.size()}")
    println("frequency of \"kotlin\": ${wordCache.get("kotlin")}")
    println("getOrPut \"kotlin\": ${wordCache.getOrPut("kotlin"){999999999}}") //prints 1 because it already exists so doesnt put only gets
    println("getOrPut \"java\": ${wordCache.getOrPut("java"){0}}") //will put java with value 0 because it does not exists
    println("Size after GetOrPut: ${wordCache.size()}")

    println("Transform \"kotlin\" (+1): ${wordCache.transform("kotlin"){it + 1}}") //returns true because already exists and then adds 1 to the value
    println("Transform \"Cobol\" (+1): ${wordCache.transform("cobol"){it + 1}}") //returns falsse because it doesnt exists and ignores the rest

    println("Snapshot: ${wordCache.snapshot()}")

    println("Words with the value greater than 0: ${wordCache.filterValues {it > 0}}")

    println("\n--- Id Registry Cache ---")
    val IdCache = Cache<Int, String>()
    IdCache.put(1, "Alice")
    IdCache.put(2, "Bob")
    println("Id 1 -> ${IdCache.get(1)}")
    println("Id 2 -> ${IdCache.get(2)}")

    IdCache.evict(1)
    println("After evict id 1, size = ${IdCache.size()}")
    println("id 1 after evict -> ${IdCache.get(1)}") //returns null because itt does't exist anymore
}
