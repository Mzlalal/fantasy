(function (window) {
    // 请求服务器的网关
    window.gateway = "http://" + location.host;
    // 默认跳转页
    window.defaultRedirectUri = "/fantasy-oauth2/mi-ma-deng-lu.html";
    // 客户端Key
    window.clientKey = "fantasy-oauth2";
    // 需要刷新令牌的的状态码
    window.refreshTokenStateCode = [1404, 1405];
    // 房间已存在状态码
    window.roomIsExist = 2205;
    // 日历类别,例:1=阳历(公历),2=阴历(农历)
    window.calendarTypeOption = [
        {
            "code": "1",
            "label": "阳历（公历）",
        },
        {
            "code": "2",
            "label": "阴历（农历）",
        },
    ];
    // 重复提醒模式
    window.repeatModeOption = [
        {
            "code": "1",
            "label": "每年",
        },
        {
            "code": "2",
            "label": "每月",
        },
        {
            "code": "3",
            "label": "每周",
        },
        {
            "code": "4",
            "label": "每日",
        },
    ];
    // 日历-月
    window.calendarMonthOption = [
        {
            "code": "01",
            "label": "1月"
        },
        {
            "code": "02",
            "label": "2月"
        },
        {
            "code": "03",
            "label": "3月"
        },
        {
            "code": "04",
            "label": "4月"
        },
        {
            "code": "05",
            "label": "5月"
        },
        {
            "code": "06",
            "label": "6月"
        },
        {
            "code": "07",
            "label": "7月"
        },
        {
            "code": "08",
            "label": "8月"
        },
        {
            "code": "09",
            "label": "9月"
        },
        {
            "code": "10",
            "label": "10月"
        },
        {
            "code": "11",
            "label": "11月"
        },
        {
            "code": "12",
            "label": "12月"
        }
    ];
    // 日历-日
    window.calendarDayOption = [
        {
            "code": "01",
            "label": "1日"
        },
        {
            "code": "02",
            "label": "2日"
        },
        {
            "code": "03",
            "label": "3日"
        },
        {
            "code": "04",
            "label": "4日"
        },
        {
            "code": "05",
            "label": "5日"
        },
        {
            "code": "06",
            "label": "6日"
        },
        {
            "code": "07",
            "label": "7日"
        },
        {
            "code": "08",
            "label": "8日"
        },
        {
            "code": "09",
            "label": "9日"
        },
        {
            "code": "10",
            "label": "10日"
        },
        {
            "code": "11",
            "label": "11日"
        },
        {
            "code": "12",
            "label": "12日"
        },
        {
            "code": "13",
            "label": "13日"
        },
        {
            "code": "14",
            "label": "14日"
        },
        {
            "code": "15",
            "label": "15日"
        },
        {
            "code": "16",
            "label": "16日"
        },
        {
            "code": "17",
            "label": "17日"
        },
        {
            "code": "18",
            "label": "18日"
        },
        {
            "code": "19",
            "label": "19日"
        },
        {
            "code": "20",
            "label": "20日"
        },
        {
            "code": "21",
            "label": "21日"
        },
        {
            "code": "22",
            "label": "22日"
        },
        {
            "code": "23",
            "label": "23日"
        },
        {
            "code": "24",
            "label": "24日"
        },
        {
            "code": "25",
            "label": "25日"
        },
        {
            "code": "26",
            "label": "26日"
        },
        {
            "code": "27",
            "label": "27日"
        },
        {
            "code": "28",
            "label": "28日"
        },
        {
            "code": "29",
            "label": "29日"
        },
        {
            "code": "30",
            "label": "30日"
        },
        {
            "code": "31",
            "label": "31日"
        }
    ];
    // 星期几
    window.weekdayOption = [
        {
            "code": 1,
            "label": "星期日"
        }, {
            "code": 2,
            "label": "星期一"
        }, {
            "code": 3,
            "label": "星期二"
        }, {
            "code": 4,
            "label": "星期三"
        }, {
            "code": 5,
            "label": "星期四"
        }, {
            "code": 6,
            "label": "星期五"
        }, {
            "code": 7,
            "label": "星期六"
        }
    ];
    // 日历-时
    window.calendarHourOption = [
        {
            "code": "00",
            "label": "0时"
        }, {
            "code": "01",
            "label": "1时"
        }, {
            "code": "02",
            "label": "2时"
        }, {
            "code": "03",
            "label": "3时"
        }, {
            "code": "04",
            "label": "4时"
        }, {
            "code": "05",
            "label": "5时"
        }, {
            "code": "06",
            "label": "6时"
        }, {
            "code": "07",
            "label": "7时"
        }, {
            "code": "08",
            "label": "8时"
        }, {
            "code": "09",
            "label": "9时"
        }, {
            "code": "10",
            "label": "10时"
        }, {
            "code": "11",
            "label": "11时"
        }, {
            "code": "12",
            "label": "12时"
        }, {
            "code": "13",
            "label": "13时"
        }, {
            "code": "14",
            "label": "14时"
        }, {
            "code": "15",
            "label": "15时"
        }, {
            "code": "16",
            "label": "16时"
        }, {
            "code": "17",
            "label": "17时"
        }, {
            "code": "18",
            "label": "18时"
        }, {
            "code": "19",
            "label": "19时"
        }, {
            "code": "20",
            "label": "20时"
        }, {
            "code": "21",
            "label": "21时"
        }, {
            "code": "22",
            "label": "22时"
        }, {
            "code": "23",
            "label": "23时"
        }
    ];
    // 日历-分
    window.calendarMinuteOption = [
        {
            "code": "00",
            "label": "0分"
        }, {
            "code": "05",
            "label": "5分"
        }, {
            "code": "10",
            "label": "10分"
        }, {
            "code": "15",
            "label": "15分"
        }, {
            "code": "20",
            "label": "20分"
        }, {
            "code": "25",
            "label": "25分"
        }, {
            "code": "30",
            "label": "30分"
        }, {
            "code": "35",
            "label": "35分"
        }, {
            "code": "40",
            "label": "40分"
        }, {
            "code": "45",
            "label": "45分"
        }, {
            "code": "50",
            "label": "50分"
        }, {
            "code": "55",
            "label": "55分"
        }
    ];
    // 懒人提醒次数
    window.lazyTimesOption = [
        {
            "code": 1,
            "label": "1次"
        }, {
            "code": 2,
            "label": "2次"
        }, {
            "code": 3,
            "label": "3次"
        }, {
            "code": 4,
            "label": "4次"
        }, {
            "code": 5,
            "label": "5次"
        }
    ];
    // 纪念日提醒间隔
    window.matterIntervalOption = [
        {
            "code": 0,
            "label": "不需要提醒"
        }, {
            "code": 3,
            "label": "3天提醒一次"
        }, {
            "code": 5,
            "label": "5天提醒一次"
        }, {
            "code": 7,
            "label": "7天提醒一次"
        }, {
            "code": 10,
            "label": "10天提醒一次"
        }, {
            "code": 15,
            "label": "15天提醒一次"
        }, {
            "code": 30,
            "label": "30天提醒一次"
        }
    ];
})(window);