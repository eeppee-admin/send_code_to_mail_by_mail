# 使用自己的QQ的SMTP服务来对外发送邮箱验证码

# 配置好你自己的config.yaml
# 启动localhost redis server(必须)

# go run main.go, 默认8080，如果需要内网穿透，可以使用cpolar
- cpolar http 8080 做内网穿透

# 提供的api组
```js
### 发送验证码
POST http://localhost:8080/send-code?email=1876056356@qq.com
### 验证验证码, 738702 是验证码，有发送验证码获取，随机的
POST http://localhost:8080/verify-code?email=1876056356@qq.com&code=6271
```

# todo 如果是给app使用的话，建议接口返回形式为
```json
{
    "code": 0,
    "msg": "接口返回消息",
    "data": "数据可有可无"
}
```
- 对app来说, 接口成功建议返回code为0

# 更多信息看Makefile和test.http
- all code gen by ai