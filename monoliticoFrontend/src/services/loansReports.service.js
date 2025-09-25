import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/loansReport/');
}

const create = data => {
    return httpClient.post('/api/loansReport/', data);
}

const remove = id => {
    return httpClient.delete(`/api/loansReport/${id}`);
}

const getAllByReportId = id => {
    return httpClient.get(`/api/loansReport/${id}`);
}

export default { getAll, create, remove, getAllByReportId};