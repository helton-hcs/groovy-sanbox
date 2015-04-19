package simplisp.repl

import simplisp.environment.*
import simplisp.evaluator.*
import simplisp.parser.*

class SimplispREPL {
	static void main(String[] args) {
		def lineNumber = 0
		def input = new Scanner(System.in)
		def environment = SimplispEnvironment.getStandardEnvironment()
		def parser = new SimplispParser()
		while (true) {
			print "simplisp:${String.format('%03d', lineNumber++)}> "
			def line = input.nextLine()
			if (line) {
				if (line in ['quit', 'exit']) {
					break
				}
				else {
					try {
						def tree = parser.parse(line)
						def result = SimplispEvaluator.evaluate(tree, environment)
						println "=> $result"
					}
					catch (Exception ex) {
						println "[Error] ${ex.message}\n${ex.printStackTrace()}"
					}
				}
			}
		}
	}
}