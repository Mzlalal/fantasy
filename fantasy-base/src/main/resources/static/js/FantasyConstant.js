(function (window) {
    // 请求服务器的网关
    window.gateway = "http://" + location.host;
    // 默认跳转页
    window.defaultRedirectUri = "/fantasy-oauth2/mi-ma-deng-lu.html";
    // 客户端Key
    window.clientKey = "fantasy-oauth2";
    // 需要刷新令牌的的状态码
    window.refreshTokenStateCode = [1404, 1405];
    // 日历类别,例:1=阳历,2=阴历(农历)
    window.calendarType = [
        {
            "calendarTypeValue": 1,
            "calendarTypeLabel": "阳历",
        },
        {
            "calendarTypeValue": 2,
            "calendarTypeLabel": "阴历（农历）",
        },
    ];
    // 日历-月
    window.calendarMonth = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
    // 日历-日
    window.calendarDay = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31];
    // 日历-时
    window.calendarHour = [4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
    // 日历-分
    window.calendarMinute = [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55];
    // 重复提醒次数
    window.repeatTimes = [1, 2, 3, 4, 5];
})(window);