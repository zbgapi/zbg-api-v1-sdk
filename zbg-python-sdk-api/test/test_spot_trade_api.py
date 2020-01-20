from unittest import TestCase
import zbg
from zbg.model.constant import *


class TestSpotTradeApi(TestCase):
    api = zbg.SpotTradeApi(api_key='7suPRyAbmOu7suPRyAbmOv',
                           secret_key='5033d710265eb96534359f31f9015670',
                           passphrase='098765')

    def test_buy(self):
        request = self.api.buy(symbol='zt_usdt', price=0.0407, amount=1000)
        print(request)

    def test_sell(self):
        request = self.api.sell(symbol='zt_usdt', price=0.0417, amount=1000)
        print(request)

    def test_cancel_order(self):
        request = self.api.cancel_order('zt_usdt', 'E6623755275163676672')
        print(request)

    def test_batch_cancel_orders(self):
        request = self.api.batch_cancel_orders(symbol='zt_usdt', order_side=OrderSide.BUY)
        print(request)

    def test_get_open_orders(self):
        request = self.api.get_open_orders('zt_usdt')
        print(request)

    def test_get_historical_orders(self):
        request = self.api.get_historical_orders(symbol='zt_usdt', order_state=OrderState.FILLED)
        print(request)

    def test_get_order(self):
        request = self.api.get_order(symbol='zt_usdt', order_id='E6623758335143325696')
        print(request)

    def test_get_order_trade(self):
        request = self.api.get_order_trade(symbol='zt_usdt', order_id='E6623236610337026048')
        print(request)
