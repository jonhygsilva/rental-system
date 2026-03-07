import api from "../../../shared/api/axiosInstance";

const BASE = "/api/customers";

export const getCustomers = ({ page = 0, size = 10, sort = "name,asc", search = "", filters = {} } = {}) => {
  const params = {
    page,
    size,
    sort,
    search,
    ...filters
  };
  return api.get(BASE, { params });
};

export const getCustomer = (id) =>
  api.get(`${BASE}/${id}`);

export const createCustomer = (data) =>
  api.post(BASE, data);

export const updateCustomer = (id, data) =>
  api.put(`${BASE}/${id}`, data);

export const deleteCustomer = (id) =>
  api.delete(`${BASE}/${id}`);
