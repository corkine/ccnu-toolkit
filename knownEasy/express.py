#!/usr/bin/env python3
VERSION = "0.0.2-sig"
import requests, json, time, traceback

WEBHOOK_URL = "https://hooks.slack.com/services/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"
EXPRESS_URL = "https://api.jisuapi.com/express/query"

def post_message(text) -> None:
	payload = {
		"text": text
	}
	r = requests.post(WEBHOOK_URL, data = json.dumps(payload))
	if r.text != 'ok': raise RuntimeError("信息发送失败")

def get_info(number:str,_type:str="auto") -> dict:
	if number == None: raise RuntimeError("单号不应该为空")
	params = {
		"appkey":"6be28a549bc9b79f",
		"type":_type,
		"number":number
	}
	r = requests.get(EXPRESS_URL, params=params)
	return r.json()


def handle_express(number:str, sleep:int):
	print("启动查询序列...")
	ISSIGN = 0
	MEM_CACHE = ""
	
	while ISSIGN != 1:
		try:
			##获取此单号信息
			data = get_info(number)
			MSG = data["msg"]
			ISSIGN = data["result"]["issign"]
			NUMBER = data["result"]["number"]
			LAST_TRACE = data["result"]["list"][0]
			LAST_TIME = LAST_TRACE["time"]
			LAST_STATUS = LAST_TRACE["status"]
			
			message = "快递 [%s] 最后状态：\n%s %s"%(NUMBER, LAST_TIME, LAST_STATUS)
			
			##存在更新
			if message != MEM_CACHE:
				MEM_CACHE = message
				print("发送更新到 Slack 频道")
				post_message(message)
			##没有更新
			else:
				pass
		except Exception as e:
			print("发生错误：%s"%(MSG))
			print("原始报文：%s"%str(data))
			print(traceback.format_exc())
		finally:
			## 如果尚未结束此单，120s 轮询查询，否则，立即终止
			if not ISSIGN: time.sleep(sleep)
	print("查询序列结束...")


if __name__ == "__main__":
	import argparse
	p = argparse.ArgumentParser(prog='快递更新查询推送程序',
		description="解析快递信息，当其有新消息时，发送到 Slack")
	p.add_argument("number", help="快递单号", type=str)
	p.add_argument("-s","--slack",  dest="slack", help="Slack WebHook URL", type=str)
	p.add_argument("-t","--time", dest="time", help="轮询时长，单位为 s", default=120, type=int)
	args = p.parse_args()
	if args.slack != None:
		WEBHOOK_URL = args.slack
	handle_express(number=args.number, sleep=args.time)
