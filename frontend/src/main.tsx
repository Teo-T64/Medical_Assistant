import ReactDOM from 'react-dom/client'
import { Provider } from 'react-redux'

import { store } from './util/store'
import App from './App'
import { AuthProvider } from 'react-oauth2-code-pkce'
import { authConfig } from './config/authConfig'

const container = document.getElementById('root')

if (!container) {
  throw new Error("Failed to find the root element. Ensure 'root' exists in index.html.")
}

const root = ReactDOM.createRoot(container)

root.render(
    <AuthProvider authConfig={authConfig}>
      <Provider store={store}>
        <App />
      </Provider>
    </AuthProvider>
)