@Grapes(
	@Grab(group='joda-time', module='joda-time', version='2.7')
)
import org.joda.time.DateTime

class DateParser {
	def String parse(time) {
		if (!time)
			throw new IllegalArgumentException()
		use (DateTimeCategory) {
			new DateTime(time).createPrintableTime('MM/dd/yyyy - hh:mm aa')
		}
	}
}