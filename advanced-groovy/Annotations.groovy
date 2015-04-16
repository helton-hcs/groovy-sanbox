import groovy.transform.*

@ToString
@EqualsAndHashCode
@Immutable
class User {
	String name
	int age
}

def user1 = new User(name: "Helton", age:23)
def user2 = new User(name: "Helton", age:23)
println user1
//user1.name = "trying change name" //raise an error because it's immutable!
assert user1 == user2

//////////////////////////////////////////////////////////////////
//Using AnnotationCollector to grouping annotatinos together
//////////////////////////////////////////////////////////////////
@AnnotationCollector([ToString, EqualsAndHashCode, Immutable])
@interface GroupedAnnotations {}

@GroupedAnnotations
class Person {
	String name
	int age
}

def person1 = new Person(name: "Helton", age:23)
def person2 = new Person(name: "Helton", age:23)
println person1
//person1.name = "trying change name" //raise an error because it's immutable!
assert person1 == person2

@Singleton
class Singleton {

}

def mfs = new MyFakeSingleton()