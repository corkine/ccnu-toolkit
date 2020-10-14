#/usr/bin/env python3
import requests
from time import sleep

def show(content="正在尝试登录 CCNU-ChinaNet 系统"):
    print(">>>>>>>>>>>>>>>>>>>>>> %s \n"%content)
def success(content=""):
    print("==============================================\n\n[成功] %s\n\n==============================================\n"%content)
def failed(content=""):
    print("==============================================\n\n[失败] %s\n\n==============================================\n"%content)
def check():
    return "<img src=//www.baidu.com/img/gs.gif>" in requests.get("http://www.baidu.com").text

def doTry():
    try:
        if check():
            success("无需登录，可访问网络。")
            return True
        else:
            requests.post(url,data=data)
            if check():
                success("已经成功认证，可访问网络。")
                return True
            else:
                failed("认证失败，不可访问网络。")
                return False
    except Exception as e:
        failed("遇到程序错误: %s"%(str(e)))
        return False
        

if __name__ == "__main__":
    url = "http://securelogin.arubanetworks.com/auth/index.html/u"
    data = {
                "user": "20xxxxxx@chinanet",
                "password": "xxxxx32"
            }
    show()
    count = 1
    while count < 5:
        print("Try for %s time."%count)
        if doTry() == True:
            break
        else:
            count += 1
            sleep(3)
    show("Ending Sequence. 程序退出。")
    sleep(5)