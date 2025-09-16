import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/clients/loans/');
}

const create = data => {
    return httpClient.post('/api/clients/loansts/', data);
}

const get = id => {
    return httpClient.get(`/api/clients/loans/${id}`);
}

const update = data => {
    return httpClient.put('/api/clients/loans/', data);
}

const remove = id => {
    return httpClient.remove(`/api/clients/loans/${id}`
    );
}

export default { getAll, get, create, update, remove};