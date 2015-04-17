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
			parser.parse(')')
		}
	}

	def void testShouldParseAnInteger() {
		def result = parser.parse('123')
		assert 123 == result.value
		assert SimplispInteger.class == result.class
	}

	def void testShouldParseAFloatingPoint() {
		def result = parser.parse('123.456')
		assert 123.456 == result.value
		assert SimplispFloat.class == result.class
	}

	def void testShouldParseASymbol() {
		def result = parser.parse('test')
		assert 'test' == result.value
		assert SimplispSymbol.class == result.class
	}	
	
	def void testShouldParseAList() {
		def result = parser.parse('(sum 1 2.3)')
		assert [new SimplispSymbol('sum'), 
			     new SimplispInteger(1), 
				 new SimplispFloat(2.3)] == result.value
		assert SimplispList.class == result.class
	}
}
