import { useContext, useEffect, useState, type JSX } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { AuthContext, type IAuthContext } from 'react-oauth2-code-pkce';
import { Button } from '@mui/material';
import { setCredentials } from './features/auth/authSlice';
import './App.css';
import { useDispatch } from 'react-redux';

export const App = (): JSX.Element => {
  const [authReady, setAuthReady] = useState<boolean>(false);
  
  const { token, tokenData, logIn, logOut } = useContext<IAuthContext>(AuthContext);
  
  const dispatch = useDispatch();

  useEffect(() => {
    if (token && tokenData) {
      dispatch(setCredentials({ token, user: tokenData }));
      queueMicrotask(() => {
        setAuthReady(true);
      });
    }    
  }, [token, tokenData, dispatch]);

  return (
    <BrowserRouter>
      {!token ? (
        <Button variant="contained" color="secondary" onClick={()=>logIn()}>
          Login
        </Button>
      ) : (
        <div>
          <pre>{JSON.stringify(tokenData,null,2)}</pre>
        </div>
      )}

    </BrowserRouter>
  );
};

export default App;