(1..3).each { i ->
    println "In a closure: $i" 
}

println "Printing evens..."
(1..10).findAll{it % 2 == 0}.each { 
    println "In a closure: $it" 
}

//Calling closure directly, passing parameters
({ println "In a closure: $it" }) "test"
({ println "In a closure: $it" }) 123