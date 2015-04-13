@Grapes(
	@Grab(group='joda-time', module='joda-time', version='2.7')
)
import org.joda.time.format.DateTimeFormat

class DateTimeCategory {
	def static String createPrintableTime(dateTime, pattern) {
		def format = DateTimeFormat.forPattern(pattern)
		dateTime.toString format
	}
}