import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:9090",
  realm: "toolRent",
  clientId: "toolRent-Frontend",
});

export default keycloak;