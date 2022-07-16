import request from '@/utils/request'

const API_NAME = `/admin/hosp/hospitalSet`

export default {
  getHospSetList(current, limit, searchObj) {
    return request({
      url: `${API_NAME}/findPage/${current}/${limit}`,
      method: 'post',
      data: searchObj // 使用JSON
    })
  },
  deleteHospSet(id) {
    return request({
      url: `${API_NAME}/${id}`,
      method: 'delete'
    })
  },
  batchDeleteHospSet(idList) {
    return request({
      url: `${API_NAME}/batchRemove`,
      method: 'delete',
      data: idList // 使用JSON
    })
  },
  lockHospSet(id, status) {
    return request({
      url: `${API_NAME}/lockHospitalSet/${id}/${status}`,
      method: 'put'
    })
  },
  saveHospSet(hospSet) {
    return request({
      url: `${API_NAME}/saveHospitalSet`,
      method: 'post',
      data: hospSet // 使用JSON
    })
  },
  updateHospSet(hospSet) {
    return request({
      url: `${API_NAME}/updateHospitalSet`,
      method: 'put',
      data: hospSet // 使用JSON
    })
  },
  getHostSet(id) {
    return request({
      url: `${API_NAME}/getHospSet/${id}`,
      method: 'get'
    })
  }
}
