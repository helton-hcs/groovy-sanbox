package groovy.runtime.metaclass.java.util

import groovy.transform.InheritConstructors

@InheritConstructors
class ArrayListMetaClass extends DelegatingMetaClass {
	
	Object invokeMethod(Object object, String method, Object[] arguments) {
		switch (method) {
			case 'collectWithIndex':
				def closure = arguments.first()
				return [object, 0..object.size()].transpose().collect(closure)
			case 'groupInPairs':
				return object.collectWithIndex { item, i ->
					[object[i - 1], item]
				}.tail()
			default:
				super.invokeMethod object, method, arguments
		}
	}
}