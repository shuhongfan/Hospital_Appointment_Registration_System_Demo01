import request from '@/utils/request'

const api_name = `/api/sms`

export default {
  sendCode(mobile) {
    return request({
      url: `${api_name}/send/${mobile}`,
      method: `get`
    })
  }
}
