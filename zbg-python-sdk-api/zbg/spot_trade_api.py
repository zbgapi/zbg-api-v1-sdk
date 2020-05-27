from typing import List

from zbg.client import ApiClient, ArgumentsRequired, OrderNotCached
from zbg.model.constant import *
from zbg.model.spot_trade import Order
from zbg.model.spot_trade import Trade


class SpotTradeApi(ApiClient):

    def __init__(self, api_key, secret_key, passphrase=None, api_host=None):
        describe = {
            'apis': {
                'private': {
                    'get': {
                        'open_orders': '/exchange/api/v1/order/open-orders',
                        'historical_orders': '/exchange/api/v1/order/orders',
                        'order': '/exchange/api/v1/order/detail',
                        'order_trade': '/exchange/api/v1/order/trades',
                    },
                    'post': {
                        'create_order': '/exchange/api/v1/order/create',
                        'cancel_order': '/exchange/api/v1/order/cancel',
                        'batch_cancel_order': '/exchange/api/v1/order/batch-cancel',

                    },
                },
            },
            'exceptions': {
                '2012': OrderNotCached,

            }
        }

        super().__init__(api_key, secret_key, passphrase, api_host, describe)

    def buy(self, symbol: str, price: float, amount: float) -> str:
        """
        Send a new buy order to ZBG for matching

        :param symbol: Trading pair，example：btc_usdt, eth_usdt...(zbg supported [symbols])
        :param price: The price of the order
        :param amount: Buy quantity
        :return: order id
        """
        self.check_symbol(symbol)

        if price is None or price <= 0:
            raise ArgumentsRequired("Order price must be greater than 0.")

        if amount is None or amount <= 0:
            raise ArgumentsRequired("Order amount must be greater than 0.")
        params = {
            'symbol': symbol,
            'price': price,
            'amount': amount,
            'side': 'buy'
        }

        return self.private_post_create_order(params)

    def sell(self, symbol: str, price: float, amount: float) -> str:
        """
        Send a new sell order to ZBG for matching

        :param symbol: Trading pair，example：btc_usdt, eth_usdt...(zbg supported [symbols])
        :param price: The price of the order
        :param amount: Sell quantity
        :return: order id
        """
        self.check_symbol(symbol)

        if price is None or price <= 0:
            raise ArgumentsRequired("Order price must be greater than 0.")

        if amount is None or amount <= 0:
            raise ArgumentsRequired("Order amount must be greater than 0.")

        params = {
            'symbol': symbol,
            'price': price,
            'amount': amount,
            'side': 'sell'
        }

        return self.private_post_create_order(params)

    def cancel_order(self, symbol: str, order_id: str):
        """
        Submit Cancel for an Order

        :param symbol: Trading pair，example：btc_usdt, eth_usdt...(zbg supported [symbols])
        :param order_id: Cancel Order Id
        :return: None
        """

        self.check_symbol(symbol)

        params = {
            'symbol': symbol,
            'order-id': order_id,
        }

        self.private_post_cancel_order(params)

    def batch_cancel_orders(self, symbol: str, order_side=None, order_ids=None, price_from=None, price_to=None) -> int:
        """

        :param symbol: Trading pair，example：btc_usdt, eth_usdt...(zbg supported [symbols])
        :param order_side: Active trading direction，“buy”or“sell”，
        :param order_ids: Order IDs
        :param price_from: Delegate price interval cancel: cancel the delegate of unit price >=price-from
        :param price_to: Delegate price interval cancellation: cancel unit price <=price-to
        :return: The number of cancelled orders
        """

        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'order-ids': order_ids,
            'price_from': price_from,
            'price_to': price_to,
        }

        if order_side:
            params['side'] = order_side.value

        return self.private_post_batch_cancel_order(params)

    OrderList = List[Order]

    def get_open_orders(self, symbol: str, page=1, size=20) -> (int, int, int, OrderList):
        """
        Returns all open orders which have not been filled completely.

        :param symbol: trading pair，example：btc_usdt,eth_usdt
        :param page: Page number, default 1
        :param size: Page number, default 1
        :return: orders
        """
        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'page': page,
            'size': size,
        }

        datas = self.private_get_open_orders(params)

        return datas['rows'], datas['page'], datas['size'], [Order(**order) for order in datas['list']]

    def get_historical_orders(self, symbol: str,
                              order_side=OrderSide.INVALID,
                              order_state=OrderState.INVALID,
                              start_date=None,
                              end_date=None,
                              history=False,
                              page=1,
                              size=100) -> (int, int, int, OrderList):
        """
        Returns orders based on a specific searching criteria.

        :param symbol: trading pair，example：btc_usdt,eth_usdt
        :param order_side: order type，buy/sell
        :param order_state: status，valid status：
                            partial-filled:     portion deal,
                            partial-canceled:   portion deal withdrawal,
                            filled:             completely deal,
                            canceled:           Had withdrawn，
                            created:            created (in storage)
        :param start_date: enquire Start date, date format yyyy-mm-dd
        :param end_date: enquire end date, date format yyyy-mm-dd
        :param history: For enquire history, default false
        :param page: Page number, default 1
        :param size: Page number, default 1
        :return: orders
        """

        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'history': history,
            'page': page,
            'size': size,
        }

        if order_side:
            params['side'] = order_side.value

        if order_state:
            params['state'] = order_state.value

        if start_date:
            params['start-date'] = start_date

        if start_date:
            params['end-date'] = end_date

        data = self.private_get_historical_orders(params)

        return data['rows'], data['page'], data['size'], [Order(**order) for order in data['list']]

    def get_order(self, symbol: str, order_id: str) -> Order:
        """
        Get the order detail

        :param symbol: trading pair，example：btc_usdt,eth_usdt
        :param order_id: order id
        :return: order record
        """

        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'order-id': order_id
        }

        order = self.private_get_order(params)

        return Order(**order)

    TradeList = List[Trade]

    def get_order_trade(self, symbol: str, order_id: str) -> TradeList:
        """
        Get the trade detail of an order

        :param symbol: trading pair，example：btc_usdt,eth_usdt
        :param order_id: order id
        :return: trades
        """

        self.check_symbol(symbol)
        params = {
            'symbol': symbol,
            'order-id': order_id
        }

        data = self.private_get_order_trade(params)

        return [Trade(**trade) for trade in data]
