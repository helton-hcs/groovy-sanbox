@Grapes([
	@Grab(
		group='org.codehaus.groovy.modules.http-builder',
		module='http-builder',
		version='0.7.1' )	
	])
import groovyx.net.http.RESTClient

class ForecastApi {
	private def forecastApi
	private def apiKey

	ForecastApi() {
		this.apiKey = Settings.getCredentials().apiKey
		this.forecastApi = new RESTClient(Settings.forecastApiURL)
	}

	def getResponse(latitude, longitude, time) {
		forecastApi.get(path:"forecast/$apiKey/$latitude,$longitude,$time")
	}
}