
# Fantasy Oauth2

## 授权服务
- 应用授权  
> 本服务作为用户中心对对接应用进行授权,用户可以根据使用情况给应用授权信息

- 无缝TOKEN  
> 一次登录获取,无缝请求多个应用


## 对接
1. 创建client,需要设置clientKey,clientSecret,redirect_uri(重定向地址)
2. 前端在登录时请求授权登录界面  
 http://oauth2.mzlalal.icu/oauth/authorize?response_type=code&client_id=XXX
> 此时会跳转到统一登录界面 http://mzlalal.icu/login.html 
3. 使用正确的用户名密码登录,登录成功后继续访问授权界面
4. 进行授权后回调上述配置的redirect_uri
5. 根据返回至redirect_uri的参数code请求后端/oauth/callback接口即可获取TOKEN