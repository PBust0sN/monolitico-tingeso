import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/fees/');
}

const create = data => {
    return httpClient.post('/api/fees/', data);
}

const get = id => {
    return httpClient.get(`/api/fees/${id}`);
}

const update = data => {
    return httpClient.put('/api/fees/', data);
}

const remove = id => {
    return httpClient.remove(`/api/fees/${id}`
    );
}