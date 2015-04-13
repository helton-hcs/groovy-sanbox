/*
//Groovy Generics Gotchas
//  Groovy don't enforce Java Generics, but is syntax compatible
//  Warning: it won't work using static compilation
List<Integer> nums = [3, 1, 4, 1, 5, 9, 'abc', new Date()]
nums << 3.5
nums.each { println it.class.name }

//Map accessors
def map = [a:1, b:2, c:2]
map.d =3
map['e'] = 1
map.put('f', 6)
println map
//println map.class() //raise an NullPointerException because .class is trying to access a key called "class"
println map.getClass().name

//Groovy Operator Overloading
println(2.0d - 1.1d) //wtf? and the precision?
println(2.0 - 1.1) //ok, this is fine
def string = 'this is a string'
println string[0]
println string[1..3]
println string[-1..0]
println string[0,2,6]

//Collections
def strings_array = 'this is a list of strings'.split() //split return a string Array [Ljava.lang.String
println strings_array
println strings_array.class.name

List strings_list = 'this is a list of strings'.split() //automagically converted Array into List!
println strings_list
println strings_list.class.name

//Old style, like Java
Collections.sort(strings_list, new Comparator<String>(){
		int compare(String s1, String s2) {
			s2.size() <=> s1.size() //comparator using "<=>" operator, called "spaceship opertor", what calls "compareTo" method
		}
	})
println strings_list

//Old versions of Groovy don't have closure coercion
Collections.sort(strings_list,
	{ s1, s2 -> s1.size() <=> s2.size() } as Comparator)
println strings_list

//Current version of Groovy with closure coercion
Collections.sort(strings_list,
	{ s1, s2 -> s2.size() <=> s1.size() })
println strings_list

//Groovy make static methods as instance methods - quite easy, isn't it?
println Math.abs(-3)
println ((-3).abs())

strings_list.sort { s1, s2 -> s1.size() <=> s2.size() }
println strings_list

strings_list.sort { -it.size() }
println strings_list
*/

//Runtime Metaprogramming
Expando e = new Expando()
e.name = 'Kitty'
e.speak = { -> "$name says meow" } //ensure that closure don't have any parameter, even "it", that it's used by default
e.speakThis = { msg -> "$name says \"$msg\""}
println e.speak()
println e.speakThis('Meow')

class Dog  {}

Dog.metaClass.name = 'Fido'
Dog.metaClass.speak = { -> "$name says ruf" }

Dog fido = new Dog()
println fido.speak()