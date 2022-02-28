(function (win, axios) {
    // gateway interceptors可以配置在common.js中提供出来使用,提供了一个CommonUtil.js参考,估计不能直接导入使用
    // 请求服务器的网关
    // win.gateway = "http://127.0.0.1:9999";
    // win.gateway = "http://gateway.mzlalal.icu";
    win.gateway = "http://" + location.host;
    // 客户端ID
    win.clientKey = "fantasy-oauth2";
    // https://www.axios-http.cn/docs/interceptors 拦截器文档
    // 添加请求拦截器
    axios.interceptors.request.use(function (config) {
        // 发送请求之前可以对config再次处理
        // 判断是否是HTTP开头,若不是则增加gateway地址
        if (config.url && !config.url.startsWith("http")) {
            // 在这里对URL进行拦截,增加gateway地址
            config.url = win.gateway + config.url;
        }
        // 增加TOKEN头
        if (!config.headers['F-Authorization']) {
            config.headers['F-Authorization'] = localStorage.getItem("user.access.token");
        }
        // 继续往下执行
        return config;
    }, function (error) {
        // 请求错误直接使用catch方法
        return Promise.reject(error);
    });

    // 添加响应拦截器
    axios.interceptors.response.use(function (res) {
        // 2xx 范围内的HTTP状态码都会触发该函数
        // res.data存在并且返回的state(业务状态)状态为200才能够继续执行,否则直接执行error
        // 避免了then方法中每次都需要判断state=200
        if (res.data && res.data.state === 200) {
            // 传递接口返回的结果给then方法
            return res.data;
        }
        // 需要重定向到登录界面的方法
        let redirectState = [411, 412, 413, 414]
        // 避免了then方法中每次都需要判断state=200
        if (res.data && redirectState.includes(res.data.state)) {
            // 1.5s后跳转到登录页
            setTimeout(() => {
                window.location = res.data.data;
            }, 1500)
            // 业务状态错误直接使用catch方法
            return Promise.reject(res);
        }
        // 业务状态错误直接使用catch方法
        return Promise.reject(res);
    }, function (error) {
        // 超出 2xx 范围的HTTP状态码都会触发该函数
        // 请求错误直接使用catch方法
        console.log("请求接口出错");
        console.log(error);
        return Promise.reject(error);
    });

    // 暴露commonUtil
    win.commonUtil = {
        // 获取URL上的参数
        getUrlPara: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
        },
        // 获取res的pageInfo中的集合,若为空则返回[]空数组
        getPageList: function (res) {
            if (res && res.page && res.page.list) {
                return res.page.list;
            }
            return [];
        }
    }

    // 事件
    win.hiddenProperty = 'hidden' in document ? 'hidden' : 'webkitHidden' in document ? 'webkitHidden' : 'mozHidden' in document ? 'mozHidden' : null;
    win.visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
    // 事件处理方法
    win.onVisibilityChange = function () {
        if (!document[hiddenProperty]) {
            // 页面激活
            console.log("the current window is activated")
            if (typeof (windowActivatedFunction) === 'function') {
                windowActivatedFunction();
            }
        } else {
            console.log("the current window is deactivated")
            // 页面失活
            if (typeof (windowDeactivatedFunction) === 'function') {
                windowDeactivatedFunction();
            }
        }
    };
    // 当前窗口得到焦点
    document.addEventListener(visibilityChangeEvent, onVisibilityChange);

    // 禁止双指放大
    document.documentElement.addEventListener('touchstart', function (event) {
        if (event.touches.length > 1) {
            event.preventDefault();
        }
    }, false);

    // 禁止双击放大
    let lastTouchEnd = 0;
    document.documentElement.addEventListener('touchend', function (event) {
        const now = Date.now();
        if (now - lastTouchEnd <= 300) {
            event.preventDefault();
        }
        lastTouchEnd = now;
    }, false);
})(window, window.axios);

// 时间增加Format格式化函数
Date.prototype.Format = function (fmt) {
    var o = {
        // 月份
        "M+": this.getMonth() + 1,
        // 日
        "d+": this.getDate(),
        // 小时
        "h+": this.getHours(),
        // 分
        "m+": this.getMinutes(),
        // 秒
        "s+": this.getSeconds(),
        // 季度
        "q+": Math.floor((this.getMonth() + 3) / 3),
        // 毫秒
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}