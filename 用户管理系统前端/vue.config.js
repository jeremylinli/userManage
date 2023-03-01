module.exports = {
  devServer: {
    port: 3000,
    open: true,
    // proxy: 'https://www.escook.cn'
    proxy: 'http://localhost:8080/user',
    disableHostCheck: true
  }
}