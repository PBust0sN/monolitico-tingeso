import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/staff/');
}

const create = data => {
    return httpClient.post('/api/staff/', data);
}

const get = id => {
    return httpClient.get(`/api/staff/${id}`);
}

const update = data => {
    return httpClient.put('/api/staff/', data);
}

const remove = id => {
    return httpClient.remove(`/api/staff/${id}`
    );
}

export default { getAll, get, create, update, remove};