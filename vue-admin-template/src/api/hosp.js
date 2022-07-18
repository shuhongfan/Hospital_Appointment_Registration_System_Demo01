import request from '@/utils/request'

const API_NAME = `/admin/hosp/hospital`

export default {
  // 医院列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `${API_NAME}/list/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  // 查询dictCode查询下级数据字典
  findByDictCode(dictCode) {
    return request({
      url: `/admin/cmn/dict/findByDictCode/${dictCode}`,
      method: 'get'
    })
  },
  // 根据id查询下级数据字典
  findByParentId(dictCode) {
    return request({
      url: `/admin/cmn/dict/findChildData/${dictCode}`,
      method: 'get'
    })
  },
  // 更新医院的上线状态
  updateStatus(id, status) {
    return request({
      url: `${API_NAME}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  },
  // 查看医院详情
  getHospId(id) {
    return request({
      url: `${API_NAME}/showHospDetail/${id}`,
      method: 'get'
    })
  }
}
