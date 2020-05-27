# -*- coding: utf-8 -*-

"""zbg client"""
import collections
import functools
import hashlib
import json
import time

from typing import List

import requests
from requests import Timeout

from zbg.errors import *
from zbg.model.common import Symbol, Currency, AssistPrice
from zbg.utils import Utils


class ApiClient(object):
    use_server_time = False
    enable_rate_limit = False
    last_rest_request_Timestamp = 0
    rate_limit = 2000  # milliseconds = seconds * 1000
    timeout = 10000  # milliseconds = seconds * 1000
    verbose = True

    markets = None
    currencies = None
    markets_by_id = None
    markets_by_name = None
    currencies_by_id = None
    currencies_by_name = None

    urls = {
        'logo': 'https://www.zbg.com/src/images/logo.png',
        'api': 'https://www.zbgpro.net',
        'market_api': 'https://kline.zbgpro.net',
        'doc': 'https://www.zbgpro.net/docs/cn/api',
    }
    apis = {
        'public': {
            'get': {
                'currencies': '/exchange/api/v1/common/currencys',
                'symbols': '/exchange/api/v1/common/symbols',
                'server_time': '/exchange/api/v1/common/timestamp',
                'assist_price': '/exchange/api/v1/common/assist-price'
            },
        },
    }
    exceptions = {
        '999': InvalidSign,
        '6000': ArgumentsRequired,
        '1011': InsufficientFunds,
    }

    def __init__(self, api_key=None, secret_key=None, passphrase=None, api_host=None, config={}):

        for key in config:
            if hasattr(self, key) and isinstance(getattr(self, key), dict):
                setattr(self, key, self.deep_extend(getattr(self, key), config[key]))
            else:
                setattr(self, key, config[key])

        if api_key:
            self.__api_key = api_key
        if secret_key:
            self.__secret_key = secret_key
        if passphrase:
            self.__passphrase = passphrase
        if api_host:
            self.urls['api'] = api_host

        self.define_rest_api(self.apis, 'request')

    def request(self, path, api='public', method="GET", params={}, headers=None):
        if self.enable_rate_limit:
            self.throttle()

        self.last_rest_request_Timestamp = Utils.milliseconds()

        if api == 'private':
            headers = self.sign(method, params, headers)

        # 设置路径参数
        path = path.format(**params)
        api = self.urls['market_api'] if path.find('api/data/v1') >= 0 else self.urls['api']
        url = api + path

        response = None
        try:
            if self.verbose:
                print('method:', method, ', url :', url, ", request:", params)

            if method == "GET":
                response = requests.get(url, params=params, headers=headers)
            else:
                response = requests.post(url, data=json.dumps(params, separators=(',', ':')), headers=headers)

            if self.verbose:
                print('method:', method, ', url:', url, ", response:", response.text)

            self.handle_fail(response, method, url)

            return response.json()['datas']
        except Timeout as e:
            self.raise_error(RequestTimeout, method, url, e)
        except ValueError as e:
            self.raise_error(BadResponse, method, url, e, response.text)
        except KeyError as e:
            self.raise_error(BadResponse, method, url, e, response.text)

    def throttle(self):
        now = float(Utils.milliseconds())
        elapsed = now - self.last_rest_request_Timestamp
        if elapsed < self.rate_limit:
            delay = self.rate_limit - elapsed
            time.sleep(delay / 1000.0)

    def sign(self, method='GET', params=None, headers=None):
        if self.__api_key == '' or self.__secret_key == '':
            raise AuthenticationError('Api key and secret key must not be empty.')

        timestamp = str(Utils.milliseconds())
        if self.use_server_time:
            timestamp = str(self.get_server_time())

        param = ''
        if method == 'GET' and params:
            for k in sorted(params):
                param += k + str(params[k])
        elif method == 'POST' and params:
            param = json.dumps(params, separators=(',', ':'))

        sig_str = self.__api_key + timestamp + param + self.__secret_key
        signature = hashlib.md5(sig_str.encode('utf-8')).hexdigest()

        new_headers = {
            'Apiid': self.__api_key,
            'Timestamp': timestamp,
            'Clienttype': "5",
            'Sign': signature
        }
        if headers:
            self.extend(new_headers, headers)

        if hasattr(self, "_ApiClient__passphrase"):
            new_headers['Passphrase'] = hashlib.md5((timestamp + self.__passphrase).encode('utf-8')).hexdigest()

        return new_headers

    def handle_fail(self, response, method=None, url=None):
        if 404 == response.status_code:
            raise NotSupported("not supported.")
        body = response.json()
        code = body['resMsg']['code']
        if '1' != code:
            message = 'method: ' + method + ', url: ' + url + ', error code: ' + code + ", message: " + body['resMsg']['message']

            if code in self.exceptions:
                exception_class = self.exceptions[code]
                raise exception_class(message)
            else:
                raise ZbgApiException(message)

    def get_server_time(self) -> int:
        return int(self.public_get_server_time())

    SymbolList = List[Symbol]

    def load_markets(self, reload=False) -> SymbolList:
        if reload or not self.markets:
            self.markets = self.get_markets()
            self.markets_by_id = Utils.index_by(self.markets, "id")
            self.markets_by_name = Utils.index_by(self.markets, "symbol")

            self.currencies = self.get_currencies()
            self.currencies_by_id = Utils.index_by(self.currencies, "id")
            self.currencies_by_name = Utils.index_by(self.currencies, "name")

        return self.markets

    def get_markets(self) -> SymbolList:
        data_array = self.public_get_symbols()
        symbols = list()
        for item in data_array:
            symbols.append(Symbol(**item))
        return symbols

    CurrencyList = List[Currency]

    def get_currencies(self) -> CurrencyList:
        data_array = self.public_get_currencies()
        currencies = list()
        for item in data_array:
            currencies.append(Currency(**item))
        return currencies

    def get_assist_price(self, *currencies: str) -> AssistPrice:
        request = {}
        if currencies:
            request['currencys'] = ",".join(currencies)
        data = self.public_get_assist_price(request)

        return AssistPrice(**data)

    def check_symbol(self, symbol: str):
        if symbol is None:
            raise ArgumentsRequired("[Input] symbol should not be null")

        self.load_markets()
        if symbol not in self.markets_by_name:
            raise NotSupported("'" + symbol + "' is not yet supported by the zbg.")

        return self.markets_by_name[symbol]

    def check_currency(self, currency: str):
        self.load_markets()
        if currency not in self.currencies_by_name:
            raise NotSupported("'" + currency + "' is not yet supported by the zbg.")

    def safe_get_symbol(self, symbol_id):
        if symbol_id is None:
            return None

        self.load_markets()
        if symbol_id not in self.markets_by_id:
            return None
        return self.markets_by_id[symbol_id]['symbol']

    @staticmethod
    def raise_error(exception_type, method=None, url=None, error=None, details=None):
        if error:
            error = str(error)
        output = ' '.join([var for var in (url, method, error, details) if var is not None])
        raise exception_type(output)

    @staticmethod
    def extend(*args):
        if args is not None:
            if type(args[0]) is collections.OrderedDict:
                result = collections.OrderedDict()
            else:
                result = {}
            for arg in args:
                result.update(arg)
            return result
        return {}

    @staticmethod
    def deep_extend(*args):
        result = None
        for arg in args:
            if isinstance(arg, dict):
                if not isinstance(result, dict):
                    result = {}
                for key in arg:
                    result[key] = ApiClient.deep_extend(result[key] if key in result else None, arg[key])
            else:
                result = arg
        return result

    @classmethod
    def define_rest_api(cls, api, method_name):
        entry = getattr(cls, method_name)  # returns a function (instead of a bound method)
        for api_type, methods in api.items():
            for http_method, urls in methods.items():
                for alias, url in urls.items():
                    url = url.strip()

                    uppercase_method = http_method.upper()
                    lowercase_method = http_method.lower()

                    underscore = api_type + '_' + lowercase_method + '_' + alias

                    def partialer():
                        outer_kwargs = {'path': url, 'api': api_type, 'method': uppercase_method}

                        @functools.wraps(entry)
                        def inner(_self, params=None):
                            inner_kwargs = dict(outer_kwargs)  # avoid mutation
                            if params is not None:
                                inner_kwargs['params'] = params
                            return entry(_self, **inner_kwargs)

                        return inner

                    to_bind = partialer()
                    setattr(cls, underscore, to_bind)
