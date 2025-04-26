module.exports = {
    // publicPath: process.env.VUE_APP_ENV_FIELD === ".env.prd" ? './' : "/",

    css: {
        loaderOptions: {
            less: {
                javascriptEnabled: true
            },
            scss: {
                prependData: `@import "@/style/_style.scss";`
            }
        }
    },
    chainWebpack: config => {
        config.plugin('html')
            .tap(args => {
                args[0].title = '退休人员信息管理系统'
                return args
            })
        // config.module
        //     .rule('images')
        //     .use('url-loader')
        //     .loader('url-loader')
        //     .tap(options => Object.assign(options, {limit: 1}))
    },
    productionSourceMap: false
};
process.env.VUE_APP_VERSION = require('./package.json').version;
