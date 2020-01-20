import time

from urllib import parse
import urllib.parse

import zbg
from zbg.model.constant import SubscribeTopic
from zbg.model.subscribe_envet import *
from zbg.websocket_connection import WebsocketConnection, ArgumentsRequired
from zbg.websocket_watch_dog import WebSocketWatchDog


class WebsocketRequest(object):

    def __init__(self):
        self.subscription_handler = None
        self.unsubscription_handler = None
        self.auto_close = False
        self.error_handler = None
        self.json_parser = None
        self.update_callback = None


class SubscriptionClient(object):
    def __init__(self, **kwargs):
        """
        Create the subscription client to subscribe the update from server.

        :param kwargs: The option of subscription connection.
            api_key: The public key applied from ZBG.
            secret_key: The private key applied from ZBG.
            uri: Set the URI for subscription.
            is_auto_connect: When the connection lost is happening on the subscription line, specify whether the client
                            reconnect to server automatically. The connection lost means:
                                Caused by network problem
                                The connection close triggered by server (happened every 24 hours)
                            No any message can be received from server within a specified time, see receive_limit_ms
            receive_limit_ms: Set the receive limit in millisecond. If no message is received within this limit time,
                            the connection will be disconnected.
            connection_delay_failure: If auto reconnect is enabled, specify the delay time before reconnect.
        """

        api_key = None
        secret_key = None
        passphrase = None
        if "api_key" in kwargs:
            api_key = kwargs["api_key"]
        if "secret_key" in kwargs:
            secret_key = kwargs["secret_key"]
        if "passphrase" in kwargs:
            passphrase = kwargs["passphrase"]
        self.__api_key = api_key
        self.__secret_key = secret_key
        self.__passphrase = passphrase
        self.connections = list()

        self.url = 'wss://kline.zbgpro.net/websocket'
        if 'url' in kwargs:
            self.url = kwargs["url"]

        host = urllib.parse.urlparse(self.url).hostname
        api_url = 'https://' + host.replace('kline', 'www')

        self.account = zbg.AccountApi(api_key, secret_key, passphrase, api_url)
        self.is_aut_connect = True
        if 'is_auto_connect' in kwargs:
            self.is_aut_connect = kwargs['is_auto_connect']

        is_auto_connect = True
        receive_limit_ms = 60000
        connection_delay_failure = 15
        if "uri" in kwargs:
            self.uri = kwargs["uri"]
        if "is_auto_connect" in kwargs:
            is_auto_connect = kwargs["is_auto_connect"]
        if "receive_limit_ms" in kwargs:
            receive_limit_ms = kwargs["receive_limit_ms"]
        if "connection_delay_failure" in kwargs:
            connection_delay_failure = kwargs["connection_delay_failure"]
        self.__watch_dog = WebSocketWatchDog(is_auto_connect, receive_limit_ms, connection_delay_failure)

        self.subscribe_message_pattern = '{{"action": "ADD", "dataType": "{}", "dataSize": {}}}'
        self.unsubscribe_message_pattern = '{{"action":"DEL", "dataType":"{}"}}'

    def __create_connection(self, request):
        connection = WebsocketConnection(self.__api_key, self.__secret_key, self.url, self.__watch_dog, request)
        self.connections.append(connection)
        connection.connect()
        return connection.id

    def subscribe_candlestick_event(self, symbol: 'str', interval: CandlestickInterval, callback, error_handler=None, init_data_size=1000):
        """
        Subscribe candlestick/kline event. If the candlestick/kline is updated, server will send the data to client and onReceive in callback will
        be called.

        :param symbol: The symbols, like "btc_usdt".
        :param interval: The candlestick/kline interval, MIN1, MIN5, DAY1 etc.
        :param callback: The implementation is required. onReceive will be called if receive server's update.
            example: def callback(candlestick_event: CandlestickEvent):
                        pass
        :param error_handler: The error handler will be called if subscription failed or error happen between client and Huobi server
            example: def error_handler(exception: ZbgApiException)
                        pass
        :param init_data_size: The number of data returned the first time.
        :return: No return
        """
        market = self.account.check_symbol(symbol=symbol)
        topic = SubscribeTopic.CANDLESTICK.value.format(market['id'], interval.value, symbol.upper())

        def subscription_handler(connection):
            connection.send(self.subscribe_message_pattern.format(topic, init_data_size))

        def unsubscription_handler(connection):
            connection.send(self.unsubscribe_message_pattern.format(topic))

        def json_parse(json_wrapper):
            candle_stick_event_obj = CandlestickEvent.json_parse(json_wrapper)
            candle_stick_event_obj.interval = interval.value
            return candle_stick_event_obj

        request = WebsocketRequest()
        request.subscription_handler = subscription_handler
        request.unsubscription_handler = unsubscription_handler
        request.json_parser = json_parse
        request.update_callback = callback
        request.error_handler = error_handler
        return self.__create_connection(request)

    def subscribe_trade_event(self, symbol: 'str', callback, error_handler=None, init_data_size=1000):
        """
        Subscribe trade event. If the trade is generated, server will send the data to client and onReceive in callback will be called.

        :param symbol: The symbols, like "btc_usdt".
        :param callback: The implementation is required. onReceive will be called if receive server's update.
            example: def callback(candlestick_event: CandlestickEvent):
                        pass
        :param error_handler: The error handler will be called if subscription failed or error happen between client and Huobi server
            example: def error_handler(exception: ZbgApiException)
                        pass
        :param init_data_size: The number of data returned the first time.
        :return: No return
        """
        market = self.account.check_symbol(symbol)

        def subscription_handler(connection):
            connection.send(self.subscribe_message_pattern.format('_'.join([market['id'], 'TRADE', symbol.upper()]), init_data_size))

        def json_parse(json_wrapper):
            return TradeEvent.json_parse(json_wrapper)

        request = WebsocketRequest()
        request.subscription_handler = subscription_handler
        request.json_parser = json_parse
        request.update_callback = callback
        request.error_handler = error_handler
        return self.__create_connection(request)

    def subscribe_ticker_event(self, symbol: 'str', callback, error_handler=None):
        """
        Subscribe 24 hours trade statistics event. If the statistics is generated, server will send the data to client and onReceive in callback will be called.

        :param symbol: The symbol, like "btc_usdt" or "ALL".
        :param callback: The implementation is required. onReceive will be called if receive server's update.
            example: def callback(candlestick_event: CandlestickEvent):
                        pass
        :param error_handler: The error handler will be called if subscription failed or error happen between client and Huobi server
            example: def error_handler(exception: ZbgApiException)
                        pass
        :return: No return
        """
        if symbol.upper() == 'ALL':
            symbol_id = 'ALL'
        else:
            market = self.account.check_symbol(symbol)
            symbol_id = market['id']

        def subscription_handler(connection):
            # ALL_TRADE_STATISTIC_24H , 5140_TRADE_STATISTIC_24H
            connection.send(self.subscribe_message_pattern.format('_'.join([symbol_id, 'TRADE_STATISTIC_24H']), 1))

        def json_parse(json_wrapper):
            ticker_event_obj = TickerEvent.json_parse(json_wrapper)
            for data in ticker_event_obj.data[:]:
                if symbol.upper() == 'ALL':
                    m = self.account.safe_get_symbol(data.symbol_id)
                    if m is None:
                        ticker_event_obj.data.remove(data)
                    data.symbol = m
                else:
                    data.symbol = symbol

            ticker_event_obj.data_size = len(ticker_event_obj.data)

            return ticker_event_obj

        request = WebsocketRequest()
        request.subscription_handler = subscription_handler
        request.json_parser = json_parse
        request.update_callback = callback
        request.error_handler = error_handler
        return self.__create_connection(request)

    def subscribe_price_depth_event(self, symbol: 'str', callback, error_handler=None):
        """
        Subscribe price depth event. If the price depth is updated, server will send the data to client and onReceive in callback will be called.

        :param symbol: The symbols, like "btc_usdt".
        :param callback: The implementation is required. onReceive will be called if receive server's update.
            example: def callback(candlestick_event: CandlestickEvent):
                        pass
        :param error_handler: The error handler will be called if subscription failed or error happen between client and Huobi server
            example: def error_handler(exception: ZbgApiException)
                        pass
        :return: No return
        """
        market = self.account.check_symbol(symbol)

        def subscription_handler(connection):
            connection.send(self.subscribe_message_pattern.format('_'.join([market['id'], 'ENTRUST_ADD', symbol.upper()]), 1))

        def json_parse(json_wrapper):
            return PriceDepthEvent.json_parse(json_wrapper)

        request = WebsocketRequest()
        request.subscription_handler = subscription_handler
        request.json_parser = json_parse
        request.update_callback = callback
        request.error_handler = error_handler
        return self.__create_connection(request)

    def subscribe_order_change_event(self, symbol: 'str', callback, error_handler=None, init_data_size=50):
        """
        Subscribe order changing event. If a order is created, canceled etc, server will send the data to client and onReceive in callback will be called.

        :param symbol: The symbol, like "btc_usdt" or "ALL".
        :param callback: The implementation is required. onReceive will be called if receive server's update.
            example: def callback(candlestick_event: CandlestickEvent):
                        pass
        :param error_handler: The error handler will be called if subscription failed or error happen between client and Huobi server
            example: def error_handler(exception: ZbgApiException)
                        pass
        :param init_data_size: The number of data returned the first time.
        :return: No return
        """
        market = self.account.check_symbol(symbol)
        symbol_id = market['id']

        account_id = self.account.get_account().user_id

        def subscription_handler(connection):
            # ALL_TRADE_STATISTIC_24H , 5140_TRADE_STATISTIC_24H
            connection.send(self.subscribe_message_pattern.format('_'.join([symbol_id, 'RECORD_ADD', account_id, symbol.upper()]), init_data_size))

        def json_parse(json_wrapper):
            return OrderChangeEvent.json_parse(json_wrapper)

        request = WebsocketRequest()
        request.subscription_handler = subscription_handler
        request.json_parser = json_parse
        request.update_callback = callback
        request.error_handler = error_handler
        return self.__create_connection(request)

    def unsubscribe_event(self, conn_id: int):
        connections = [connection for connection in self.connections if connection.id == conn_id]
        if connections:
            connections[0].close_on_hand()

