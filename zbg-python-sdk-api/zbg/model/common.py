"""
Basic Data, include symbol, currency, server time, assist price
"""
from zbg.utils import Utils


class ResultModel(dict):
    """
    The base class of the response data.
    """

    def __init__(self, **kw):
        super().__init__()
        for k, v in kw.items():
            self.__setattr__(k, v)

    def __setattr__(self, name, value):
        self[str.replace(name, '-', '_')] = value

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'%s' object has no attribute '%s'" % (self.__class__.__name__, key))


class Symbol(ResultModel):
    """
    The ZBG supported symbols.

    :member
        id: The symbol id in ZBG.
        base_currency: The base currency in a trading symbol.
        quote_currency: The quote currency in a trading symbol.
        price_precision: The quote currency precision when quote price (decimal places).
        amount_precision: The base currency precision when quote amount (decimal places).
        symbol_partition: The trading section, possible values: [main，innovation，bifurcation].
        symbol: The symbol, like "btc_usdt".
        state : trade status, maybe one in [online，offline,suspend]
        min_order_amt : minimum volume limit only used in limit-order and sell-market order
        # max_order_amt : Maximum volume
        """

    def __init__(self, **kwargs):
        self.id = ""
        self.base_currency = ""
        self.quote_currency = ""
        self.price_precision = 0
        self.amount_precision = 0
        self.symbol_partition = ""
        self.symbol = ""
        self.state = ""

        # self.max_order_amt = ""

        super().__init__(**kwargs)
        self.min_order_amt = Utils.safe_float(kwargs, "min-order-amt")


class Currency(ResultModel):
    """
    The ZBG supported currencies.

    :member
        id: The currency id in ZBG.
        name: Name of the currency.
        draw_flag: Whether can withdraw.
        draw_fee: Withdraw Commission.
        once_draw_limit: Maximum withdrawal limit.
        daily_draw_limit: Maximum daily withdrawal limit.
        min_draw_limit: Minimum withdrawal limit.
        """

    def __init__(self, **kwargs):
        self.id = ""
        self.name = ""
        self.draw_flag = ""
        self.once_draw_limit = 0
        self.daily_draw_limit = 0

        super().__init__(**kwargs)
        self.draw_fee = Utils.safe_float(kwargs, "draw-fee")
        self.min_draw_limit = Utils.safe_float(kwargs, "min-draw-limit")


class AssistPrice(ResultModel):
    """
    The discount price of the specified currency in usd, cny and btc.

    :member
        btc : dict, The discount price of the currencies in btc.
        cny : dict, The discount price of the currencies in cny.
        usd : dict, The discount price of the currencies in usd.
    """

    def __init__(self, **kwargs):
        self.btc = {}
        self.cny = {}
        self.usd = {}

        super().__init__(**kwargs)

    def to_btc_price(self, coin):
        try:
            return float(self.btc[coin])
        except KeyError:
            raise AttributeError(r"There is no currency [%s] price converted into BTC" % coin)

    def to_cny_price(self, coin):
        try:
            return float(self.cny[coin])
        except KeyError:
            raise AttributeError(r"There is no currency [%s] price converted into CNY" % coin)

    def to_usd_price(self, coin):
        try:
            return float(self.usd[coin])
        except KeyError:
            raise AttributeError(r"There is no currency [%s] price converted into USD" % coin)


