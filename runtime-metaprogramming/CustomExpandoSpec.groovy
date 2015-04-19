@Grapes(
	@Grab(group='org.spockframework', module='spock-core', version='1.0-groovy-2.4')
)
import spock.lang.Specification

class CustomExpandoSpec extends Specification {

	void 'test property access'() {
		when:
		def ex = new CustomExpando()
		ex.name = 'Helton'

		then:
		ex.name == 'Helton'
	}

}