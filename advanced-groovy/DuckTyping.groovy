class Duck {
	String name

	def void walk() {
		println "® $name is walking..."
	}

	def void talk() {
		println "® $name is talking..."
	}

}

def actLikeADuck(duck) {
	duck.walk()
	duck.talk()
}

actLikeADuck new Duck(name:"Dudu")

def fakeDuck = new Expando()
fakeDuck.name = 'FakeDuck'
fakeDuck.walk = { println "$name is walking..." }
fakeDuck.talk = { println "$name is talking..." }
actLikeADuck(fakeDuck)