import requests
from bs4 import BeautifulSoup

## 华中师范大学中央系统认证
__AUTHOR__ = "Corkine Ma @ CCNU"
__VERSION__ = "0.0.1"
__LOG__ = "0.0.1 针对 JSESSIONID 和 PORTAL_TOKEN 进行处理"

def auth_session(username:str, password:str) -> (requests.Session, str):
    """自动通过用户名和密码认证登陆中央服务器，返回 Session 对象以及 PORTAL_TOKEN 密钥"""
    basic_uri = "https://account.ccnu.edu.cn"
    basic_uri_http = "http://account.ccnu.edu.cn"
    test_uri = "http://one.ccnu.edu.cn"
    log_to = "/cas/login"
    post_url = basic_uri + log_to
    post_url_http = basic_uri_http + log_to

    _r = requests.get(post_url)
    _html = BeautifulSoup(_r.content, "html.parser")
    _lt = str(_html.select("input[name='lt']")[0]["value"])
    JSESSIONID = _r.cookies["JSESSIONID"]
    #DISTINCTID = "16b5eeee67d4d6-0d4b1ca6296dfd-37657e03-1fa400-16b5eeee67e73c"

    s = requests.Session()
    r = s.post(post_url, headers= {
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
        "Accept-Encoding": "gzip, deflate, br",
        "Accept-Language": "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "Cache-Control": "max-age=0",
        "Connection": "keep-alive",
        "Content-Length": "157",
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": "CASPRIVACY=; CASTGC=; JSESSIONID="+ JSESSIONID + "; UM_distinctid=;",
        "DNT": "1",
        "Host": "account.ccnu.edu.cn",
        "Origin": "https://account.ccnu.edu.cn",
        "Referer": "https://account.ccnu.edu.cn/cas/login?service=http%3A%2F%2Fone.ccnu.edu.cn%2Fcas%2Flogin_portal",
        "Upgrade-Insecure-Requests": "1",
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36",
    }, params={
        "service": "http://one.ccnu.edu.cn/cas/login_portal"
    }, data= {
        "username": username,
        "password": password,
        "lt": _lt,
        "execution": "e1s1",
        "_eventId": "submit",
        "submit": "登录"
    })

    assert ("您已经成功登录中央认证系统。" in str(r.text))
    assert (r.status_code == 200)

    tr = s.get(test_uri)
    assert "PORTAL_TOKEN" in tr.cookies
    return (s, tr.cookies["PORTAL_TOKEN"])

if __name__ == "__main__":
    from config import test_username, test_password
    auth_session(test_username, test_password)