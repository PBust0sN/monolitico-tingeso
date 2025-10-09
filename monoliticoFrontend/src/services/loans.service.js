import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/loans/');
}

const create = data => {
    return httpClient.post('/api/loans/', data);
}

const newLoan = data => {
    return httpClient.post('/api/loans/new', data);
}

const returnLoan = data => {
    return httpClient.post('/api/loans/return', data);
}

const get = id => {
    return httpClient.get(`/api/loans/${id}`);
}

const update = data => {
    return httpClient.put('/api/loans/', data);
}

const remove = id => {
    return httpClient.delete(`/api/loans/${id}`
    );
}

const calculateCost = id => {
    return httpClient.get(`/api/loans/calculate/cost/${id}`);
}

// Send the full loan object in the request body so backend can validate dates
const chechDates = data => {
    return httpClient.post('/api/loans/checkdates', data);
}

export default { getAll, get, create, newLoan, returnLoan, update, remove, calculateCost, chechDates};