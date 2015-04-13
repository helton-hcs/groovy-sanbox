//pesadelo Java pré 7
BigDecimal deltaJava(BigDecimal a, BigDecimal b, BigDecimal c) {
  return b.multiply(b).minus(new BigDecimal(4).multiply(a).multiply(c))
}

println deltaJava(new BigDecimal(2), new BigDecimal(3), new BigDecimal(4))

//Groovy
def deltaGroovy(a, b, c) {
  (b*b) - (4*a*c)
}

println deltaGroovy(2, 3, 4)

String printMe =  "hello world"
def c = { parameter -> println(printMe + parameter) }
c(" from Groovy!") 