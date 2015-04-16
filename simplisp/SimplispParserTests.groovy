import groovy.util.GroovyTestCase

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

	def void testShouldParseAnIntegerAsGroovyInteger() {
		def result = parser.parse('123')
		assert 123 == result
		assert java.lang.Integer == result.class
	}

	def void testShouldParseAFloatingPointAsGroovyBigDecimal() {
		def result = parser.parse('123.456')
		assert 123.456 == result
		assert java.math.BigDecimal == result.class
	}

	def void testShouldParseASymbolAsGroovyString() {
		def result = parser.parse('test')
		assert 'test' == result
		assert java.lang.String == result.class
	}	
}
