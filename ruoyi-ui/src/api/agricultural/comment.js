import request from '@/utils/request'

export function listComment(query) {
  return request({ url: '/agricultural/comment/list', method: 'get', params: query })
}

export function addComment(data) {
  return request({ url: '/agricultural/comment', method: 'post', data })
}

export function delComment(commentIds) {
  return request({ url: '/agricultural/comment/' + commentIds, method: 'delete' })
}

export function updateCommentStatus(commentId, status) {
  return request({ url: `/agricultural/comment/${commentId}/status/${status}`, method: 'put' })
}
