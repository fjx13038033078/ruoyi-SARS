import request from '@/utils/request'

export function listErrorLog(query) {
  return request({ url: '/agricultural/errorlog/list', method: 'get', params: query })
}

export function getErrorLog(errorId) {
  return request({ url: '/agricultural/errorlog/' + errorId, method: 'get' })
}

export function delErrorLog(errorIds) {
  return request({ url: '/agricultural/errorlog/' + errorIds, method: 'delete' })
}

export function cleanErrorLog() {
  return request({ url: '/agricultural/errorlog/clean', method: 'delete' })
}
