#!/usr/bin/env python3
VERSION = "0.0.1-sig"
import requests, json, time, traceback
from random import random
from bs4 import BeautifulSoup

WEBHOOK_URL = "https://hooks.slack.com/services/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"
TEST_URL = "https://apps.apple.com/cn/app/goodnotes-4/id778658393"
SLEEP_IN = 3

URL_LIST = [
	"https://apps.apple.com/cn/app/goodnotes-5/id1444383602",
]

def get_price(url:str):
	headers = {
		"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
		"Accept-Encoding": "gzip, deflate, br",
		"Accept-Language":"zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
		"Cache-Control": "max-age=0",
		"Connection": "keep-alive",
		"DNT": "1",
		"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36"
	}
	r = requests.get(url, headers = headers)
	soup = BeautifulSoup(r.text,'lxml')
	info = soup.find("header", class_="product-header")
	title = info.find("h1", class_="product-header__title").text.strip().split("\n")[0]
	price = info.find("li",class_="app-header__list__item--price").text
	return title, price, url

def post_message(text) -> None:
	payload = {
		"text": text
	}
	r = requests.post(WEBHOOK_URL, data = json.dumps(payload))
	if r.text != 'ok': raise RuntimeError("信息发送失败")

def get_prices(urls:list, sleep_func) -> list:
	result = []
	for url in urls:
		data = get_price(url)
		result.append(data)
		sleep = sleep_func()
		time.sleep(sleep)
	return result

def handle_check(urls:list, check_sleep:int, item_sleep):
	print("启动价格查询序列...")
	mem_list = []

	while True:
		try:
			new_prices = []
			new_datas = get_prices(urls, item_sleep)
			## 对于每条新数据，遍历
			for new_data in new_datas:
				## 如果存在变化或者不存在
				if not new_data in mem_list:
					title = new_data[0]
					old_item = None
					# 找到旧项目
					for mem_item in mem_list:
						if title == mem_item[0]:
							old_item = mem_item
							break
					## 删除旧项目
					if old_item != None:
						mem_list.remove(old_item)
					## 更新内存和消息
					mem_list.append(new_data)
					new_prices.append(new_data)

			if len(new_prices) != 0:
				print("发现存在数据更新，启动消息发送序列...")
				for item in new_prices:
					message = "[APP] %s 价格发生变动，当前价格： %s <%s|查看>"%item
					print("发现更新：%s"%message)
					post_message(message)
				
		except Exception as e:
			print("发生错误：")
			print(traceback.format_exc())
		finally:
			time.sleep(check_sleep)
	print("价格查询序列结束...")

def simple_time():
	return int(random() * SLEEP_IN)

if __name__ == "__main__":
	import argparse
	p = argparse.ArgumentParser(prog='AppStore 价格监测程序',
	 	description="监测 App Store 价格变化，当其发生改变，则推送通知到 Slack")
	p.add_argument("-s","--slack",  dest="slack", help="Slack WebHook URL", type=str)
	p.add_argument("-t1","--time1", dest="time1", help="轮询时长，单位为 s", default=1000, type=int)
	p.add_argument("-t2","--time2", dest="time2", help="单个查询间隔时长，单位为 s", type=int)
	args = p.parse_args()
	if args.slack != None:
		WEBHOOK_URL = args.slack
	if args.time2 != None:
		SLEEP_IN = args.time2
	print("Checking with args", args)
	handle_check(URL_LIST, args.time1, simple_time)