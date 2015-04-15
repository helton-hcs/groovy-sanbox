import groovy.transform.*

@AnnotationCollector([ToString, EqualsAndHashCode, TupleConstructor])
@interface SimplispAnnotations {}

/***********************************************************************
 * Extensions
 ***********************************************************************/
Character.metaClass.isEOF = {
	((byte)delegate) == -1
}

List.metaClass.car = {
	delegate[1]
}

List.metaClass.cdr = {
	delegate[2]
}

/***********************************************************************
 * Environment
 ***********************************************************************/
@SimplispAnnotations
class Environment {
	def parentEnvironment

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
	def node

	static check(list) {
		if (list.isEmpty()) {
			list
		}
		else {
			switch(list.car) {
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

class DefineSpecialForm extends SpecialForm {
	def eval(environment) {
        def symbol = node.cdr.car
        environment.putValue(symbol.name, node.cdr.cdr.car.eval(environment))
        null
	}
}

class LambdaSpecialForm extends SpecialForm {
	def eval(environment) {
/*
            @SuppressWarnings("unchecked")
            final MumblerListNode<Node> formalParams =
            (MumblerListNode<Node>) this.node.cdr.car;
            final MumblerListNode<Node> body = this.node.cdr.cdr;
            return new Function() {
                @Override
                public Object apply(Object... args) {
                    Environment lambdaEnv = new Environment(parentEnv);
                    if (args.length != formalParams.length()) {
                        throw new RuntimeException(
                                "Wrong number of arguments. Expected: " +
                                        formalParams.length() + ". Got: " +
                                        args.length);
                    }

                    // Map parameter values to formal parameter names
                    int i = 0;
                    for (Node param : formalParams) {
                        SymbolNode paramSymbol = (SymbolNode) param;
                        lambdaEnv.putValue(paramSymbol.name, args[i]);
                        i++;
                    }

                    // Evaluate body
                    Object output = null;
                    for (Node node : body) {
                        output = node.eval(lambdaEnv);
                    }

                    return output;
                }
            };
*/
	}

}

class IfSpecialForm extends SpecialForm {
	def eval(environment) {
		def testNode = node.cdr.car
		def thenNode = node.cdr.cdr.car
		def elseNode = node.cdr.cdr.cdr.car
		def result = testNode.eval(environment)
		if (result.isEmpty() || result == Boolean.False) {
			elseNode.eval environment
		}
		else {
			thenNode.eval environment			
		}
	}
}

class QuoteSpecialForm extends SpecialForm {
	def eval(environment) {
		node.cdr.car
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
    	switch (ch) {
    		case ')':
    			break
    		case { it.isEOF() }:
    			throw new EOFException('EOF reached before closing of list')
    		default:
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
        symbol += ch
        ch = stream.read()
    }
    stream.unread ch
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
	while (true) {
		def line = console.readLine "simplisp:${String.format('%03d', lineNumber++)}> " 
		if (line) {
			try {
				tree = read(new ByteArrayInputStream(line.getBytes()))
				println "=> ${tree.eval()}"
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