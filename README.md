这里存放着我在华中师范大学期间，用来简化信息访问而编写的一些小脚本(Java、Scala、Python、TypeScript、JavaScript etc)。所有代码按照 Apache License v2 开源。

## 1. cardCheck

此模块依赖于 Python 3 环境，用于自动登录中心验证系统，检查校园卡余额。

### card.py

此脚本支持使用 `python card.py 学号 密码 开始日期 结束日期` 用来查询校园卡消费情况。


    Usage: 输入统一身份认证登陆学号、密码、查询开始时间、结束时间、
    每次查询数目即可查询(如果出错过多则降低速度，API 设计为 10 条/次)，比如

    查询指定日期，速度自定义 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01 30
    查询指定日期，速度为 90 条/页 --> python card.py 2017110666 3333332 2016-01-01 2019-01-01
    查询从 2010 - 2020 所有日期，速度为 90 条/页 --> python card.py 2017110666 3333332
    查询本地文件 result.json --> python card.py

> 支持环境: Python 3.5 及以上，安装 BeautifulSoup 4、Requests 包

### auth.py

此脚本用于登陆华中师范大学统一认证系统。

> 支持环境: Python 3.5 及以上，安装 BeautifulSoup 4、Requests 包


## 2. autoLogin

CCNU 校园网自动登陆，程序依赖于 JVM（JRE 或者 JDK）环境，已编译。

### 使用方法

下载 dst 目录下的 jar 文件和 yml 配置文件，修改 yml 配置，然后在命令行运行：

```bash
java -jar autoLogin.jar
```

即可自动测试登录。

> 在 Windows 环境下，加壳的程序可运行 dst/autoLogin.exe 执行，其仍然需要 Java 环境，且位于系统 PATH 路径下。

### 配置说明

一般而言，只需要修改 USERNAME_VALUE 和 PASSWORD_VALUE 即可。需要注意，USER_NAME 需要添加 @运营商后缀。

NETWORK_VALUE 按照校园网、电信、移动、联通 依次为 0 1 2 3

```yaml
CHECK_URL: "http://www.ccnu.edu.cn" #测试网络连通地址
LOGIN_URL: "http://10.220.250.50" #登录地址
USERNAME: "DDDDD" #登录表单用户名 name
PASSWORD: "upass" #登录表单密码 name
NETWORK: "suffix" #登陆表单运营商 name
USERNAME_VALUE: "realAccount@cmcc" #修改为 你的用户名@运营商 格式
PASSWORD_VALUE: "realPassword" #修改为你的密码
NETWORK_VALUE: "2" #修改为你的运营商
SUCCESS_CHECK: "您已登录成功，欢迎使用！请不要关闭本页。" #用来判断登录成功地址是否包含此字符串
INDEX_CHECK: "鄂ICP备05003325号-1" #用来判断网络连通地址是否包含此字符串
RETRY: 5 #重试次数，可自由修改，Integer 类型，正整数，大于 0
SLEEP: 5 #每次重试间隔秒数，可自由修改，Integer 类型，正整数
```

## 3. questionnaireRecorder

此项目用于进行心理学问卷快速录入，其在之前博客的 Python + Qt 实现的基础上，扩展了适用性，使用 Scala + JavaFx 实现，现版本系统可针对大多数任意需求的问卷进行快速录入。

### 程序运行示例

![](http://static2.mazhangjing.com/20190717/f4ec269_2019-07-1721.25.35.gif)

注意，图中的文本框、组别、输入的长度、类型限制，都是基于 config.yaml 配置反射创建的，修改此文件可配置自定义组与组要求的问卷录入。

修改需按照 yaml 语法格式编写配置，提供组的相关信息（其中限制输入的类型、字符长度最为重要，没有字符限制则无法自动完成此组数据录入）。

程序提供较为自动化的组输入完成检测、提供调到下一组、输入未完成全选重新输入、完全输入完成后保存并继续且重设焦点、数据的 CSV 格式输出、数据的查看与修改。

### 下载与说明 

程序提供二进制可运行文件 `/questionnaireRecorder/dst/recorder.jar` (需要运行在 JDK 8 环境下，依赖 JavaFx 8 包)

> 源代码遵循 GPL v2 协议开源，不得商用或者修改后闭源使用，违反规则使用程序面临相关的法律责任。



