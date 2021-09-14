Feature: Test case 'loanapproval-process'

    Scenario: LoanApproved
    		Given a customer approached for loan
    		When a customer approached for loan with the following data
        # DataEntry - Start
      	And the human task 'Data Entry' is claimable by 'rhpamAdmin'
      	And the human task 'Data Entry' is Completed by 'rhpamAdmin' with parameters
          	|firstName   |lastName   |DOB   			|CreditScore   |income 		 |
          	|John				 |Finn		   |11/11/2011  |0.1			     |50000 		 |
        Then the Loan Approval status is
       	And the human task 'Approval Documentation' is claimable by 'rhpamAdmin'
        And the human task 'Approval Documentation' is Completed by 'rhpamAdmin' with parameters
        		|ApprovalDocument  |
            |true	     				 |
            
	    Scenario: LoanRejected
    		Given a customer approached for loan
    		When a customer approached for loan with the following data
        # DataEntry - Start
      	And the human task 'Data Entry' is claimable by 'rhpamAdmin'
      	And the human task 'Data Entry' is Completed by 'rhpamAdmin' with parameters
          	|firstName   |lastName   |DOB   			|CreditScore   |income 		 |
          	|John				 |Finn		   |11/11/2011  |0.6			     |50000 		 |
        Then the Loan Approval status is
       	And the human task 'Reject Documentation' is claimable by 'rhpamAdmin'
        And the human task 'Reject Documentation' is Completed by 'rhpamAdmin' with parameters
        		|RejectedDocument  |
            |true	     				 |        