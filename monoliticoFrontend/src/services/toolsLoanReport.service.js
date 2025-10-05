import httpClient from "../http-common";

const create = data => {
    return httpClient.post('/api/tools/loan/', data);
}

const getToolsIdByLoanId = id => {
    return httpClient.get(`/api/toolsloansreports/getools/${id}`);
}


export default {create, getToolsIdByLoanId};