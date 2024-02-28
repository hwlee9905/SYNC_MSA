const { createProxyMiddleware } = require('http-proxy-middleware');
const BASE_URL = 'http://158.247.197.212:9090'

module.exports = (app) => {
  // app.use(
  //   '/api',
  //   createProxyMiddleware({
  //     target: BASE_URL,	// 서버 URL or localhost:설정한포트번호
  //     changeOrigin: true,
  //   })
  // );
};