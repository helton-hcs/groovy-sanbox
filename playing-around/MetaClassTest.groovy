List.metaClass.collectWithIndex = { body ->
    def x = [delegate, 0..<delegate.size()].transpose()
    println x.class
    println body.class
    x.collect(body)
}

List.metaClass.groupInPairs = {
	delegate.collectWithIndex { item, i ->
		[delegate[i - 1], item]
	}.tail()
}

def compareEqualsOp = { args ->
	args.groupInPairs().collect { 
		it.first() == it.last() 
	}.every { it }
}

println compareEqualsOp([1, 2])
def doList = { Object ... args ->
	args as ArrayList
}

def x = doList(1, 2, 3, 4)
println "$x"
println "${x.class}"