class Person {
	String name
	String surname
	String toString() {
		"${name} ${surname}"
	}
	boolean equals(Object object) {
		object.name == name && object.surname == surname
	}
}

/************** Utils **************/
separatorSize = 100

List.metaClass.show = { message ->
	println "@List - ${message}"
	println "-" * separatorSize
	delegate.eachWithIndex { item, index ->
		println "[${index}] = ${item}"
	}
	println "=" * separatorSize
}

Map.metaClass.show = { message ->
	println "@Map - ${message}"
	println "-" * separatorSize
	delegate.each { key, value -> 
		println "${key} = ${value}"
	}
	println "=" * separatorSize
}

/************** Lists **************/
def people = [
	new Person(name:"Helton", surname:"Souza"),
	new Person(name:"John", surname:"Fox")
]
people.show("Showing list")

people += new Person(name:"Mary", surname:"Jane")
people.add(new Person(name:"Bill", surname:"Finn")) //old style!
people.show("Adding people")

people -= new Person(name:"Mary", surname:"Jane")
people.show("Removing person")

people.findAll {
	it.name.length() == 4
}.show("Finding people")

/************** Maps **************/
def peopleMap = [Helton:new Person(name:"Helton", surname:"Souza")
               , "Bill":new Person(name:"Bill", surname:"Finn")]
peopleMap.show("Showing map")

peopleMap.Mary = new Person(name:"Mary", surname:"Jane")
peopleMap["John"] = new Person(name:"John", surname:"Fox")
peopleMap.show("Adding people to map")

/************** Mocking with maps **************/
truePerson = new Person(name:"Helton", surname:"Souza")
falsePerson = [name:"Helton", surname:"Souza"]
falsePerson.sayHello = { println "Hello" }
falsePerson.sayHello()