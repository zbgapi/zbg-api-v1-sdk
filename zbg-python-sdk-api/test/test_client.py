from datetime import datetime
from unittest import TestCase

import zbg
from zbg.utils import Utils


class TestClient(TestCase):
    client = zbg.ApiClient()
    client.urls['api'] = 'https://www.zbgpro.net'

    def test_request(self):
        request = self.client.request("/exchange/api/v1/common/symbols")
        self.assertIsNotNone(request)

    def test_get_server_time(self):
        server_time = self.client.get_server_time()
        print(server_time)
        print(datetime.fromtimestamp(server_time / 1000))

    def test_get_symbols(self):
        symbols = self.client.get_markets()
        self.assertTrue(len(symbols) > 0)
        print(symbols)
        print(symbols[0].symbol, symbols[0].base_currency)

    def test_get_currencies(self):
        symbols = self.client.get_currencies()
        print(symbols)

    def test_index_by(self):
        symbols = self.client.load_markets()
        symbols_by_id = Utils.index_by(symbols, "id")
        self.assertTrue('336' in symbols_by_id)

    def test_load_symbols(self):
        symbols = self.client.load_markets()
        self.assertTrue(len(symbols) > 0)
        print(self.client.markets_by_name['zt_usdt'])
        print(self.client.markets_by_id['336'])
        self.assertTrue('btc_usdt' in self.client.markets_by_name)
        self.assertTrue('btc' in self.client.currencies_by_name)

    def test_get_assist_price(self):
        assist_price = self.client.get_assist_price("zt", "eos")
        print(assist_price)
        print(assist_price.to_btc_price('zt'))

