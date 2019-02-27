<template>
    <div id="main">
        <router-view/>
    </div>
</template>
<script>
export default {
    data () {
        return {

        };
    },
    methods: {}
};
</script>
<style lang="scss">
    html, body {
        height: 100%;
    }
    body{
        font-family: PingFang SC,Hiragino Sans GB,Microsoft YaHei,Arial,sans-serif;
    }
    .page-body{
        height: 100%;
        width: calc(100% - 200px);
        float: right;

        position: relative;
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        .page-body-content{
            padding: 0 16px 16px;
        }
        &.hide-slidebar{
            width: 100%;
        }
    }
     .spin-icon-load {
         animation: ani-demo-spin 1s linear infinite;
     }
    .ivu-table-fixed{
        box-shadow: 3px 0px 5px -2px rgba(0, 0, 0, .08) !important;
    }
    .ivu-table-fixed-right{
        box-shadow: -3px 0px 5px -2px rgba(0, 0, 0, .08) !important;
    }
    @keyframes ani-demo-spin {
        from {
            transform: rotate(0deg);
        }
        50% {
            transform: rotate(180deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

    #help-page{
        max-width: 720px;
        width: 90%;
        margin: 0 auto;
        h1{
            font-size: 24px;
            color: #404040;
            font-weight: 500;
            line-height: 40px;
            border-bottom: 1px solid #ececec;
            margin: 0 0 30px;
            padding-bottom: 10px;
        }
        h2{
            color: #404040;
            margin: 1.6em 0 .6em;
            font-weight: 400;
            clear: both;
            margin-top: 32px;
            font-size: 18px;
            line-height: 30px;
        }
        img{
            border-radius: 4px;
            margin: 0 -20px;
            padding: 20px;
            width: 100%;
            height: auto;
        }
        p{
            margin: 1.6em 0;
            line-height: 1.8em;
            font-weight: 400;
            color: #666;
            font-size: 14px;
            img{
                display: inline-block;
                padding: 0;
                margin: 0;
                width: auto;
                height: auto;
                vertical-align: text-bottom;
            }
        }
        a{
            color: inherit;
            transition: all ease 0.3s;
            &:hover{
                color: #333;
                text-decoration: underline;
                &:before{
                    opacity: 1;
                }
            }

            &.not-icon{
                &:before{
                    display: none;
                }
            }
            font-family: 'Font Awesome 5 Free';
            font-weight: 900;
            -webkit-font-smoothing: antialiased;
            display: inline-block;
            font-style: normal;
            font-variant: normal;
            text-rendering: auto;
            line-height: 1;
            &:before{
                content: "\F35D";
                color: #999;
                opacity: .5;
                font-size: 12px;
                margin-right: 2px;
                transform: scale(0.8);
                display: inherit;
            }
        }
        ul,ol{
            margin-left: 20px;
            margin: 0;
            padding: 0;
            li{
                p{
                    margin: .2em 0;
                    padding-left: 0;
                }
            }
        }
        ol{
            margin-left: 20px;
            li{
                margin-left: 14px;
            }
        }
        ul{
            margin-left: 20px;
            li{
                margin-left: 14px;
                list-style: circle;
            }
        }
        i{
            margin: 0 6px;
        }
        b{
            font-weight: 900;
            text-decoration: none;
            margin: 0 6px;
            & + i{
                margin-left: 0
            }
        }
        code{
            background: #f5f2f0;
            margin: 0 3px;
            padding: 2px 6px;
            border-radius: 3px;
            color: #dd4a68;
            font-size: .8rem;
            border: 1px solid #e9e9e9;
        }
        blockquote{
            font-size: 90%;
            color: #999;
            border-left: 4px solid #e9e9e9;
            padding-left: .8em;
            margin: 1em 0;
            font-style: italic;
        }
        .end{
            text-align: center;
            padding: 50px 0;
            font-size: 12px;
            color: #999;
            display: flex;
            align-items: center;
            margin: 0 -12px;
            &:after,&:before{
                content: ' ';
                display: block;
                width: 100%;
                height: 1px;
                background-color: #eee;
                margin: 0 12px;
                flex: 1;
            }
            b{
                font-weight: inherit;
            }
        }
        .help-android{

        }
    }
</style>
<style scoped lang="scss">
    #main {
        height: 100%;
        .content {
            height: 100%;
            width: calc(100% - 200px);
            float: right;

            position: relative;
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        .header {
            height: 64px;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-self: center;
            line-height: 64px;
            background-color: #fff;
            box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.05);
            .header__logo {
                height: 100%;
                .logo {
                    display: block;
                    height: 100%;
                    width: auto;
                }
            }
            .header-right {

            }
        }
    }
</style>
