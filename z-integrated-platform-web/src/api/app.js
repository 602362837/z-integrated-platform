import request from './request'

export const getAppList = (params) => request.get('/v1/apps', { params })
export const getApp = (id) => request.get(`/v1/apps/${id}`)
export const createApp = (data) => request.post('/v1/apps', data)
export const updateApp = (data) => request.put(`/v1/apps/${data.id}`, data)
export const deleteApp = (id) => request.delete(`/v1/apps/${id}`)
export const getAppStats = () => request.get('/app/stats')
