import httpClient from "../http-common";

const create = data => {
    return httpClient.post('/api/clientsBehindLoans/', data);
}


const getAllLoansIdByClientBehindId = id => {
    return httpClient.get(`/api/clientsBehindLoans/${id}`);
}

export default { create, getAllLoansIdByClientBehindId};