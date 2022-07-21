import request from '@/utils/request'

const API_NAME = '/admin/statistics'

export default {

  getCountMap(searchObj) {
    return request({
      url: `${API_NAME}/getCountMap`,
      method: 'get',
      params: searchObj
    })
  }
}
