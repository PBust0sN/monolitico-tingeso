import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/clientsBehind/');
}

const create = data => {
    return httpClient.post('/api/clientsBehind/', data);
}

const remove = id => {
    return httpClient.delete(`/api/clientsBehind/${id}`);
}

const getAllByReportId = id => {
    return httpClient.get(`/api/clientsBehind/${id}`);
}

export default { getAll, create, remove, getAllByReportId};