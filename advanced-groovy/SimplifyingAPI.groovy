//Complex like BigDecimal in Java!
class ThirdPartyComplexAPI {
	private static final FACTOR = Math.PI
	def value

	def add(ThirdPartyComplexAPI complex) {
		new ThirdPartyComplexAPI(value:((this.value + complex.value) * FACTOR))
	}

	def subtract(ThirdPartyComplexAPI complex) {
		new ThirdPartyComplexAPI(value:((this.value - complex.value) * FACTOR))
	}

	def multiply(ThirdPartyComplexAPI complex) {
		new ThirdPartyComplexAPI(value:((this.value * complex.value) * FACTOR))
	}

	def divide(ThirdPartyComplexAPI complex) {
		new ThirdPartyComplexAPI(value:((this.value / complex.value) * FACTOR))
	}

}

//Grooving it! Operator Overloading
ThirdPartyComplexAPI.metaClass.plus  = { delegate.add      it }
ThirdPartyComplexAPI.metaClass.minus = { delegate.subtract it }
ThirdPartyComplexAPI.metaClass.mul   = { delegate.multiply it }
ThirdPartyComplexAPI.metaClass.div   = { delegate.divide   it }

def c1 = new ThirdPartyComplexAPI(value:5)
def c2 = new ThirdPartyComplexAPI(value:3)

println(c1.add(c2).value)
println((c1 + c2).value)

println(c1.subtract(c2).value)
println((c1 - c2).value)

println(c1.multiply(c2).value)
println((c1 * c2).value)

println((c1 / c2).value)
println(c1.divide(c2).value)