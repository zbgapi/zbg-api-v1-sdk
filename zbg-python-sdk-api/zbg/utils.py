import collections
import time
import urllib.parse as _urlencode


class Utils:
    @staticmethod
    def safe_float(dictionary, key, default_value=None):
        try:
            if isinstance(dictionary, list) and isinstance(key, int) and len(dictionary) > key:
                value = float(dictionary[key])
            else:
                value = float(dictionary[key]) if (key is not None) and (key in dictionary) and (dictionary[key] is not None) else default_value
        except ValueError:
            value = default_value
        return value

    @staticmethod
    def safe_string(dictionary, key, default_value=None):
        try:
            if isinstance(dictionary, list) and isinstance(key, int) and len(dictionary) > key:
                value = str(dictionary[key])
            else:
                value = str(dictionary[key]) if (key is not None) and (key in dictionary) and (dictionary[key] is not None) else default_value
        except ValueError:
            value = default_value
        return value

    @staticmethod
    def safe_integer(dictionary, key, default_value=None):
        value = Utils.safe_string(dictionary, key)
        try:
            return int(value) if value else default_value
        except ValueError:
            return default_value

    @staticmethod
    def index_by(array, key):
        result = {}
        if type(array) is dict:
            array = Utils.keysort(array).values()
        for element in array:
            if (key in element) and (element[key] is not None):
                k = element[key]
                result[k] = element
        return result

    @staticmethod
    def keysort(dictionary):
        return collections.OrderedDict(sorted(dictionary.items(), key=lambda t: t[0]))

    @staticmethod
    def milliseconds():
        return int(time.time() * 1000)

    @staticmethod
    def url_encode(params={}):
        if (type(params) is dict) or isinstance(params, collections.OrderedDict):
            return _urlencode.urlencode(params)
        return params