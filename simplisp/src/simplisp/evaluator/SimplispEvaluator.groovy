package simplisp.evaluator

import simplisp.types.*

class SimplispEvaluator {
		
	static def evaluate(expression, environment) {	
		switch (expression) {
			case { it instanceof String }:
				return environment.find(expression)
			case { it instanceof Integer || it instanceof BigDecimal }:
				return expression
			case { it instanceof Boolean }:
				return expression
			case { it.first() == 'quote' }:
				return expression.tail().first()
			case { it.first() == 'define' }:
				def (_, name, assignedExpression) = expression
				return environment.add(name, evaluate(assignedExpression, environment))
			case { it.first() == 'if' }:
				def (_, test, consequence, alternative) = expression
				if (evaluate(test, environment)) {
					return evaluate(consequence, environment)
				}
				return evaluate(alternative, environment)
			default:
				def procedure = evaluate(expression.first(), environment)
				def args = expression.tail()
				def evaluatedArgs = args.collect {
					evaluate(it, environment)
				}
				return procedure(*evaluatedArgs)
		}
	}
}
