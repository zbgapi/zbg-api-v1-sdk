from enum import Enum


class CandlestickInterval(Enum):
    MIN1 = "1M"
    MIN5 = "5M"
    MIN15 = "15M"
    MIN30 = "30M"
    MIN60 = "1H"
    DAY1 = "1D"
    WEEK1 = "1W"


class OrderSide(Enum):
    BUY = 'buy'
    SELL = 'sell'
    INVALID = None


class OrderState(Enum):
    """
    order status，include：
    partial-filled: portion deal,
    partial-canceled: portion deal withdrawal,
    filled: complete deal,
    canceled: cancel，
    created: created (in storage)
    """
    PARTIAL_FILLED = 'partial-filled'
    PARTIAL_CANCELED = 'partial-canceled'
    FILLED = 'filled'
    CANCELED = 'canceled'
    CREATED = 'created'
    INVALID = None


class TransferType(Enum):
    """
    master-transfer-in:sub account transfer to main account currency
    master-transfer-out :main account transfer to sub account
    """
    MASTER_TRANSFER_IN = "master-transfer-in"
    MASTER_TRANSFER_OUT = "master-transfer-out"


class Direct(Enum):
    PREV = 'prev'
    NEXT = 'next'


class DepositType(Enum):
    """
    blockchain:	Blockchain roll-in
    system: 	system deposit
    recharge:	fiat recharge
    transfer:	Merchants transfer money to each other
    """
    BLOCKCHAIN = 'blockchain'
    SYSTEM = 'system'
    RECHARGE = 'recharge'
    TRANSFER = 'transfer'


class WithdrawState(Enum):
    """
    reexamine:	    Under examination for withdraw validation
    canceled:	    Withdraw canceled by user
    pass:   	    Withdraw validation passed
    reject: 	    Withdraw validation rejected
    transferred:	On-chain transfer initiated
    confirmed:  	On-chain transfer completed with one confirmation
    """
    REEXAMINE = 'canceled'
    CANCELED = 'canceled'
    PASS = 'pass'
    REJECT = 'reject'
    TRANSFERRED = 'transferred'
    CONFIRMED = 'confirmed'


class ConnectionState(Enum):
    IDLE = 0
    CONNECTED = 1
    CLOSED_ON_ERROR = 2


class SubscribeTopic(Enum):
    CANDLESTICK = "{}_KLINE_{}_{}"
    TRADE = "{}_TRADE_{}"
    TICKER = "{}_TRADE_STATISTIC_24H"
    PRICE_DEPTH = "{}_ENTRUST_ADD_{}"
    ORDER_CHANGE = "{}_RECORD_ADD_{}_{}"



