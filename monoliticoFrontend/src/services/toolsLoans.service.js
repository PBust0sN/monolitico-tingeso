import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/tools/loan/');
}

const create = data => {
    return httpClient.post('/api/tools/loan/', data);
}

const get = id => {
    return httpClient.get(`/api/tools/loan/${id}`);
}

const update = data => {
    return httpClient.put('/api/tools/loan/', data);
}

const remove = id => {
    return httpClient.delete(`/api/tools/loan/${id}`
    );
}

const getToolsIdByLoanId = id => {
    return httpClient.get(`/api/loan/tools/${id}`);
}


export default { getAll, get, create, update, remove, getToolsIdByLoanId};