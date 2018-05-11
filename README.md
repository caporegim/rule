Microservice for transaction check by rules stored in database (H2). Based on Spring Boot.

There is a system that processes financial transactions. It is necessary to write a microservice that defines the rule by which the transaction should be processed.


Each transaction is a set of parameters:
  Transaction type (type code from the directory)
  Transaction direction (original / cancellation)
  Transaction source (system code from the directory)
  Transaction Amount
  Transaction currency
  Transaction date

Each rule consists of the conditions for the transaction parameters:
  Type (equal)
  Direction (equal)
  Source (equal)
  Amount (more / less / equal)
And the action you need to perform:
  Conduct / Reject
  Balance type code
All the rules have a validity period (start date, end date)

When choosing a rule for a transaction, the following criteria are used:
The rule is valid at the time of the transaction.
The transaction satisfies all the conditions on the rule (if any of the conditions is not specified, then the transaction is assumed to satisfy it).
If the transaction satisfies several rules, then the rule is chosen, which has more conditions.
If the transaction satisfies several rules with the same number of conditions, then the rule that was created later is selected.

The service response must contain the action specified by the rule and the unique identifier of the rule.

Non-functional requirements:
- Rules are stored in DBMS
- UI for rule management is not needed
