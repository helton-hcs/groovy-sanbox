package simplisp.environment

import groovy.transform.Canonical
import simplisp.types.*

@Canonical
class Environment {
	def private Environment outerEnvironment
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
				throw new IllegalAccessException("$name not found")
			}
		}
	}

	def static Environment getStandardEnvironment() {
		def environment = new Environment()

		environment.add '+', { args ->
			args.inject { n1, n2 -> n1 + n2 }
		}
		environment.add '-', { args ->
			args.inject { n1, n2 -> n1 - n2 }
		}		
		environment.add '*', { args ->
			args.inject 1, { n1, n2 -> n1 * n2 }
		}
		environment.add '/', { args ->
			if (args.size() == 1) {
				1 / args.first()
			}
			else {
				args.inject { n1, n2 -> n1 / n2 }
			}
		}
		environment.add '>', { args ->
			args.groupInPairs().collect { 
				it.first() > it.last() 
			}.every { it }
		}
		environment.add '<', { args ->
			args.groupInPairs().collect {
				it.first() < it.last()
			}.every { it }
		}
		environment.add '>=', { args ->
			args.groupInPairs().collect { 
				it.first() >= it.last() 
			}.every { it }
		}
		environment.add '<=', { args ->
			args.groupInPairs().collect {
				it.first() <= it.last()
			}.every { it }
		}
		environment.add '=', { args ->
			args.groupInPairs().collect { 
				it.first() == it.last() 
			}.every { it }
		}
		environment.add 'abs', { args ->
			if (args.size() != 1) {
				throw new IllegalArgumentException("ABS accepts 1 argument")
			}
			args.first().abs()
		}
		environment.add 'append', environment.find('+')
		environment.add 'car', { args ->
			args.first()
		}
		environment.add 'cdr', { args ->
			args.tail()
		}
		environment.add 'cons', { args ->
			if (args.size() != 2) {
				throw new IllegalArgumentException("CONS accepts 2 arguments")
			}
			[args.first()] + args.last()
		}
		environment.add 'list?', { args ->
			if (args.size() != 1) {
				throw new IllegalArgumentException("LIST? accepts 1 argument")
			}
			args.first() instanceof List
		}
		environment.add 'number?', { args ->
			println "args = $args"
			if (args.size() != 1) {
				throw new IllegalArgumentException("NUMBER? accepts 1 argument")
			}
			(args.first() instanceof Integer) || (args.first() instanceof BigDecimal)
		}
		environment.add 'symbol?', { args ->
			if (args.size() != 1) {
				throw new IllegalArgumentException("SYMBOL? accepts 1 argument")
			}
			args.first() instanceof String
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