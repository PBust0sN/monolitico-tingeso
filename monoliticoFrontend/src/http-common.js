import axios from "axios";
import keycloak from "./services/keycloak";

const monoliticoBackendServer = import.meta.env.VITE_MONOLITICO_BACKEND_SERVER;
const monoliticoBackendport = import.meta.env.VITE_MONOLITICO_BACKEND_PORT;

console.log(monoliticoBackendServer)
console.log(monoliticoBackendport)

const api = axios.create({
    baseURL: `http://${monoliticoBackendServer}:${monoliticoBackendport}`,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(async (config) => {
  if (keycloak.authenticated) {
    await keycloak.updateToken(30);
    config.headers.Authorization = `Bearer ${keycloak.token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default api;