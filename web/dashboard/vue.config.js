
const debug = process.env.NODE_ENV !== 'production'

module.exports = {
    publicPath: '/app_version',
    outputDir: 'app_version',
    assetsDir: 'static',
    lintOnSave: true,
    productionSourceMap: true,
    css: {
      sourceMap: true
    },
	configureWebpack: config => {
	   if(debug) { // 开发环境配置
	      config.devtool='source-map'
	   }
	},
	/****
	 * 开发环境配置
	 ****/
	 devServer: {
		open: false,
		port: 8080,
		// 跨域
		proxy: {
		  '/app_version_manager/': {
			 target: 'http://localhost:9090/', //代理地址，这里设置的地址会代替axios中设置的baseURL
			 changeOrigin: true, // 如果接口跨域，需要进行这个参数配置
			 //ws: true, // proxy websockets
			 //pathRewrite方法重写url
			 pathRewrite: {
				 '^/app_version_manager': '/' 
				 //pathRewrite: {'^/api': '/'} 重写之后url为 http://192.168.1.16:8085/xxxx
				 //pathRewrite: {'^/api': '/api'} 重写之后url为 http://192.168.1.16:8085/api/xxxx
			} 
		  }
		}
	 },
	 
	 /****
	  * 生产环境配置
	  ****/
	  // devServer: {
		 // open: false,
		 // port: 6868,
		 // //跨域
		 // proxy: {
		 //   '/app_version_manager/': {
			//   target: 'http://111.204.83.200:6868/',  //代理地址，这里设置的地址会代替axios中设置的baseURL
			//   changeOrigin: true, // 如果接口跨域，需要进行这个参数配置
			//   //ws: true, // proxy websockets
			//   //pathRewrite方法重写url
			//   pathRewrite: {
			// 	 '^/app_version_manager': '/' 
			// 	 //pathRewrite: {'^/api': '/'} 重写之后url为 http://192.168.1.16:8085/xxxx
			// 	 //pathRewrite: {'^/api': '/api'} 重写之后url为 http://192.168.1.16:8085/api/xxxx
			//   }  
		 //   }
		 // }
	  // }
	  
};
