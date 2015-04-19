package simplisp.tests.evaluator

import groovy.util.GroovyTestCase;
import simplisp.environment.*
import simplisp.evaluator.*
import simplisp.parser.*

class SimplispEvaluatorTests extends GroovyTestCase {
	def environment = SimplispEnvironment.getStandardEnvironment()
	def parser = new SimplispParser()
	
	def evaluate(program) {
		SimplispEvaluator.evaluate(parser.parse(program), environment)
	}
	
	def void testShouldEvaluateQuote() {
		assert 'hello' == evaluate('(quote hello)')
		assert ['+', 1, 2] == evaluate ('(quote (+ 1 2))')
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
		assert evaluate('(not #f)')	
		assert 0 == evaluate('(length (list))')
		assert 3 == evaluate('(length (list 3 4 5))')
		assert 'true'  == evaluate('(if (<= 1 2) (quote true) (quote false))')
		assert 'false' == evaluate('(if (>= 1 2) (quote true) (quote false))')
		assert 	evaluate('(lambda? (lambda (x y) (+ x y)))')
		assert 	!evaluate('(lambda? 1)')
		assert 	!evaluate('(lambda? (quote hello))')
	}
	
	def void testSet() {
		evaluate('(define x 10)')
		assert 10 == evaluate('x')
		
		evaluate('(set! x 20)')
		assert 20 == evaluate('x')

		shouldFail(IllegalAccessException) {		
			evaluate('(set! y 20)')
		}
	}
	
	def void testLambda() {
		assert [4, 3, 2, 1] == evaluate('(cons ((lambda (x) (* x 2)) 2) (list 3 2 1))')
		
		evaluate('(define sumThis (lambda (x y) (+ x y)))')
		assert 5 == evaluate('(sumThis 2 3)')
		
		evaluate('(define circle-area (lambda (r) (* 3.14 (* r r))))')
		assert 28.26 == evaluate('(circle-area 3)')
		
		evaluate('(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))')
		assert 120 == evaluate('(fact 5)')
		
		evaluate('(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))')
		assert 8 == evaluate('(fib 5)')	
	}
		
}
