package simplisp.evaluator

import simplisp.environment.SimplispEnvironment
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
			case { it.first() == 'set!' }:
				def (_, name, assignedExpression) = expression
				return environment.update(name, evaluate(assignedExpression, environment))
			case { it.first() == 'lambda' }:
				def (_, parameters, body) = expression
				def lambda = { Object ... args ->
					def lambdaEnvironment = new SimplispEnvironment(environment)
					[parameters, args].transpose().each { key, value ->
						lambdaEnvironment.add key, value
					}
					evaluate(body, lambdaEnvironment)			
				}
				return lambda
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
