from zbg.model.common import ResultModel
from zbg.utils import Utils


class Order(ResultModel):
    """
    Order data

    :member
        order_id:   	    string	order ID
        symbol: 	        string	trading pair
        price:	            decimal	Entrusted price
        side:	            string	order type， buy/sell
        amount: 	        string	Total amount
        available_amount:	string	Outstanding quantity
        filled_amount:  	string	The amount which has been filled
        filled-cash-amount:	string	The filled total in quote currency
        state:          	string	order status:
                                    submitted：Order application (not in storage),
                                    partial-filled：Part of the deal
                                    cancelling：Is removed from the single,
                                    created：created (in storage)
        created_at:     	long	The timestamp in milliseconds when the order was created
    """

    def __init__(self, **kwargs):
        self.order_id = ''
        self.symbol = ''
        self.side = ''
        self.state = ''
        self.created_at = 0

        super().__init__(**kwargs)

        self.price = Utils.safe_float(kwargs, 'price')
        self.amount = Utils.safe_float(kwargs, 'amount')
        self.available_amount = Utils.safe_float(kwargs, 'available-amount')
        self.filled_amount = Utils.safe_float(kwargs, 'filled-amount', 0)
        self.filled_cash_amount = Utils.safe_float(kwargs, 'filled-cash-amount', 0)


class Trade(ResultModel):
    """
    Trade data

    :member
        trade_id:	    string	Unique trade ID
        order_id:	    string	order ID
        match_id:	    string	The match order id of this match
        symbol:         string	trading pair
        price:  	    decimal	The limit price of limit order
        side:   	    string	initiative order type，buy/sell
        filled_amount:	decimal	The amount which has been filled
        filled-fees:	decimal	Transaction fee paid so far
        role:       	string	the role in the transaction: taker or maker
        created-at: 	long	The timestamp in milliseconds when the match and fill is done
    """

    def __init__(self, **kwargs):
        self.trade_id = ''
        self.order_id = ''
        self.match_id = ''
        self.symbol = ''
        self.side = ''
        self.role = ''
        self.created_at = 0

        super().__init__(**kwargs)

        self.price = Utils.safe_float(kwargs, 'price')
        self.filled_amount = Utils.safe_float(kwargs, 'filled-amount', 0)
        self.filled_fees = Utils.safe_float(kwargs, 'filled-fees', 0)
