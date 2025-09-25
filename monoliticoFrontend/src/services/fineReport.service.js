import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/fineReport/');
}

const create = data => {
    return httpClient.post('/api/fineReport/', data);
}

const remove = id => {
    return httpClient.delete(`/api/fineReport/${id}`);
}

const getAllByReportId = id => {
    return httpClient.get(`/api/fineReport/${id}`);
}

export default { getAll, create, remove, getAllByReportId};