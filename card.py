import requests
import json
import auth

__AUTHOR__ = "Corkine Ma @ CCNU"
__VERSION__ = "0.0.3"
__LOG__ = "0.0.3 更新分片获取数据功能，超时自动重连，增强系统稳定性"

def card_data(username:str, password:str, token: str, start:str, end:str, max_limit:int) -> (dict, str):
    """获取校园卡信息，首先获取页数，然后分页获取，当分页出错时，自动重新验证，然后重新获取，
    最后返回所有结果：Python 和 Json 格式"""
    total, _ = card_page(1, token, start, end, max_limit)
    res = []
    print("一共有 %s 页"%total)
    for i in range(total):
        ok = False
        use_token = token
        while not ok:
            print("正在获取第 %s 页"%(i+1))
            try:
                _, n_res = card_page(i+1, use_token, start, end, max_limit)
                ok = True
            except Exception as e:
                print("在获取第 %s 页出现问题，准备更新 Token... "%(i+1))
                _, use_token = auth.auth_session(username, password)
                print("已更新 Token 信息")
        res += n_res
    import json
    return res, json.dumps(res)

def card_page(page_number:int, token: str, start:str, end:str, max_limit:int) -> (int, list):
    """分页读取校园卡信息，返回总页数和当前页条目"""
    query_post_url = "http://one.ccnu.edu.cn/ecard_portal/query_trans"
    data = {
        "limit": str(max_limit),
        "page": page_number,
        "tranType": "", 
        "start": start,
        "end": end
    }

    r = requests.post(query_post_url, headers={
        "Authorization": "Bearer " + token,
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": "PORTAL_TOKEN=" + token,
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36"
    }, params = data)

    data = json.loads(r.text) # NT 系统下中文字符串 json 化出错
    if not "result" in data or not "total" in data["result"] or not "rows" in data["result"]:
        print("\tData:", data)
        raise RuntimeError("结果应该存在，因为接口不稳定，可能获取到 0，多次重试即可")
    return int(data["result"]["total"]), data["result"]["rows"]


def write_to(file_name:str, content:str):
    """将数据写入文件"""
    file = open(file_name, "w",encoding='utf-8')
    file.write(content)
    file.close()

def count(file_name:str):
    """统计指定文件中的消费和充值数据"""
    file = open(file_name,"r",encoding='utf-8')
    lines = file.readlines()[0]
    file.close()

    import json
    rows = json.loads(lines)

    custome_list = []
    custom_money = 0.0
    in_list = []
    in_money = 0.0

    for row in rows:
        if row["dealName"] == "消费":
            custome_list.append(row)
            custom_money += row["transMoney"]
        elif "充值" in row["dealName"]:
            in_list.append(row)
            in_money += row["transMoney"] 

    print("一共条数", len(rows))
    print("总共消费 %.3f"%custom_money, "一共", len(custome_list), "笔记录")
    print("总共充值 %.3f"%in_money, "一共", len(in_list), "笔记录")

def ctl_run(username:str, password:str, start:str="2016-01-01", end:str="2020-01-01", 
            mix_limit:int=90, just_count:bool=False, json_file:str="result.json"):
    """命令行运行程序调用入口，用于认证并获取数据，添加了命令行打印和错误处理"""
    if not just_count:
        ok = False
        print("正在尝试登陆系统")
        _, token = auth.auth_session(username, password)
        while not ok:
            try:
                print("正在获取数据")
                _, content = card_data(username, password, token, start, end, mix_limit)
                ok = True
            except Exception as e:
                print("获取数据时发生错误，正在重试",str(e))
        print("获取到数据，将数据写入到 %s 中"%json_file)
        write_to(json_file, content)
    print("\n数据统计如下 ========================\n")
    count(json_file)
    

if __name__ == "__main__":
    # from config import test_end, test_start, test_username, test_password, test_filename
    # ctl_run(test_username, test_password, test_start, test_end, just_count=False)
    from sys import argv
    print("Usage: 输入统一身份认证登陆学号、密码、查询开始时间、结束时间、每次查询数目即可查询(如果出错过多则降低速度，API 设计为 10 条/次)，比如\n")
    print("查询指定日期，速度自定义 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01 30")
    print("查询指定日期，速度为 90 条/页 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01")
    print("查询从 2010 - 2020 所有日期，速度为 90 条/页 --> python card.py 2017110666 3333332")
    print("查询本地文件 result.json --> python card.py\n")
    if len(argv) == 6:
        ctl_run(argv[1], argv[2], argv[3], argv[4], int(argv[5]))
    elif len(argv) == 5:
        ctl_run(argv[1], argv[2], argv[3], argv[4])
    elif len(argv) == 3:
        ctl_run(argv[1],argv[2])
    elif len(argv) == 1:
        ctl_run("","",just_count=True)


