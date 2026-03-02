import request from './request'

export const getPlatformUserList = (params) => request.get('/v1/platform-users', { params })
export const getPlatformUser = (id) => request.get(`/v1/platform-users/${id}`)
export const createPlatformUser = (data) => request.post('/v1/platform-users', data)
export const updatePlatformUser = (data) => request.put(`/v1/platform-users/${data.id}`, data)
export const deletePlatformUser = (id) => request.delete(`/v1/platform-users/${id}`)
export const getPlatformUserStats = () => request.get('/platform-user/stats')
