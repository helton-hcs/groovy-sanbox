package simplisp.environment

import groovy.transform.Canonical
import simplisp.types.*

@Canonical
class Environment {
	def Environment outerEnvironment
	def private map = [:]

	def add(name, value) {
		map."$name" = value
	}

	def find(name) {
		if (map.containsKey(name)) {
			map."$name"
		}
		else {
			if (outerEnvironment) {
				outerEnvironment.find name
			}
			else {
				throw new IllegalAccessException("The referenced symbol '$name' was not found.")
			}
		}
	}

	def static Environment getStandardEnvironment() {
		def environment = new Environment()

		environment.add '+', { Object ... args ->
			args.inject { n1, n2 -> n1 + n2 }
		}
		environment.add '-', { Object ... args ->
			args.inject { n1, n2 -> n1 - n2 }
		}		
		environment.add '*', { Object ... args ->
			args.inject 1, { n1, n2 -> n1 * n2 }
		}
		environment.add '/', { Object ... args ->
			if (args.size() == 1) {
				1 / args.first()
			}
			else {
				args.inject { n1, n2 -> n1 / n2 }
			}
		}
		environment.add '>', { Object ... args ->
			(args as ArrayList).groupInPairs().collect { 
				it.first() > it.last() 
			}.every { it }
		}
		environment.add '<', { Object ... args ->
			(args as ArrayList).groupInPairs().collect {
				it.first() < it.last()
			}.every { it }
		}
		environment.add '>=', { Object ... args ->
			(args as ArrayList).groupInPairs().collect { 
				it.first() >= it.last() 
			}.every { it }
		}
		environment.add '<=', { Object ... args ->
			(args as ArrayList).groupInPairs().collect {
				it.first() <= it.last()
			}.every { it }
		}
		environment.add '=', { Object ... args ->
			(args as ArrayList).groupInPairs().collect { 
				it.first() == it.last() 
			}.every { it }
		}
		environment.add 'abs', { it ->
			it.abs()
		}
		environment.add 'append', environment.find('+')
		environment.add 'car', {
			it.first()
		}
		environment.add 'cdr', { it ->
			it.tail()
		}
		environment.add 'cons', { item, list ->
			[item] + list
		}
		environment.add 'list', { Object ... args ->
			args as ArrayList
		}
		environment.add 'min', { Object ... args ->
			(args as ArrayList).min()
		}
		environment.add 'max', { Object ... args ->
			(args as ArrayList).max()
		}
		environment.add 'not', {
			!it
		}
		environment.add 'length', { 
			it.size()
		}
		
		//predicates
		environment.add 'list?', {
			it instanceof ArrayList
		}
		environment.add 'number?', {
			(it instanceof Integer) || (it instanceof BigDecimal)
		}
		environment.add 'symbol?', {
			it instanceof String
		}
		environment.add 'null?', {
			it == null || it.isEmpty()
		}
		environment.add 'boolean?', {
			it instanceof Boolean
		}
/*
          '+':op.add, 
          '-':op.sub, 
          '*':op.mul, 
          '/':op.div, 
          '>':op.gt, 
          '<':op.lt, 
          '>=':op.ge, 
          '<=':op.le, 
          '=':op.eq, 
          'abs':     abs,
          'append':  op.add,  
        'apply':   apply,
        'begin':   lambda *x: x[-1],
          'boolean?'
          'car':     lambda x: x[0],
          'cdr':     lambda x: x[1:], 
          'cons':    lambda x,y: [x] + y,
        'eq?':     op.is_, 
        'equal?':  op.eq, 
          'length':  len, 
          'list':    lambda *x: list(x), 
          'list?':   lambda x: isinstance(x,list), 
        'map':     map,
          'max':     max,
          'min':     min,
          'not':     op.not_,
          'null?':   lambda x: x == [], 
          'number?': lambda x: isinstance(x, Number),   
        'procedure?': callable,
        'round':   round,
          'symbol?': lambda x: isinstance(x, Symbol),
*/
		environment
	}

}