# CCNU-TOOLKIT

这里存放着我在华中师范大学期间，用来简化信息访问而编写的一些小脚本。所有代码按照 Apache License v2 开源。

## card.py

此脚本支持使用 `python card.py 学号 密码 开始日期 结束日期` 用来查询校园卡消费情况。


    Usage: 输入统一身份认证登陆学号、密码、查询开始时间、结束时间、
    每次查询数目即可查询(如果出错过多则降低速度，API 设计为 10 条/次)，比如

    查询指定日期，速度自定义 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01 30
    查询指定日期，速度为 90 条/页 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01
    查询从 2010 - 2020 所有日期，速度为 90 条/页 --> python card.py 2017110666 3333332
    查询本地文件 result.json --> python card.py

> 支持环境: Python 3.5 及以上，安装 BeautifulSoup 4、Requests 包

## auth.py

此脚本用于登陆华中师范大学统一认证系统。

> 支持环境: Python 3.5 及以上，安装 BeautifulSoup 4、Requests 包
