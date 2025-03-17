const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8085', // Spring Boot 메인 서버
      changeOrigin: true,
    })
  );

  app.use(
    '/socket',
    createProxyMiddleware({
      target: 'http://localhost:9000', // Node.js 실시간 통신 서버
      changeOrigin: true,
    })
  );
};