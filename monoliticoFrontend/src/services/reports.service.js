import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/reports/');
}

const create = data => {
    return httpClient.post('/api/reports/', data);
}

const get = id => {
    return httpClient.get(`/api/reports/${id}`);
}

const update = data => {
    return httpClient.put('/api/reports/', data);
}

const remove = id => {
    return httpClient.remove(`/api/reports/${id}`
    );
}

export default { getAll, get, create, update, remove};