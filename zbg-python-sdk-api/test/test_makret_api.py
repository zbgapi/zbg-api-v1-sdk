
from unittest import TestCase

import zbg
from zbg.model import CandlestickInterval


class TestMarketApi(TestCase):
    market_api = zbg.MarketApi()

    def test_get_candle_sticks(self):
        request = self.market_api.get_candlesticks('zt_usdt', CandlestickInterval.DAY1, 10)
        self.assertIsNotNone(request)
        self.assertIsNotNone(request[0].open)

    def test_get_ticker(self):
        request = self.market_api.get_ticker('zt_usdt')
        print(request)

    def test_get_tickers(self):
        request = self.market_api.get_tickers()
        print(request)

    def test_price_depth(self):
        request = self.market_api.get_price_depth('zt_usdt')
        self.assertIsNotNone(request.timestamp)
        print(request.bids[0])
        print(request.asks[-1])

    def test_trades(self):
        request = self.market_api.get_trades('zt_usdt')
        print(request)
        print(request[0].trade_time())

    def test_get_historical_trades(self):
        request = self.market_api.get_historical_trades('zt_usdt')
        print(request)

        request = self.market_api.get_historical_trades('zt_usdt', 'T6623032821957013504')
        print(request)

