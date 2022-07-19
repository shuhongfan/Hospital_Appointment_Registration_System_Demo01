import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import cookie from 'js-cookie'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:88',
  timeout: 15000 // 请求超时时间
})

// http request 拦截器
service.interceptors.request.use(
  config => {
    // 判断cookie是否有token值
    if (cookie.get('token')) {
      // 把token值放到cookie里面
      config.headers['token'] = cookie.get('token')
    }
    return config
  },
  err => {
    return Promise.reject(err)
  })

// http response 拦截器
service.interceptors.response.use(
  response => {
    // 如果状态码是208，说明未登录，弹出登录输入框
    if (response.data.code === 208) {
      eventLogin.$emit('loginDialogEvent')
      return
    } else {
      if (response.data.code !== 200) {
        Message({
          message: response.data.message,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(response.data)
      } else {
        return response.data
      }
    }
  },
  error => {
    return Promise.reject(error.response)
  })
export default service
