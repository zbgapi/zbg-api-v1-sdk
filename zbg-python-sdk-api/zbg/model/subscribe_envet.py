from zbg.model import *
from zbg.model.common import ResultModel
from zbg.utils import Utils


class CandlestickEvent(ResultModel):

    def __init__(self):
        super().__init__()
        self.data_type = "K"
        self.data_size = 0
        self.interval = CandlestickInterval.INVALID
        self.data = []

    @staticmethod
    def json_parse(data):
        obj = CandlestickEvent()
        if isinstance(data[0], list):
            obj.data_size = len(data)
            obj.data = [CandleStick.json_parse(item) for item in data]
        else:
            obj.data = [CandleStick.json_parse(data)]
            obj.data_size = 1

        return obj


class TradeEvent(ResultModel):

    def __init__(self):
        super().__init__()
        self.data_type = "T"
        self.data_size = 0
        self.data = []

    @staticmethod
    def json_parse(data):
        obj = TradeEvent()
        if isinstance(data[0], list):
            obj.data_size = len(data)
            obj.data = [Trade.json_parse(item) for item in data]
        else:
            obj.data = [Trade.json_parse(data)]
            obj.data_size = 1

        return obj


class TickerEvent(ResultModel):
    def __init__(self):
        super().__init__()
        self.data_type = "S"
        self.data_size = 0
        self.data = []

    @staticmethod
    def json_parse(data):
        data = data['trade_statistic']
        obj = TickerEvent()
        obj.data_size = len(data)
        obj.data = [Ticker.json_parse(item) for item in data]

        return obj


class PriceDepthEvent(ResultModel):

    def __init__(self):
        super().__init__()
        self.data_type = "AE"
        self.timestamp = 0
        # if data_type = 'AE' return {'bids':[{price, amount}, ...], 'asks':[{price, amount}, ...]}
        # if data_type = 'E'  return {'side': 'buy/sell', 'price':0.1, 'amount': 1}
        self.data = {}

    @staticmethod
    def json_parse(data):
        obj = PriceDepthEvent()
        if isinstance(data[0], list):
            data = data[0]
            obj.timestamp = Utils.safe_integer(data, 3)
            obj.data = {
                'asks': [DepthEntry.json_parse(e) for e in data[4]['asks']],
                'bids': [DepthEntry.json_parse(e) for e in data[5]['bids']]
            }
        else:
            obj.data_type = 'E'
            obj.timestamp = Utils.safe_integer(data, 2)
            obj.data = {
                'side': 'buy' if 'bid' == Utils.safe_string(data, 4).lower() else 'sell',
                'price': Utils.safe_float(data, 5),
                'amount': Utils.safe_float(data, 6),
            }
        return obj


class OrderChangeEvent(ResultModel):
    def __init__(self):
        super().__init__()
        self.data_type = "R"
        self.timestamp = 0
        self.data_size = 0
        self.data = []

    @staticmethod
    def json_parse(data):
        obj = OrderChangeEvent()
        if isinstance(data[0], list):
            data = data[0]
            obj.timestamp = Utils.safe_integer(data, 3)
            obj.data = [OrderChange.json_parse(item) for item in data[4]]
            obj.data_size = len(obj.data)
        else:
            obj.timestamp = Utils.safe_integer(data, 3)
            obj.data = [OrderChange.json_parse(data[4:])]
            obj.data_size = 1

        return obj


class OrderChange(ResultModel):
    def __init__(self):
        super().__init__()
        self.order_id = ''
        self.side = ''
        self.status = ''
        self.timestamp = 0
        self.price = 0.0
        self.amount = 0.0
        self.filled_amount = 0.0
        self.avg_amount = 0.0
        self.filled_cash_amount = 0.0

    @staticmethod
    def json_parse(item):
        order = OrderChange()
        order.order_id = item[0]
        order.side = 'buy' if Utils.safe_integer(item, 1) == 1 else 'sell'
        # status :  0:craeted 1:canceled 2: filled 3:partial-filled
        status = Utils.safe_integer(item, 2)
        if status == 0:
            order.status = 'created'
        elif status == 1:
            order.status = 'canceled'
        elif status == 2:
            order.status = 'filled'
        elif status == 3:
            order.status = 'partial-filled'

        order.price = Utils.safe_float(item, 3)
        order.amount = Utils.safe_float(item, 4)
        order.filled_amount = Utils.safe_float(item, 5, 0.0)
        order.filled_cash_amount = Utils.safe_float(item, 6, 0.0)
        order.avg_amount = Utils.safe_float(item, 7, 0.0)
        order.timestamp = Utils.safe_integer(item, 8)

        return order

