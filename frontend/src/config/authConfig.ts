import { type TAuthConfig } from "react-oauth2-code-pkce"

export const authConfig: TAuthConfig = {
  clientId: import.meta.env.VITE_CLIENT_ID,
  authorizationEndpoint: import.meta.env.VITE_AUTH_ENDPOINT_KEYCLOAK,
  tokenEndpoint: import.meta.env.VITE_TOKEN_ENDPOINT_KEYCLOAK,
  redirectUri: import.meta.env.VITE_REDIRECT_URI,
  scope: 'openid profile email offline_access',
  onRefreshTokenExpire: (event) => event.logIn(),
}