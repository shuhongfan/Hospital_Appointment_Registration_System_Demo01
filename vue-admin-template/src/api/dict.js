import request from '@/utils/request'

const API_NAME = `/admin/cmn/dict`

export default {
  getDictList(id) {
    return request({
      url: `${API_NAME}/findChildData/${id}`,
      method: 'get'
    })
  }
}
