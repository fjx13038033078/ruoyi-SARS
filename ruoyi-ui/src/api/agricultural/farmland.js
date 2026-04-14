import request from '@/utils/request'

export function listFarmland(query) {
  return request({ url: '/agricultural/farmland/list', method: 'get', params: query })
}

export function getFarmland(farmlandId) {
  return request({ url: '/agricultural/farmland/' + farmlandId, method: 'get' })
}

export function addFarmland(data) {
  return request({ url: '/agricultural/farmland', method: 'post', data })
}

export function updateFarmland(data) {
  return request({ url: '/agricultural/farmland', method: 'put', data })
}

export function delFarmland(farmlandIds) {
  return request({ url: '/agricultural/farmland/' + farmlandIds, method: 'delete' })
}

export function selectFarmlandList() {
  return request({ url: '/agricultural/farmland/selectList', method: 'get' })
}
