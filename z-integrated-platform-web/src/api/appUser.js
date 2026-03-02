import request from './request'

export const getAppUserList = (params) => request.get('/v1/app-users', { params })
export const getAppUser = (id) => request.get(`/v1/app-users/${id}`)
export const createAppUser = (data) => request.post('/v1/app-users', data)
export const updateAppUser = (data) => request.put(`/v1/app-users/${data.id}`, data)
export const deleteAppUser = (id) => request.delete(`/v1/app-users/${id}`)
export const syncAppUsers = (appCode) => request.post(`/v1/app-users/sync?appCode=${appCode}`)
export const getAppUserMapping = (params) => request.get('/v1/app-users/mapping', { params })
export const updateAppUserMapping = (data) => request.put(`/v1/app-users/${data.id}/map`, data)
