import request from '@/utils/request'

export function listEnvData(query) {
  return request({ url: '/agricultural/envdata/list', method: 'get', params: query })
}

export function getEnvData(dataId) {
  return request({ url: '/agricultural/envdata/' + dataId, method: 'get' })
}

export function addEnvData(data) {
  return request({ url: '/agricultural/envdata', method: 'post', data })
}

export function delEnvData(dataIds) {
  return request({ url: '/agricultural/envdata/' + dataIds, method: 'delete' })
}

export function generateEnvData(farmlandId, count) {
  return request({
    url: '/agricultural/envdata/generate',
    method: 'post',
    params: { farmlandId, count }
  })
}
