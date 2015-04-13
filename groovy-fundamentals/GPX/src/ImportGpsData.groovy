@GrabConfig(systemClassLoader=true)
@Grab('mysql:mysql-connector-java:5.1.35')
@Grab('joda-time:joda-time:2.7')
import groovy.sql.Sql
import org.joda.time.DateTime

def file = new File('../data/fells_loop.gpx')

def slurper = new XmlSlurper()
def gpx = slurper.parse(file)

/*gpx.with {
	println "$name $desc"
	println attributes()['version'] //instead of println gpx.@version
	println attributes()['creator'] //instead of println gpx.@creator
}*/

def forecastApi = new ForecastApi()

def sql = Sql.newInstance(
	Settings.databaseStringConnection, 
	Settings.getCredentials().database.user, 
	Settings.getCredentials().database.password,
	Settings.mySqlDriver
)

gpx.rte.rtept.each {
	def row = sql.firstRow("""
 		                   select * 
 		                     from routepoints 
 	                        where latitude  = ${it.@lat.toDouble()} 
	                          and longitude = ${it.@lon.toDouble()}
	                          and timestep  = ${new DateTime(it.time.toString()).toDate()}
		                   """)
	if (!row) {
		def response = forecastApi.getResponse(it.@lat, it.@lon, it.time)
		def routepoints = sql.dataSet("routepoints")
		routepoints.add(
			latitude: it.@lat.toDouble(),
			longitude: it.@lon.toDouble(),
			timestep: new DateTime(it.time.toString()).toDate(),
			temperature: response.data.currently.temperature)
	}
}

sql.eachRow('select * from routepoints') {
	println "Latitude: ${it.latitude}, Longitude: ${it.longitude}, " + 
	        "Timestep: ${it.timestep}, Temperature: ${it.temperature}"
}

sql.close()