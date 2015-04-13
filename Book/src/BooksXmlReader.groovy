class Author {
	String name
	String surname

	String toString() {
		"$surname, $name"
	}
}

class Book {
	String id
	Author author
	String title
	String genre
	Double price
	Date publishDate	
	String description

	private static Book fromXmlElement(book) {
		def (surname, name) = book.author.toString().split(',')
		new Book (
			id:book.@id, 
			author:new Author (
				name:name.trim(), 
				surname:surname.trim()
			),
			title:book.title,
			genre:book.genre,
			price:Double.valueOf(book.price.toString()),
			publishDate:Date.parse('yyyy-MM-dd', book.publish_date.toString()),
			description:book.description.toString().replace("\n      ", '').trim()
		)
	}

	public static List fromXmlFile(fileName) {
		new XmlSlurper().parse(new File(fileName)).book.collect { fromXmlElement(it) }
	}
}

Book.fromXmlFile('../data/books.xml').each {
	println "Id    = ${it.id}"
	println "Title = ${it.title}"
	println "Genre = ${it.genre}"
	println "Price = US\$ ${it.price}"
	println "=" * 50
}