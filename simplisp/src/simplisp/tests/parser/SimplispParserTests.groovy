package simplisp.tests.parser

import groovy.util.GroovyTestCase
import simplisp.parser.*
import simplisp.types.*

class SimplispParserTests extends GroovyTestCase {
	def parser

	def void setUp() {
		parser = new SimplispParser()
	}

	def void testShouldTokenizeCorrect() {
		def tokens = parser.tokenize("(begin (define r 10) (* pi (* r r)))")
		assert ['(', 'begin', '(', 'define', 'r', '10', ')', '(', '*', 'pi', '(', '*', 'r', 'r', ')', ')', ')'] == tokens
		
		tokens = parser.tokenize("(if (1 <= 2) (quote true) (quote false))")
		assert ['(', 'if', '(', '1', '<=', '2', ')', '(', 'quote', 'true', ')', '(', 'quote', 'false', ')', ')'] == tokens 
	}

	def void testShouldFailOnParsingWhenThereIsNoTokensAvailable() {
		shouldFail(SyntaxError) {
			parser.parse("")
		}
		shouldFail(SyntaxError) {
			parser.parse("  ")
		}		
		shouldFail(SyntaxError) {
			parser.parse("		")
		}		
		shouldFail(SyntaxError) {
			parser.parse("""

			""")
		}		
	}

	def void testShouldFailOnParseWithUnbalancedParenthesis() {
		shouldFail(SyntaxError) {
			parser.parse("(define x")
		}
		shouldFail(SyntaxError) {
			parser.parse("(define x (+ 4 5)")
		}
		shouldFail(SyntaxError) {
			parser.parse(')')
		}
	}

	def void testShouldParseAnInteger() {
		def result = parser.parse('123')
		assert 123 == result.value
		assert Integer.class == result.class
	}

	def void testShouldParseAFloatingPoint() {
		def result = parser.parse('123.456')
		assert 123.456 == result
		assert BigDecimal.class == result.class
	}

	def void testShouldParseASymbol() {
		def result = parser.parse('test')
		assert 'test' == result
		assert String.class == result.class
	}	
	
	def void testShouldParseAList() {
		def result = parser.parse('(sum 1 2.3)')
		assert ['sum', 1, 2.3] == result
		assert ArrayList.class == result.class
	}
	
	def void testShouldParseNestedLists() {
		def result = parser.parse('(if (> 1 2) (quote true) (quote false))')
		assert ['if', ['>', 1, 2], ['quote', 'true'], ['quote', 'false']] == result
		assert ArrayList.class == result.class
	}
	
	def void testShouldParseABoolean() {
		def result = parser.parse('#t')
		assert result
		assert Boolean.class == result.class

		result = parser.parse('#f')
		assert !result
		assert Boolean.class == result.class
	}
}
