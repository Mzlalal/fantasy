// 定义组件注册的模板template html
const template_down_slide_stream_html =
    `<div class="ui-form ui-border-t ui-container overflow-auto" :style="{'min-height': '30vh', 'max-height': 'calc(100vh - 120px)'}" 
        @scroll="onScroll" ref="scrollTarget">
        <slot></slot>
        <section class="ui-notice" v-if="loading === false && hasData === false">
            <i></i>
            <p>暂无数据</p>
        </section>
        <div class="ui-loading-wrap margin-top-1em" v-if="loading">
            <p>加载中</p>
            <i class="ui-loading"></i>
        </div>
        <section class="ui-notice margin-top-1em" style="height: unset" v-if="loading === false && hasData && hasBeenBottom">
            <p>没有更多了~</p>
        </section>
    </div>`;
// Vue定义组件
const template_down_slide_stream = Vue.extend({
    template: template_down_slide_stream_html,
    // 这里的data与vue对象的data类似，只不过组件中的data必须是函数的形式
    data() {
        return {}
    },
    // 这里的methods与vue对象的methods一样，可以在这里定义组件的函数 没用到也可以不写
    methods: {
        // 监听滚动
        onScroll() {
            // 滚动条高度 - 距离顶部的距离 - 像素高度
            let scrollBottom = this.$refs.scrollTarget.scrollHeight - this.$refs.scrollTarget.scrollTop - this.$refs.scrollTarget.offsetHeight;
            // 加上容器计算offsetToBottom的高度
            scrollBottom += this.offsetToBottom;
            // 打印
            console.log(scrollBottom);
            // 滚动到底部小于distanceToBottom的距离,则提供事件
            if (scrollBottom <= this.distanceToBottom && this.hasBeenBottom === false && this.loading === false) {
                // 提供事件出去
                this.$emit("on-scroll-to-bottom");
            }
        },
    },
    // props用来接收外部参数的
    props: {
        // 其他组件占用的距离
        offsetToBottom: {
            type: Number,
            default: 0,
        },
        // 到底部的距离
        distanceToBottom: {
            type: Number,
            default: 0,
        },
        // 加载框
        loading: {
            type: Boolean,
            default: false,
        },
        // 是否有数据
        hasData: {
            type: Boolean,
            default: false,
        },
        // 是否已经到底部
        hasBeenBottom: {
            type: Boolean,
            default: false,
        }
    },
});

//Vue注册全局组件
Vue.component('template-down-slide-stream', template_down_slide_stream);