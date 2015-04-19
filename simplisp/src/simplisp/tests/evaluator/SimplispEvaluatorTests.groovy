package simplisp.tests.evaluator

import groovy.util.GroovyTestCase;
import simplisp.environment.*
import simplisp.evaluator.*
import simplisp.parser.*

class SimplispEvaluatorTests extends GroovyTestCase {
	def environment = Environment.getStandardEnvironment()
	def parser = new SimplispParser()
	
	def evaluate(program) {
		SimplispEvaluator.evaluate(parser.parse(program), environment)
	}
	
	def void testShouldEvaluateQuote() {
		assert 'hello' == evaluate('(quote hello)')
	}
	
	def void testShouldEvaluateDefine() {		
		assert 10 == evaluate('(define x 10)')
		assert 10 == evaluate('x')
	}
	
	def void testShouldEvaluateBuiltinFunctions() {
		assert 15             == evaluate('(+ 1 2 3 4 5)')
		assert -13            == evaluate('(- 1 2 3 4 5)')
		assert 120            == evaluate('(* 1 2 3 4 5)')
		assert 0.008333333335 == evaluate('(/ 1 2 3 4 5)')
		assert evaluate('(> 5 4 3 2 1)')
		assert evaluate('(< 1 2 3 4 5)')
		assert evaluate('(>= 5 4 3 1 1)')
		assert evaluate('(<= 1 2 3 4 4)')
		assert evaluate('(= 1 1 1 1 1)')
		assert evaluate('(= (+ 4 5) (+ 5 4))')
		assert 10 == evaluate('(abs -10)')
		assert [1, 2, 3, 4] == evaluate('(list 1 2 3 4)')
		assert [2, 3, 4] == evaluate('(cdr (list 1 2 3 4))')
		assert 1 == evaluate('(car (list 1 2 3 4))')
		assert evaluate('(number? 1)')
		assert evaluate('(number? 3.14)')
		assert evaluate('(list? (list 1 2 3 4))')
		assert evaluate('(list? (cdr (list 1 2)))')
		assert evaluate('(symbol? (quote hello))')
		assert evaluate('(null? (list))')
		assert !evaluate('(null? (list 1 2 3))')		
		assert 1 == evaluate('(min 1 2 3)')
		assert 3 == evaluate('(min 5 4 3)')
		assert 3 == evaluate('(max 1 2 3)')
		assert 5 == evaluate('(max 5 4 3)')
		assert evaluate('(not (> 1 2))')
		assert !evaluate('(not (< 1 2))')
		assert !evaluate('(not #t)')
		assert evaluate('(not #f)')		assert 0 == evaluate('(length (list))')
		assert 3 == evaluate('(length (list 3 4 5))')
		assert 'true'  == evaluate('(if (<= 1 2) (quote true) (quote false))')
		assert 'false' == evaluate('(if (>= 1 2) (quote true) (quote false))')		
	}
		
}
