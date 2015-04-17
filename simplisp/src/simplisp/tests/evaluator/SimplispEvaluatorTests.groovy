package simplisp.tests.evaluator

import groovy.util.GroovyTestCase;
import simplisp.environment.*
import simplisp.evaluator.*
import simplisp.parser.*

class SimplispEvaluatorTests extends GroovyTestCase {
	def environment = Environment.getStandardEnvironment()
	def parser = new SimplispParser()
	
	def run(program) {
		SimplispEvaluator.evaluate(parser.parse(program), environment)
	}
	
	def void testThis() {		
		println run('(define x 10)')
		println run('x')
	}
}
