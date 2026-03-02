import request from './request'

export const getOrgTree = (params) => request.get('/v1/orgs/tree', { params })
export const getOrg = (id) => request.get(`/v1/orgs/${id}`)
export const createOrg = (data) => request.post('/v1/orgs', data)
export const updateOrg = (data) => request.put(`/v1/orgs/${data.id}`, data)
export const deleteOrg = (id) => request.delete(`/v1/orgs/${id}`)
