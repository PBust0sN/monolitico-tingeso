import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/roles/');
}

const create = data => {
    return httpClient.post('/api/roles/', data);
}

const get = id => {
    return httpClient.get(`/api/roles/${id}`);
}

const update = data => {
    return httpClient.put('/api/roles/', data);
}

const remove = id => {
    return httpClient.remove(`/api/roles/${id}`
    );
}

export default { getAll, get, create, update, remove};