from unittest import TestCase
import zbg
from zbg.model.constant import TransferType, WithdrawState, Direct


class TestAccountApi(TestCase):
    api = zbg.AccountApi(api_key='7suPRyAbmOu7suPRyAbmOv',
                         secret_key='5033d710265eb96534359f31f9015670',
                         passphrase='098765')

    def test_get_account(self):
        print([m for m in dir(self.api) if m.startswith('private')])
        request = self.api.get_account()
        self.assertIsNotNone(request)
        print(request)

    def test_get_balances(self):
        request = self.api.get_balances()
        print(request)
        request = self.api.get_balances(filter_zero=True)
        print(request)
        print(request[0].balance)
        self.assertGreater(request[0].balance, 0.0)
        print(float("3.3e-05"))

    def test_get_balance(self):
        request = self.api.get_balance('zt')
        print(request)

    def test_get_aggregate_balance(self):
        request = self.api.get_aggregate_balance(filter_zero=True)
        print(request)

    def test_sub_balance(self):
        request = self.api.get_sub_balance('7nxFxAMjkDg', filter_zero=True)
        print(request)

    def test_transfer(self):
        self.api.transfer('7nxFxAMjkDg', 'zt', 7.26, TransferType.MASTER_TRANSFER_IN)
        request = self.api.get_sub_balance('7nxFxAMjkDg', filter_zero=True)
        print(request)

    def test_get_deposit_address(self):
        request = self.api.get_deposit_address('zt')
        self.assertFalse(request.is_memo)

    def test_get_historical_deposit(self):
        request = self.api.get_historical_deposit('zt')
        print(request)
        print(request[1])

    def test_get_withdraw_address(self):
        request = self.api.get_withdraw_address('qc')
        print(request)

    def test_get_historical_withdraw(self):
        request = self.api.get_historical_withdraw('qc', direct=Direct.PREV)
        print(request)
        self.assertTrue(request[3][0].state == WithdrawState.CONFIRMED.value)

    def test_withdraw(self):
        request = self.api.withdraw(currency='qc', address='QLcwMMTawEsED3KDb9bE6b1g5inenBGGpZ', amount=100)
        print(request)
        request = self.api.cancel_withdraw(request)
        print(request)
