#!/usr/bin/env python3
__VERSION__ = "0.0.3-sig"
__REQUIRE__ = "lxml,bs4"
__AUTHOR__ = "Corkine Ma"
__DATE__ = "2019年08月12日"

import requests, json, time, traceback
from bs4 import BeautifulSoup
from random import random

WEBHOOK_URL = "https://hooks.slack.com/services/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"
SLEEP_IN = 3
TITLE_LIST = []

FIND = [
	"http://faxian.smzdm.com/h0s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p6/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p6/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p6/#filter-block"
]
FIND_0 = [
	"http://faxian.smzdm.com/h0s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h0s0t0f0c5p6/#filter-block"
]
FIND_3 = [
	"http://faxian.smzdm.com/h2s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h2s0t0f0c5p6/#filter-block"
]
FIND_12 = [
	"http://faxian.smzdm.com/h3s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h3s0t0f0c5p6/#filter-block"
]
FIND_24 = [
	"http://faxian.smzdm.com/h4s0t0f0c5p1/#filter-block",
	"http://faxian.smzdm.com/h4s0t0f0c5p2/#filter-block",
	"http://faxian.smzdm.com/h4s0t0f0c5p3/#filter-block",
	"http://faxian.smzdm.com/h4s0t0f0c5p4/#filter-block",
	"http://faxian.smzdm.com/h4s0t0f0c5p5/#filter-block",
	"http://faxian.smzdm.com/h4s0t0f0c5p6/#filter-block"
]
FIND_TEST = "http://faxian.smzdm.com/h4s0t0f0c5p1/#filter-block"

def post_message(text) -> None:
	payload = {
		"text": text
	}
	r = requests.post(WEBHOOK_URL, data = json.dumps(payload))
	if r.text != 'ok': raise RuntimeError("信息发送失败")

def handle_search(url_list:list, check_sleep:int, random_sleep, filter):
	print("启动商品价格搜索序列...")
	mem_items = []
	while True:
		try:
			datas = get_info_with_filter(url_list, random_sleep, filter)
			if len(datas) != 0:
				for data in datas:
					if data in mem_items: continue
					message = "[好价] %s %s <%s|查看>"%data
					print("Sending Message to Slack %s"%message)
					post_message(message)

					mem_items.append(data)
					if len(mem_items) > 500: 
						mem_items = mem_items[-10:]
		except Exception as e:
			print("发生错误:")
			print(traceback.format_exc())
		finally:
			time.sleep(check_sleep)
	print("商品价格搜索序列结束...")

def get_info(url:str) -> list:
	headers = {
		"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
		"Accept-Encoding": "gzip, deflate, br",
		"Accept-Language":"zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
		"Cache-Control": "max-age=0",
		"Connection": "keep-alive",
		#"Cookie": "__ckguid=qBQ6B4w4qa3fFpu7IoHoLJ3; device_id=213070643315588523438752182f36198d73100619dae204d2c3cc882d; homepage_sug=j; PHPSESSID=983509a373c791d031934ab9d7cd4b32; _ga=GA1.2.203063312.1558852346; wt3_sid=%3B999768690672041; r_sort_type=score; smzdm_user_source=B26F29DF2B7F3C73A3943A0512B12C4F; __gads=ID=6a354d15813ba5ac:T=1564031512:S=ALNI_MbOXaYpOetbOS40Y0QGox9xvVNMMA; __jsluid_s=c2712c0dab29518cd5146d432625b3e7; userId=9211504065; sess=NWMxMDR8MTU2OTIzODI3M3w5MjExNTA0MDY1fGI5Nzc2NzBhZWU3NDBiZWFlMWFiMjQyOTA4MWIyYmIy; user=user%3A9211504065%7C9211504065; smzdm_id=9211504065; smzdm_user_view=4F337A26EAA902D5BBC670DD3022E9B6; _gid=GA1.2.227065644.1565240905; ss_ab=ss58; wt3_eid=%3B999768690672041%7C2155885240200774531%232156558401400529332; _zdmA.uid=ZDMA.zhwfl-YVV.1565585998.2419200; s_his=gram%2Cairdots%20ios%2Cipad%2Cusb%20c%20a%2Cusb%20c%20a%20ota%2Ct1s%2Ct3%2C%E5%B0%8F%E7%88%B1%E9%9F%B3%E7%AE%B1play%2C%E5%B0%8F%E7%88%B1%E8%A7%A6%E5%B1%8F%E9%9F%B3%E7%AE%B1%2Cfcwm; __jsluid_h=0df2f80260ffa6890d71e689cf28fcda; zdm_qd=%7B%22referrer%22%3A%22http%3A%2F%2Finfo.mazhangjing.com%2Fsmzdm%22%7D; Hm_lvt_9b7ac3d38f30fe89ff0b8a0546904e58=1563876385,1565586725,1565586733,1565587169; jisufa_tip=known; Hm_lpvt_9b7ac3d38f30fe89ff0b8a0546904e58=1565587204",
		"DNT": "1",
		"Host": "faxian.smzdm.com",
		#"Referer": "http://info.mazhangjing.com/smzdm",
		#"Upgrade-Insecure-Requests": "1",
		"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36"
	}
	r = requests.get(url, headers=headers)
	soup = BeautifulSoup(r.text,"lxml")
	# if "操作太频繁，请稍后再试" in r.text:
	# 	print("System Security Check Failed!")
		#raise RuntimeError("System Security Check Failed")
	print("Getting Information from %s"%soup.title.text)
	divs = soup.find_all("div", class_="feed-block-ver")
	#print("Getting Item Count %s"%len(divs))
	items = []
	for i in divs:
		title_link = i.h5.a
		title = title_link.text
		link = title_link["href"]
		price = i.find(class_="z-highlight").text
		items.append((title, price, link))
	return items

def get_info_with_filter(urls:list, sleep_func, filter) -> list:
	find = []
	for url in urls:
		print("Finding Information in %s"%url)
		datas = get_info(url)
		#print("Datas", datas)
		sleep = sleep_func()
		print("Sleeping for Security Check %s"%sleep)
		time.sleep(sleep)
		for data in datas:
			if filter(data): 
				print("Find Item %s"%str(data))
				find.append(data)
	print("Filter Result Count %s"%len(find))
	return find

def simple_title_filter(tup) -> bool:
	title = str(tup[0]).upper()
	if "IPAD" in title and ("膜" in title or "套" in title): return True

	for need_item in TITLE_LIST:
		need_item_u = str(need_item).upper()
		if need_item_u in title: return True

	return False

def random_sleep():
	return int(random() * SLEEP_IN)

if __name__ == "__main__":
	import argparse
	p = argparse.ArgumentParser(prog='什么值得买低价过滤程序',
	 	description="查找心仪商品，当存在时，发送到 Slack")
	p.add_argument("keywords", help="商品标题关键词, 使用 | 隔开", type=str)
	p.add_argument("-s","--slack",  dest="slack", help="Slack WebHook URL", type=str)
	p.add_argument("-t1","--time1", dest="time1", help="轮询时长，单位为 s", default=300, type=int)
	p.add_argument("-t2","--time2", dest="time2", help="每页查询间隔时长，单位为 s", type=int)
	args = p.parse_args()
	if args.slack != None:
		WEBHOOK_URL = args.slack
	if args.time2 != None:
		SLEEP_IN = args.time2
	keywords = []
	for keyword in str(args.keywords).split("|"):
		keywords.append(keyword.strip())
	TITLE_LIST.extend(keywords)
	print("System will find Those Keywords %s, and will send message to %s, 轮询 in %ss, 查询 in %ss" \
		%(keywords, WEBHOOK_URL, args.time1, SLEEP_IN))
	print("Initilizing Action Sequence...")
	handle_search(FIND, args.time1, random_sleep, simple_title_filter)
	print("Ending Action Sequence...")
