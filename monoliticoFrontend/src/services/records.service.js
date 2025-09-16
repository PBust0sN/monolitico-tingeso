import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/records/');
}

const create = data => {
    return httpClient.post('/api/records/', data);
}

const get = id => {
    return httpClient.get(`/api/records/${id}`);
}

const update = data => {
    return httpClient.put('/api/records/', data);
}

const remove = id => {
    return httpClient.remove(`/api/records/${id}`
    );
}

export default { getAll, get, create, update, remove};