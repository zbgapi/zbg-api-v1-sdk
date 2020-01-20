import zbg

if __name__ == '__main__':
    account = zbg.AccountApi()
    symbols = account.get_symbols()
    print(symbols[0].base_currency)


