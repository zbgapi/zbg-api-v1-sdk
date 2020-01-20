"""
Market Data Api
"""
from typing import List

from zbg import ApiClient
from zbg.model.market import *
from zbg.model.constant import *


class MarketApi(ApiClient):

    def __init__(self, market_api_host=None):
        describe = {
            'apis': {
                'public': {
                    'get': {
                        'klines': '/api/data/v1/klines',
                        'ticker': '/api/data/v1/ticker',
                        'tickers': '/api/data/v1/tickers',
                        'price_depth': '/api/data/v1/entrusts',
                        'trades': '/api/data/v1/trades',
                        'historical_trades': '/exchange/api/v1/common/trade-history/{symbol}',
                        'historical_trades_by_id': '/exchange/api/v1/common/trade-history/{symbol}/{trade-id}',
                    },
                },
            }
        }

        if market_api_host:
            describe['urls'] = {
                'market_api': market_api_host,
            }

        super().__init__(config=describe)

    CandleStickList = List[CandleStick]

    def get_candlesticks(self, symbol: str, interval=CandlestickInterval.MIN15, data_size=100) -> CandleStickList:
        self.check_symbol(symbol)

        params = {
            'marketName': symbol,
            'type': interval.value,
            'dataSize': data_size,

        }
        data_array = self.public_get_klines(params)

        return [CandleStick.json_parse(e) for e in data_array]

    def get_ticker(self, symbol: str) -> Ticker:
        self.check_symbol(symbol)
        params = {
            'marketName': symbol,
        }
        data_array = self.public_get_ticker(params)

        ticker = Ticker.json_parse(data_array)
        ticker.symbol = symbol

        return ticker

    Tickers = List[Ticker]

    def get_tickers(self) -> Tickers:
        self.load_markets()
        data_array = self.public_get_tickers()

        tickers = list()
        for item in data_array:
            ticker = Ticker.json_parse(item)
            if ticker.symbol_id in self.markets_by_id:
                ticker.symbol = self.markets_by_id[ticker.symbol_id]['symbol']
                tickers.append(ticker)

        return tickers

    def get_price_depth(self, symbol: str, size=50) -> PriceDepth:
        self.check_symbol(symbol)
        params = {
            'marketName': symbol,
            'dataSize': size
        }

        data_object = self.public_get_price_depth(params)

        return PriceDepth.json_parse(data_object)

    TradeList = List[Trade]

    def get_trades(self, symbol: str, size=80) -> TradeList:
        self.check_symbol(symbol)
        params = {
            'marketName': symbol,
            'dataSize': size
        }

        data_array = self.public_get_trades(params)

        return [Trade.json_parse(data_object) for data_object in data_array]

    HistoricalTradeList = List[HistoricalTrade]

    def get_historical_trades(self, symbol: str, trade_id=None) -> HistoricalTradeList:
        """
        Return The latest 80 transaction records.

        :param symbol: The name of the market.
        :param trade_id: The maximum 1000 historical transaction records from [trade-id] onwards.
        :return: The historical trade records.
        """

        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'trade-id': trade_id
        }

        data_array = self.public_get_historical_trades_by_id(params) if trade_id else self.public_get_historical_trades(params)

        return [HistoricalTrade(**data_object) for data_object in data_array]
