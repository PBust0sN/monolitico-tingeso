import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/tools/');
}

const create = data => {
    return httpClient.post('/api/tools/', data);
}

const get = id => {
    return httpClient.get(`/api/tools/${id}`);
}

const update = data => {
    return httpClient.put('/api/tools/', data);
}

const remove = id => {
    return httpClient.delete(`/api/tools/${id}`
    );
}

const updateState = (id, state) => {
    return httpClient.put(`/api/tools/update/state/${id}?state=${state}`);
}

const updateStock = id => {
    return httpClient.put(`/api/tools/update/stock/${id}`);
}

const getTenTools = () => {
    return httpClient.get('/api/tools/topTen');
}

export default { getAll, get, create, update, remove, updateState, updateStock, getTenTools };