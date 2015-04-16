def executeOnGroovyShell(command) {
	println new GroovyShell(this.class.classLoader).evaluate(command)	
}

executeOnGroovyShell '1 + 2'
executeOnGroovyShell 'println "Hello, World!"'