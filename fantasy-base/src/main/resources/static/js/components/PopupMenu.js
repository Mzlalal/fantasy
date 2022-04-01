// 定义组件注册的模板template html
const template_html = `<div class="ui-actionsheet show" v-if="menuShow" @click="onClose">
        <div class="ui-actionsheet-cnt am-actionsheet-down">
            <button @click="redirectUpdateUserInfo">我的信息</button>
            <button @click="redirectClientCenter">应用中心</button>
            <button class="ui-actionsheet-del" @click="logout">退出登录</button>
            <div class="ui-actionsheet-split-line"></div>
            <button id="cancel" @click="onClose">取消</button>
        </div>
    </div> `;
// Vue定义组件
const template_popup_menu = Vue.extend({
    template: template_html,
    // 这里的data与vue对象的data类似，只不过组件中的data必须是函数的形式
    data() {
        return {
            // 是否显示菜单
            menuShow: false,
        }
    },
    // 这里的methods与vue对象的methods一样，可以在这里定义组件的函数 没用到也可以不写
    methods: {
        // 跳转到更新用户信息
        redirectUpdateUserInfo() {
            // 保存URL
            localStorage.setItem("user.href.from", window.location.href);
            // 跳转到修改信息
            window.location = "/fantasy-oauth2/wo-de-xin-xi.html";
        },
        // 跳转到应用中心
        redirectClientCenter() {
            // 跳转到修改信息
            window.location = "/fantasy-oauth2/gong-neng-lie-biao.html";
        },
        // 退出登录
        logout() {
            // 确认退出登录
            if (!window.confirm("确定当前退出用户登录?")) {
                return;
            }
            // 请求接口
            axios({
                method: "get",
                url: "/fantasy-oauth2/api/v1/oauth2/token/logout",
            }).then(res => {
                // 清空缓存
                localStorage.clear();
                // 跳转地址
                window.location = window.defaultRedirectUri;
            }).catch(res => {
                if (res.data && res.data.msg) {
                    this.tips = res.data.msg;
                } else {
                    this.tips = "服务器繁忙，请稍后再试";
                }
            })
        },
        // 关闭弹窗
        onClose() {
            // 提供事件出去
            this.$emit("on-close");
        }
    },
    // props用来接收外部参数的
    props: {
        menuShow: {
            type: Boolean,
            default: false,
        }
    },
});

//Vue注册全局组件
Vue.component('template-popup-menu', template_popup_menu);