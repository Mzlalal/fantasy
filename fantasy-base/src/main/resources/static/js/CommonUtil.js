(function (window, axios) {
    // https://www.axios-http.cn/docs/interceptors 拦截器文档
    // 请求拦截器
    axios.interceptors.request.use(function (config) {
        // 发送请求之前可以对config再次处理
        // 判断是否是HTTP开头,若不是则增加gateway地址
        if (config.url && !config.url.startsWith("http")) {
            // 在这里对URL进行拦截,增加gateway地址
            config.url = window.gateway + config.url;
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

    // 响应拦截器
    axios.interceptors.response.use(async function (res) {
        // res.data存在并且返回的state(业务状态)状态为200才能够继续执行,否则直接执行error
        // 避免了then方法中每次都需要判断state=200
        if (res.data && res.data.state === 200) {
            // 传递接口返回的结果给then方法
            return res.data;
        }
        // 需要刷新令牌的的状态码
        if (res.data && window.refreshTokenStateCode.includes(res.data.state)) {
            // 获取刷新令牌
            let refreshToken = localStorage.getItem("user.refresh.token");
            if (!refreshToken) {
                // 无刷新令牌,跳转到登录页
                window.commonUtil.redirectDefaultUri();
                return;
            }
            let param = {
                "clientKey": window.clientKey,
                "refreshToken": refreshToken
            }
            // 请求 await同步
            let refreshTokenRes = await axios({
                method: "post",
                url: "/fantasy-oauth2/api/v1/oauth/refresh.token",
                data: param
            })
            if (refreshTokenRes.data && refreshTokenRes.data.accessToken && refreshTokenRes.data.refreshToken) {
                // 保存用户令牌,用户刷新令牌
                localStorage.setItem("user.access.token", refreshTokenRes.data.accessToken);
                localStorage.setItem("user.refresh.token", refreshTokenRes.data.refreshToken);
            } else {
                // 刷新令牌失败,跳转到登录页
                window.commonUtil.redirectDefaultUri();
                return;
            }
            // 业务状态错误直接使用catch方法
            return axios.request(res.config);
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

    // 防抖动的公共定时器
    let timer = null;
    // 暴露commonUtil
    window.commonUtil = {
        // 防抖动
        debounce: function (fn, wait) {
            // 如果不为空则清除
            if (timer !== null) {
                clearTimeout(timer);
            }
            // 设置新的定时器
            timer = setTimeout(fn, wait);
        },
        // 获取URL上的参数
        getUrlPara: function (name) {
            const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            const r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
        },
        // 获取res的pageInfo中的集合,若为空则返回[]空数组
        getPageList: function (res) {
            if (res && res.page && res.page.list) {
                return res.page.list;
            }
            return [];
        },
        // 获取res的pageInfo中的totalPage,若为空则返回0
        getTotalPage: function (res) {
            if (res && res.page && res.page.totalPage) {
                return res.page.totalPage;
            }
            return 0;
        },
        // 验证令牌
        checkToken: function () {
            // 请求接口
            axios({
                method: "post",
                url: "/fantasy-oauth2/api/v1/token/check.token",
            }).then(res => {
                if (res.data) {
                    localStorage.setItem("user.info", JSON.stringify(res.data));
                }
                // 获取首页
                let clientIndexUri = localStorage.getItem("client.index.uri");
                // 跳转
                window.location = (clientIndexUri.startsWith("http") ? "" : window.gateway) + clientIndexUri;
            }).catch(res => {
                // 查看返回消息
                if (res.data && res.data.msg) {
                    this.msg = res.data.msg;
                } else {
                    this.msg = "服务器繁忙，请稍后再试";
                }
            })
        },
        // 跳转到默认地址
        redirectDefaultUri: function () {
            // 刷新令牌失败 1.5s后跳转到登录页
            setTimeout(() => {
                window.location = window.defaultRedirectUri;
            }, 500)
            // 提示
            alert("信息失效，请重新登录");
        }
    }

    // 页面重新获得焦点事件
    window.hiddenProperty = 'hidden' in document ? 'hidden' : 'webkitHidden' in document ? 'webkitHidden' : 'mozHidden' in document ? 'mozHidden' : null;
    window.visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
    // 事件处理方法
    window.onVisibilityChange = function () {
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
})(window, window.axios);

// 时间增加Format格式化函数
Date.prototype.Format = function (fmt) {
    const o = {
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
    for (const k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

// 计算两个日期之间的天数差
Date.prototype.daysDistance = function (date1, date2) {
    // date是yyyy-MM-dd格式
    date1 = Date.parse(date1);
    date2 = date2 ? Date.parse(date2) : new Date();
    // 计算两个日期之间相差的毫秒数的绝对值
    const distance = Math.abs(date2 - date1);
    // 毫秒数除以一天的毫秒数,就得到了天数
    return Math.floor(distance / (24 * 3600 * 1000));
}