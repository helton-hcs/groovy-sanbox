package simplisp.evaluator

import simplisp.types.*

class SimplispEvaluator {
	static def evaluate(expression, environment) {
		println "$expression"
		switch (expression) {
			case { it instanceof SimplispSymbol }:
				return environment.find(expression.value)
			case { !(it instanceof SimplispList) }:
				return expression.value
			case { it.value.first()== 'quote' }:
				return new SimplispList(expression.value.tail())
			case { it.value.first() == 'define' }:
				def (_, name, assignedExpression) = expression.value
				return environment.add(name, evaluate(assignedExpression))
			default:
				def procedure = evaluate(expression.value.first(), environment)
				def args = expression.value.tail().collect {
					evaluate(it, environment)
				}
				procedure(args)
/*
    else:                          # (proc arg...)
        proc = eval(x[0], env)
        args = [eval(exp, env) for exp in x[1:]]
        return proc(*args) 
*/			
		}
	}
}
