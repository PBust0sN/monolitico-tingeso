import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/loans/');
}

const create = data => {
    return httpClient.post('/api/loans/', data);
}

const get = id => {
    return httpClient.get(`/api/loans/${id}`);
}

const update = data => {
    return httpClient.put('/api/loans/', data);
}

const remove = id => {
    return httpClient.remove(`/api/loans/${id}`
    );
}

export default { getAll, get, create, update, remove};