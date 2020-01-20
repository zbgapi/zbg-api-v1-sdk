
import time
from unittest import TestCase
import zbg
from zbg.errors import ZbgApiException
from zbg.model.constant import SubscribeTopic
from zbg.model.subscribe_envet import *


class TestSubscriptionClient(TestCase):
    api = zbg.SubscriptionClient(api_key='7suPRyAbmOu7suPRyAbmOv',
                                 secret_key='5033d710265eb96534359f31f9015670',
                                 passphrase='098765',
                                 connection_delay_failure=5)

    def test_subscribe_candlestick_event(self):
        def callback(event: CandlestickEvent):
            print(">>  " + str(event))

        self.api.subscribe_candlestick_event(symbol='btc_usdt', interval=CandlestickInterval.MIN15, callback=callback)

        time.sleep(50)

    def test_subscribe_trade_event(self):
        def callback(event: TradeEvent):
            print(">>  " + str(event))

        self.api.subscribe_trade_event(symbol='btc_usdt', callback=callback)
        time.sleep(60)

    def test_subscribe_ticker_event(self):
        def callback(event: TickerEvent):
            print(">>  " + str(event))

        self.api.subscribe_ticker_event(symbol='btc_usdt', callback=callback)
        time.sleep(60)

    def test_all_subscribe_ticker_event(self):
        def callback(event: TickerEvent):
            print(">>  " + str(event))

        def error_handler(error: ZbgApiException):
            print("error", error)

        self.api.subscribe_ticker_event(symbol='all', callback=callback, error_handler=error_handler)
        time.sleep(60)

    def test_price_depth_event(self):
        def callback(event: PriceDepthEvent):
            print(">>  " + str(event))

        def error_handler(error: ZbgApiException):
            print("error", error)

        self.api.subscribe_price_depth_event(symbol='btc_usdt', callback=callback, error_handler=error_handler)
        time.sleep(60)

    def test_order_change_event(self):
        def callback(event: OrderChangeEvent):
            print(">>  " + str(event))

        def error_handler(error: ZbgApiException):
            print("error", error)

        self.api.subscribe_order_change_event(symbol='zt_usdt', callback=callback, error_handler=error_handler)
        time.sleep(120)

    def test_unsubscribe_event(self):
        def callback(event: CandlestickEvent):
            print(">>  " + str(event))

        conn_id = self.api.subscribe_candlestick_event(symbol='btc_usdt', interval=CandlestickInterval.MIN15, callback=callback)

        time.sleep(5)
        print(">>>> unsubscribe event")
        self.api.unsubscribe_event(conn_id)
        time.sleep(10)

    def test_re_connect_event(self):
        def callback(event: CandlestickEvent):
            print(">>  " + str(event))

        def error_handler(error: ZbgApiException):
            print("error", error)
        self.api.subscribe_order_change_event(symbol='zt_usdt', callback=callback, error_handler=error_handler)

        time.sleep(100)
