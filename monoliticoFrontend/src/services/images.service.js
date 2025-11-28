import httpClient from "../http-common";

const uploadImage = (data, filename) => {
    const params = new URLSearchParams();
    if (filename) {
        params.append('filename', filename);
    }
    
    return httpClient.post(`/api/images/upload${filename ? '?filename=' + filename : ''}`, data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

const getImage = imageName => {
    return httpClient.get(`/api/images/${imageName}`, { responseType: 'blob' });
}

export default { uploadImage, getImage };