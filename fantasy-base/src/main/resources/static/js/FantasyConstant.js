(function (window) {
    // 请求服务器的网关
    window.gateway = "http://" + location.host;
    // 默认跳转页
    window.defaultRedirectUri = "/fantasy-oauth2/mi-ma-deng-lu.html";
    // 客户端Key
    window.clientKey = "fantasy-oauth2";
    // 需要刷新令牌的的状态码
    window.refreshTokenStateCode = [1404, 1405];
})(window);