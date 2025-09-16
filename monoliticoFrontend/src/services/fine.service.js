import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/fine/');
}

const create = data => {
    return httpClient.post('/api/fine/', data);
}

const get = id => {
    return httpClient.get(`/api/fine/${id}`);
}

const update = data => {
    return httpClient.put('/api/fine/', data);
}

const remove = id => {
    return httpClient.remove(`/api/fine/${id}`
    );
}

export default { getAll, get, create, update, remove};