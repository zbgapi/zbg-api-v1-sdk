from typing import List

from zbg.client import ApiClient, ArgumentsRequired
from zbg.model.account import *
from zbg.model.constant import TransferType, Direct


class AccountApi(ApiClient):

    def __init__(self, api_key, secret_key, passphrase, api_host=None):
        describe = {
            'apis': {
                'private': {
                    'get': {
                        'accounts': '/exchange/api/v1/account/accounts',
                        'balances': '/exchange/api/v1/account/balance',
                        'balance': '/exchange/api/v1/account/balance/{currency}',
                        'aggregate_balance': '/exchange/api/v1/account/sub/aggregate-balance',
                        'sub_balance': '/exchange/api/v1/account/sub/balance/{sub-uid}',
                        'deposit_address': '/exchange/api/v1/account/deposit/address',
                        'historical_deposit': '/exchange/api/v1/account/deposit/history',
                        'withdraw_address': '/exchange/api/v1/account/withdraw/address',
                        'historical_withdraw': '/exchange/api/v1/account/withdraw/history',
                    },
                    'post': {
                        'transfer': '/exchange/api/v1/account/sub/transfer',
                        'withdraw': '/exchange/api/v1/account/withdraw/create',
                        'cancel_withdraw': '/exchange/api/v1/account/withdraw/cancel/{withdraw-id}',

                    },
                },
            }
        }

        super().__init__(api_key, secret_key, passphrase, api_host, describe)

    def get_account(self) -> Account:
        """
        :return: The current user information and its list of sub-accounts.
        """
        account = self.private_get_accounts()
        return Account(**account)

    BalanceList = List[Balance]

    def get_balances(self, filter_zero=False) -> BalanceList:
        """
        :return: The asset information of the current user
        """
        balances = self.private_get_balances()

        return [Balance(**balance) for balance in balances if filter_zero and Utils.safe_float(balance, 'balance', 0.0) > 0]

    def get_balance(self, currency: str) -> Balance:
        """
        :param currency currency name.
        :return: The balance of an account specified by currency name.
        """

        self.check_currency(currency)
        params = {
            'currency': currency
        }

        account = self.private_get_balance(params)
        return Balance(**account)

    def get_aggregate_balance(self, filter_zero=False) -> BalanceList:
        """
        :return: The main account queries the various currencies of all sub accounts under it.
        """
        balances = self.private_get_aggregate_balance()

        return [Balance(**balance) for balance in balances if filter_zero and Utils.safe_float(balance, 'balance', 0.0) > 0]

    def get_sub_balance(self, sub_uid: str, filter_zero=False) -> BalanceList:
        """
        :return: Get Account Balance of a Sub-Account
        """
        params = {
            'sub-uid': sub_uid
        }

        balances = self.private_get_sub_balance(params)

        return [Balance(**balance) for balance in balances if filter_zero and Utils.safe_float(balance, 'balance', 0.0) > 0]

    def transfer(self, sub_uid: str, currency: str, amount: float, transfer_type: TransferType):
        """
        The main account shall perform the Asset transfer between the main and sub account.

        :param sub_uid: sub user id
        :param currency: currency name
        :param amount: quantity
        :param transfer_type: transfer type
        :return: None
        """

        self.check_currency(currency)
        params = {
            'sub-uid': sub_uid,
            'currency': currency,
            'amount': amount,
            'type': transfer_type.value,
        }

        self.private_post_transfer(params)

    def get_deposit_address(self, currency: str) -> DepositAddress:
        """
        Generate and get the currency address in zbg.

        :param currency: currency name
        :return: Currency deposit address in zbg
        """

        self.check_currency(currency)
        params = {
            'currency': currency
        }

        address = self.private_get_deposit_address(params)

        return DepositAddress(**address)

    HistoricalDepositList = List[HistoricalDeposit]

    def get_historical_deposit(self, currency: str, direct=Direct.NEXT, page=1, size=100) -> (int, int, int, HistoricalDepositList):
        """
        Search for all existed deposits and return their latest status.

        :param currency: Currency name
        :param direct: Turn records the sort direction， “prev” or “next”default is“next”
        :param page: Page，default 1
        :param size: Query record size, default 100, value [1,500]
        :return: Currency deposit records in zbg as (rows, page, size, [data1, data2...])
        """

        self.check_currency(currency)
        params = {
            'currency': currency,
            'direct': direct.value,
            'page': page,
            'size': size,
        }

        data = self.private_get_historical_deposit(params)

        return data['rows'], data['page'], data['size'], [HistoricalDeposit(**deposit) for deposit in data['list']]

    def get_withdraw_address(self, currency: str):
        """
        Get the withdrawal address of currency filled in on this platform

        :param currency: currency name
        :return: Currency withdraw addresses in zbg
        """

        self.check_currency(currency)
        params = {
            'currency': currency
        }

        addresses = self.private_get_withdraw_address(params)

        return [WithdrawAddress(**address) for address in addresses]

    HistoricalWithdrawList = List[HistoricalWithdraw]

    def get_historical_withdraw(self, currency: str, direct=Direct.NEXT, page=1, size=100) -> (int, int, int, HistoricalWithdrawList):
        """
        Search for all existed withdraws and return their latest status.

        :param currency: Currency name
        :param direct: Turn records the sort direction， “prev” or “next”default is“next”
        :param page: Page，default 1
        :param size: Query record size, default 100, value [1,500]
        :return: Currency deposit address in zbg as (rows, page, size, [data1, data2...])
        """

        self.check_currency(currency)
        params = {
            'currency': currency,
            'direct': direct.value,
            'page': page,
            'size': size,
        }

        data = self.private_get_historical_withdraw(params)

        return data['rows'], data['page'], data['size'], [HistoricalWithdraw(**deposit) for deposit in data['list']]

    def withdraw(self, currency: str, address: str, amount: float) -> str:
        """
        Create a Withdraw Request

        :param currency: Currency name
        :param address: Withdrawal address，Need to advance in the website Settings
        :param amount: Withdrawal quantity
        :return: Withdraw Id
        """

        self.check_currency(currency)
        if address is None:
            raise ArgumentsRequired("Withdraw address must not be empty.")

        if amount is None or amount <= 0:
            raise ArgumentsRequired("Withdraw amount must be greater than 0.")

        params = {
            'currency': currency,
            'address': address,
            'amount': amount,
        }

        return self.private_post_withdraw(params)

    def cancel_withdraw(self, withdraw_id: str):
        """
        Cancel a Withdraw Request

        :param withdraw_id: Withdraw ID
        :return: Withdraw ID
        """
        if withdraw_id is None:
            raise ArgumentsRequired("Withdraw id must not be empty.")

        params = {
            'withdraw-id': withdraw_id,
        }

        return self.private_post_cancel_withdraw(params)
