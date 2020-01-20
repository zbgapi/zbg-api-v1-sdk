"""
Account data
"""
from zbg.utils import Utils
from zbg.model.common import ResultModel


class Account(ResultModel):
    """
    The current user information

    :member
        user_id:	    string	user ID
        login_name:	    string	login name
        phone:	        string	phone number
        email:	        string	email
        type:	        string	account type: main，sub，virtual
        certification:	boolean	Whether real name authentication has been conducted
        mobile_auth:	boolean	Whether or not the phone has been verified
        email_auth:	    boolean	Whether or not email validation has been performed
        google_auth:	boolean	Whether or not Google validation is performed
        safe_pwd_auth:	boolean	Whether a security password is set
        subs:	        list	List of sub-account ID
    """

    def __init__(self, **kwargs):
        self.user_id = ''
        self.login_name = ''
        self.phone = ''
        self.email = ''
        self.type = ''
        self.certification = False
        self.mobile_auth = False
        self.email_auth = False
        self.google_auth = False
        self.safe_pwd_auth = False
        self.subs = []

        super().__init__(**kwargs)


class Balance(ResultModel):
    """
    The balance of currency.
    :member
        currency:	string	currency name
        balance:	decimal	balance
        available:	decimal	available balance
        freeze: 	decimal	frozen(not available)
    """

    def __init__(self, **kwargs):
        super().__init__()

        self.currency = Utils.safe_string(kwargs, 'currency')
        self.balance = Utils.safe_float(kwargs, 'balance')
        self.available = Utils.safe_float(kwargs, 'available')
        self.freeze = Utils.safe_float(kwargs, 'freeze')


class DepositAddress(ResultModel):
    """
    The currency deposit address in zbg.

    :member
        is_memo:	    bool
        address:	string	Deposit address
        memo:	    string	Deposit address memo
    """

    def __init__(self, **kwargs):
        super().__init__()

        self.is_memo = kwargs['isMemo'] if 'isMemo' in kwargs else False
        self.address = Utils.safe_string(kwargs, 'address')
        self.memo = Utils.safe_string(kwargs, 'memo')


class HistoricalDeposit(ResultModel):
    """
    Deposit record

    :member
        deposit_id: 	string	deposit ID
        currency:   	string	currency
        amount:     	float	deposit amount
        address:    	string	The deposit source address
        tx_hash:    	string	The on-chain transaction hash
        confirm_times:	int	    confirm-times
        state:      	string	deposit status：[confirming, completed]
        type:       	string	The type of this deposit (see below for details)
                                blockchain:	Blockchain roll-in
                                system: 	system deposit
                                recharge:	fiat recharge
                                transfer:	Merchants transfer money to each other
        created_at: 	long	created time
        confirmed_at:	long	The time of entry is confirmed 6 times
    """

    def __init__(self, **kwargs):
        self.deposit_id = ''
        self.currency = ''
        self.address = ''
        self.tx_hash = ''
        self.confirm_times = 0
        self.state = ''
        self.type = ''
        self.created_at = 0
        self.confirmed_at = 0

        super().__init__(**kwargs)

        self.amount = Utils.safe_float(kwargs, 'amount')


class WithdrawAddress(ResultModel):
    """
    The currency deposit address in zbg.

    :member
        is_memo:	    bool
        address:	string	Deposit address
        memo:	    string	Deposit address memo
    """

    def __init__(self, **kwargs):
        self.id = ''
        self.currency = ''
        self.address = ''
        self.remark = ''

        super().__init__(**kwargs)


class HistoricalWithdraw(ResultModel):
    """
    Withdrawal record

    :member
        withdraw_id:	string	deposit ID
        currency:   	string	currency
        amount:     	decimal	withdrawal amount
        address:    	string	The withdraw source address
        tx_hash:    	string	The on-chain transaction hash
        state:      	string	The state of this transfer (see below for details)
                                reexamine:	    Under examination for withdraw validation
                                canceled:	    Withdraw canceled by user
                                pass:   	    Withdraw validation passed
                                reject: 	    Withdraw validation rejected
                                transferred:	On-chain transfer initiated
                                confirmed:  	On-chain transfer completed with one confirmation
        fee:        	decimal	withdraw fee
        created_at: 	long	The timestamp in milliseconds for the transfer creation
        audited_at: 	long	The timestamp in milliseconds for the transfer audition
    """

    def __init__(self, **kwargs):
        self.withdraw_id = ''
        self.currency = ''
        self.address = ''
        self.tx_hash = ''
        self.state = ''
        self.fee = ''
        self.created_at = 0
        self.audited_at = 0

        super().__init__(**kwargs)

        self.amount = Utils.safe_float(kwargs, 'amount')


