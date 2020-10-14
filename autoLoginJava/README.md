# CCNU 校园网自动登陆

## 使用方法

下载 dst 目录下的 jar 文件和 yml 配置文件，修改 yml 配置，然后在命令行运行：

```bash
java -jar YourJarFilePathAndName.jar
```

即可自动测试登录。

## 配置说明

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

