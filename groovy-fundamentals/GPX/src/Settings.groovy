class Settings {
	static final forecastApiURL = 'https://api.forecast.io/'
	static final mySqlDriver = 'com.mysql.jdbc.Driver'
	static final databaseStringConnection = 'jdbc:mysql://localhost/gps'

	def static getCredentials() {
		def credentialsFile = new File('credentials.groovy')
		def configSlurper = new ConfigSlurper()
		configSlurper.parse(credentialsFile.toURL())		
	}
}