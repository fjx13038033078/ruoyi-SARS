import request from '@/utils/request'

export function listScheme(query) {
  return request({ url: '/agricultural/deploy/list', method: 'get', params: query })
}

export function getScheme(schemeId) {
  return request({ url: '/agricultural/deploy/' + schemeId, method: 'get' })
}

export function getSchemeNodes(schemeId) {
  return request({ url: '/agricultural/deploy/' + schemeId + '/nodes', method: 'get' })
}

export function generateScheme(farmlandId, algorithmType) {
  return request({
    url: '/agricultural/deploy/generate',
    method: 'post',
    params: { farmlandId, algorithmType }
  })
}

export function updateScheme(data) {
  return request({ url: '/agricultural/deploy', method: 'put', data })
}

export function delScheme(schemeIds) {
  return request({ url: '/agricultural/deploy/' + schemeIds, method: 'delete' })
}
