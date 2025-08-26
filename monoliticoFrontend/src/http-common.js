import axios from "axios";


const monoliticoBackendServer = import.meta.env.VITE_MONOLITICO_BACKEND_SERVER;
const monoliticoBackendport = import.meta.env.VITE_MONOLITICO_BACKEND_PORT;

console.log(monoliticoBackendServer)
console.log(monoliticoBackendport)

export default axios.create({
    baseURL: `http://${monoliticoBackendServer}:${monoliticoBackendport}`,
    headers: {
        'Content-Type': 'application/json'
    }
});
