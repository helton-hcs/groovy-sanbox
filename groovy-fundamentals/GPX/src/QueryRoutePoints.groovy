@GrabConfig(systemClassLoader=true)
@Grab('mysql:mysql-connector-java:5.1.35')
import groovy.sql.Sql

def sql = Sql.newInstance(
	Settings.databaseStringConnection, 
	Settings.getCredentials().database.user, 
	Settings.getCredentials().database.password,
	Settings.mySqlDriver
)

def row = sql.firstRow('select latitude, longitude from routepoints')
println "Latitude: ${row.latitude}, Longitude: ${row.longitude}"

/*sql.eachRow('select * from routepoints where temperature < 50') {
	println "Latitude: ${it.latitude}, Longitude: ${it.longitude}, " + 
	"Timestep: ${it.timestep}, Temperature: ${it.temperature}"
}*/

def newTemperature = 100
sql.executeUpdate "update routepoints set temperature = $newTemperature"

sql.close();
