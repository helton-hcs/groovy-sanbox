package simplisp.tests.environment

import groovy.util.GroovyTestCase
import simplisp.environment.Environment
import simplisp.types.*

class EnvironmentTests extends GroovyTestCase {

	def void testAddAndFindShouldWorkOnEnvironment() {
		def environment = new Environment()
		environment.add 'x', 1000
		assert 1000 == environment.find('x')
		environment.add 'sumThis', { a, b -> a + b }
		assert 3 == environment.find('sumThis')(1, 2)
	}
	
	def void testFindShouldWorkOnEnvironmentWithOuter() {
		def outerEnvironment = new Environment()
		outerEnvironment.add 'x', 1000
		outerEnvironment.add 'sumThis', { a, b -> a + b }
		def environment = new Environment(outerEnvironment)
		assert 1000 == environment.find('x')
		assert 3 == environment.find('sumThis')(1, 2)
	}

	def void testFindShouldThrowExceptionOnEnvironmentWhenNameWasNotFound() {
		def environment = new Environment()
		shouldFail(IllegalAccessException) {
			environment.find('not_exists')
		}
	}
	
	def void testStandardEnvironmentShouldReturnAValidEnvironment() {
		def environment = Environment.getStandardEnvironment()
		assert environment
		assert Environment.class == environment.class
	}

	def void testStandardEnvironmentShouldContainPlusMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 1  == environment.find('+')([1])
		assert 3  == environment.find('+')([1, 2])
		assert 15 == environment.find('+')([1, 2, 3, 4, 5])
	}

	def void testStandardEnvironmentShouldContainMinusMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 1   == environment.find('-')([1])
		assert -1  == environment.find('-')([1, 2])
		assert -13 == environment.find('-')([1, 2, 3, 4, 5])
	}

	def void testStandardEnvironmentShouldContainMultiplyMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 1   == environment.find('*')([1])
		assert 2   == environment.find('*')([1, 2])
		assert 120 == environment.find('*')([1, 2, 3, 4, 5])
	}

	def void testStandardEnvironmentShouldContainDivideMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 0.1            == environment.find('/')([10])
		assert 0.5            == environment.find('/')([1, 2])
		assert 0.008333333335 == environment.find('/')([1, 2, 3, 4, 5])
	}	

	def void testStandardEnvironmentShouldContainGreaterThanMethod() {
		def environment = Environment.getStandardEnvironment()
		assert  environment.find('>')([1])
		assert !environment.find('>')([1, 2])
		assert  environment.find('>')([2, 1])
		assert !environment.find('>')([1, 1])
		assert !environment.find('>')([1, 2, 3, 4, 5])
		assert !environment.find('>')([1, 2, 3, 4, 4])
		assert  environment.find('>')([5, 4, 3, 2, 1])
		assert !environment.find('>')([5, 4, 3, 1, 1])
	}
	
	def void testStandardEnvironmentShouldContainLessThanMethod() {
		def environment = Environment.getStandardEnvironment()
		assert  environment.find('<')([1])
		assert  environment.find('<')([1, 2])
		assert !environment.find('<')([2, 1])
		assert !environment.find('<')([1, 1])
		assert  environment.find('<')([1, 2, 3, 4, 5])
		assert !environment.find('<')([1, 2, 3, 4, 4])
		assert !environment.find('<')([5, 4, 3, 2, 1])
		assert !environment.find('<')([5, 4, 3, 1, 1])
	}
	
	def void testStandardEnvironmentShouldContainGreaterOrEqualThanMethod() {
		def environment = Environment.getStandardEnvironment()
		assert  environment.find('>=')([1])
		assert !environment.find('>=')([1, 2])
		assert  environment.find('>=')([2, 1])
		assert  environment.find('>=')([1, 1])
		assert !environment.find('>=')([1, 2, 3, 4, 5])
		assert !environment.find('>=')([1, 2, 3, 4, 4])
		assert  environment.find('>=')([5, 4, 3, 2, 1])
		assert  environment.find('>=')([5, 4, 3, 1, 1])
	}
	
	def void testStandardEnvironmentShouldContainLessOrEqualThanMethod() {
		def environment = Environment.getStandardEnvironment()
		assert  environment.find('<=')([1])
		assert  environment.find('<=')([1, 2])
		assert !environment.find('<=')([2, 1])
		assert  environment.find('<=')([1, 1])
		assert  environment.find('<=')([1, 2, 3, 4, 5])
		assert  environment.find('<=')([1, 2, 3, 4, 4])
		assert !environment.find('<=')([5, 4, 3, 2, 1])
		assert !environment.find('<=')([5, 4, 3, 1, 1])
	}
	
	def void testStandardEnvironmentShouldContainEqualsMethod() {
		def environment = Environment.getStandardEnvironment()
		assert  environment.find('=')([1])
		assert !environment.find('=')([1, 2])
		assert  environment.find('=')([1, 1])
		assert !environment.find('=')([1, 2, 3, 4, 5])
		assert !environment.find('=')([1, 1, 1, 1, 5])
		assert !environment.find('=')([1, 1, 1, 4, 1])
	}
	
	def void testStandardEnvironmentShouldContainAbsMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 1 == environment.find('abs')([1])
		assert 1 == environment.find('abs')([-1])
		shouldFail(IllegalArgumentException) {
			environment.find('abs')([])
		}
		shouldFail(IllegalArgumentException) {
			environment.find('abs')([1, 2])
		}
	}
	
	def void testStandardEnvironmentShouldContainAppendMethod() {
		def environment = Environment.getStandardEnvironment()
		assert environment.find('append') == environment.find('+')
	}
	
	def void testStandardEnvironmentShouldContainCARMethod() {
		def environment = Environment.getStandardEnvironment()
		assert 1 == environment.find('car')([1])
		assert 1 == environment.find('car')([1, 2])
		assert 1 == environment.find('car')([1, 2, 3, 4, 5])
		assert 5 == environment.find('car')([5, 4, 3, 2, 1])
	}
	
	def void testStandardEnvironmentShouldContainCDRMethod() {
		def environment = Environment.getStandardEnvironment()
		assert []           == environment.find('cdr')([1])
		assert [2]          == environment.find('cdr')([1, 2])
		assert [2, 3, 4, 5] == environment.find('cdr')([1, 2, 3, 4, 5])
		assert [4, 3, 2, 1] == environment.find('cdr')([5, 4, 3, 2, 1])
	}
	
	def void testStandardEnvironmentShouldContainCONSMethod() {
		def environment = Environment.getStandardEnvironment()
		assert [1, 2]          == environment.find('cons')([1, [2]])
		assert [1, 2, 3, 4]    == environment.find('cons')([1, [2, 3, 4]])
		assert ['a', 'b', 'c'] == environment.find('cons')(['a', ['b', 'c']])
		shouldFail(IllegalArgumentException) {
			environment.find('cons')([])
		}
		shouldFail(IllegalArgumentException) {
			environment.find('cons')([1])
		}
	}

	def void testStandardEnvironmentShouldContainList_Method() {
		def environment = Environment.getStandardEnvironment()
		assert environment.find('list?')([new SimplispList([new SimplispSymbol('sum'), new SimplispInteger(1), new SimplispFloat(2.3)])])
		shouldFail(IllegalArgumentException) {
			environment.find('list?')([])
		}
		shouldFail(IllegalArgumentException) {
			environment.find('list?')([1, 2])
		}
	}
	
	def void testStandardEnvironmentShouldContainNumber_Method() {
		def environment = Environment.getStandardEnvironment()
		assert environment.find('number?')([new SimplispInteger(1)])
		assert environment.find('number?')([new SimplispFloat(2.3)])
		shouldFail(IllegalArgumentException) {
			environment.find('number?')([])
		}
		shouldFail(IllegalArgumentException) {
			environment.find('number?')([1, 2])
		}
	}
	
	def void testStandardEnvironmentShouldContainSymbol_Method() {
		def environment = Environment.getStandardEnvironment()
		assert environment.find('symbol?')([new SimplispSymbol('test')])
		shouldFail(IllegalArgumentException) {
			environment.find('symbol?')([])
		}
		shouldFail(IllegalArgumentException) {
			environment.find('symbol?')([1, 2])
		}
	}
}