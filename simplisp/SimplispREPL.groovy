import groovy.transform.*

@AnnotationCollector([ToString, EqualsAndHashCode, TupleConstructor])
@interface SimplispAnnotations {}

/***********************************************************************
 * Extensions
 ***********************************************************************/
Character.metaClass.isEOF = {
	((byte)delegate) == -1
}

ArrayList.metaClass.car = {
	delegate.first()
}

ArrayList.metaClass.cdr = {
	delegate.tail()
}

/***********************************************************************
 * Environment
 ***********************************************************************/
@SimplispAnnotations
class Environment {
	def parentEnvironment
	def private map = [:]

	def putValue(name, value) {
		map."$name" = value
	}

	def getValue(name) {
		if (map.containsKey(name)) {
			map."$name"
		}
		else if (parentEnvironment) {
			parentEnvironment.getValue name
		}
		else {
			throw new RuntimeException("No variable: $name")
		}
	}
	
	def static getBaseEnvironment() {
		new Environment() //to-do: add functions
	}
}

/***********************************************************************
 * Nodes
 ***********************************************************************/

@SimplispAnnotations
class NumberNode {
	def final num
	
	def eval(environment = null) {
		num
	}
}

@SimplispAnnotations
class BooleanNode {
	def final value
	def static TRUE  = new BooleanNode(Boolean.TRUE)
	def static FALSE = new BooleanNode(Boolean.FALSE)

	def eval(environment = null) {
		value
	}	
}

@SimplispAnnotations
class SymbolNode {
	def final name
	
	String toString() {
		"'$name"
	}

	def eval(environment) {
		environment.getValue name
	}
}

@SimplispAnnotations
class SpecialForm {
	def nodes

	static check(list) {
		if (list.isEmpty()) {
			list
		}
		else {
			switch(list.car()) {
				case { it == new SymbolNode('define') }:
					new DefineSpecialForm(list)
					break
				case { it == new SymbolNode('lambda') }:
					new LambdaSpecialForm(list)
					break
				case { it == new SymbolNode('if') }:
					new IfSpecialForm(list)
					break
				case { it == new SymbolNode('quote') }:
					new QuoteSpecialForm(list)
					break
				default:
					list
			}
		}
	}
}

@InheritConstructors
class DefineSpecialForm extends SpecialForm {
	def eval(environment) {
        def symbol = nodes.cdr().car()
        environment.putValue(symbol.name, nodes.cdr().cdr().car().eval(environment))
        null
	}
}

@InheritConstructors
class LambdaSpecialForm extends SpecialForm {
	def eval(environment) {
		def formalParams = nodes.cdr().car()
		def body = nodes.cdr().cdr()
		def function = [apply: { args ->
			def lambdaEnvironment = new Environment(lambdaEnvironment)
			if (args.length != formalParams.length()) {
				throw new RuntimeException("Wrong number of arguments. Expected: ${formalParams.length()}. Got: ${args.length}")
			}
			formalParams.eachWithIndex { param, index ->
				lambdaEnvironment.putValue(param.name, args[index])
			}
			def result = null
			body.each {
				result = it.eval lambdaEnvironment
			}
			result
		}]
	}

}

@InheritConstructors
class IfSpecialForm extends SpecialForm {
	def eval(environment) {
		assert nodes.size() == 3 : "If statement should have only 3 expressions"
		def testNode = nodes.cdr().car()
		def thenNode = nodes.cdr().cdr().car()
		def elseNode = nodes.cdr().cdr().cdr().car()
		def result = testNode.eval environment
		if (!result || result == Boolean.FALSE) {
			elseNode.eval environment
		}
		else {
			thenNode.eval environment			
		}
	}
}

@InheritConstructors
class QuoteSpecialForm extends SpecialForm {
	def eval(environment) {
		nodes.cdr().car()
	}
}
/***********************************************************************
 * REPL
 ***********************************************************************/
def readWhitespaces(stream) {
	char ch = stream.read()
	while (ch.isWhitespace()) {
		ch = stream.read()
	}
	stream.unread ch
}

def readList(stream) {
	def nodes = []
    char paren = stream.read()
    assert paren == '(' : "Reading a list must start with '('"
    while (true) {
    	readWhitespaces stream
    	char ch = stream.read()
    	if (ch == ')') {
    		break
    	}
    	else if (ch.isEOF()) {
    		throw new EOFException('EOF reached before closing of list')
    	}
    	else {
			stream.unread ch
			nodes << read(stream)    		
    	}
    }
    SpecialForm.check(nodes)
}

def readNumber(stream) {
	def stringNumber = ''
	char ch = stream.read()
    while (ch.isDigit()) {
        stringNumber += ch
        ch = stream.read()
    }
    stream.unread ch
    new NumberNode(stringNumber.toLong())
}

def readBoolean(stream) {
	char hash = stream.read()
	assert hash == '#' : "Reading a boolean must start with '#'"

	def symbol = readSymbol stream
	switch (symbol) {
		case new SymbolNode('t'):
			BooleanNode.TRUE
			break
		case new SymbolNode('f'):
			BooleanNode.FALSE
			break
		default:
			throw new IllegalArgumentException("Unknown value: #${symbol.name}")
	}
}

def readSymbol(stream) {
	def symbol = ''
	char ch = stream.read()
    while (!(ch.isEOF() || ch.isWhitespace() || ch in ['(', ')'])) {
    	println "[Debug] Reading $ch..."
        symbol += ch
        ch = stream.read()
    }
    stream.unread ch
    println "[Debug] Symbol readed = $symbol"
    new SymbolNode(symbol)	
}

def read(InputStream istream) {
	read new PushbackReader(new InputStreamReader(istream))
}

def read(PushbackReader stream) {
	readWhitespaces stream
	char ch = stream.read()
	if (ch) {
		stream.unread(ch)
		switch (ch) {
			case '(':				
				readList stream			
				break
			case { it.isDigit() }:
				readNumber stream
				break
			case '#':
				readBoolean stream
				break
			case ')':
				throw new IllegalArgumentException('Unmatched close parenthesis')
			default:
				readSymbol stream
		}
	}
}

def startREPL() {
	def lineNumber = 0
	def console = System.console()
	def environment = Environment.getBaseEnvironment()
	while (true) {
		def line = console.readLine "simplisp:${String.format('%03d', lineNumber++)}> " 
		if (line) {
			try {
				tree = read(new ByteArrayInputStream(line.getBytes()))
				println "[Debug] tree = $tree"
				result = tree.eval(environment)
				println "=> $result"
			}
			catch (Exception ex) {
				println "[Error] ${ex.message}\n${ex.printStackTrace()}"
			}
		}
		else {
			break
		}
	}
}

def runSimplisp(filename) {
	def topEnvironment = Environment.getBaseEnvironment()
	def nodes = read(new FileInputStream(filename))
	nodes.each {
		it.eval topEnvironment		
	}
}

assert args.length < 2 : "SimplispREPL only accepts 1 or 0 files";
if (args.length == 0) {
    startREPL()
}
else {
    runSimplisp args[0]
}
