class BankAccount {
	private balance

	BankAccount(openingBalance) {
		balance = openingBalance
	}

	def void deposit(amount) {
		balance += amount
	}

	def withdraw(amount) {
		if (amount > balance)
			throw new InsufficientFundsException()
		balance -= amount
	}

	def void accrueInterest() {
		def service = new InterestRateService()
		def rate = service.getInterestRate()
		//def rate = 0.10 //when test with StubFor it won't complain about. To make sure that the method on InterestRate is called, use MockFor

		// example: 50 = 500 * 0.10
		def accruedInterest = balance * rate

		deposit(accruedInterest)
	}
}