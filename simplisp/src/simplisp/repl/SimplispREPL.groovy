package simplisp.repl

import simplisp.environment.*
import simplisp.evaluator.*
import simplisp.parser.*

class SimplispREPL {
	static void main(String[] args) {
		def lineNumber = 0
		def console = System.console()
		def environment = Environment.getStandardEnvironment()
		def parser = new SimplispParser()
		while (true) {
			def line = console.readLine "simplisp:${String.format('%03d', lineNumber++)}> "
			if (line) {
				try {
					def tree = parser.parse(line)
					def result = SimplispEvaluator.evaluate(tree, environment)
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
}
