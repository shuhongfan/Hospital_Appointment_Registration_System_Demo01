import request from '@/utils/request'

const api_name = '/api/cmn/dict'

export default {
  findByDictCode(dictCode) {
    return request({
      url: `${api_name}/findByDictCode/${dictCode}`,
      method: 'get'
    })
  },

  findByParentId(parentId) {
    return request({
      url: `${api_name}/findByParentId/${parentId}`,
      method: 'get'
    })
  }
}
