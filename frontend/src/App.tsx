import { useContext, useEffect, useState, type JSX } from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthContext, type IAuthContext } from 'react-oauth2-code-pkce';
import { Box, Button } from '@mui/material';
import { setCredentials } from './features/auth/authSlice';
import './App.css';
import './index.css';
import { useDispatch } from 'react-redux';
import SymptomList from './components/SymptomList';
import SymptomForm from './components/SymptomForm';
import SymptomDetail from './components/SymptomDetail';

const SymptomPage = ()=>{
  return(
    <div className='min-h-screen flex flex-col items-center gap-4'>
      <Box component="section" sx={{ p: 2, border: '0.5px solid lightgrey', borderRadius:'10px', boxShadow:'0px 0px 1px lightgray' }}>
        <SymptomForm onSymptomAdded={()=>window.location.reload()}/>
      </Box>
      <Box component="section" sx={{ p: 2, border: '0.5px solid lightgrey', borderRadius:'10px', boxShadow:'0px 0px 1px lightgray' }}>
        <SymptomList/>
      </Box>
    </div>
  )
}

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
        //<div>
        //  <pre>{JSON.stringify(tokenData,null,2)}</pre>
        //</div>
        <div>
          <Routes>
            <Route path='/symptoms' element={<SymptomPage/>}/>
            <Route path='/symptoms/:id' element={<SymptomDetail/>}/>
            <Route path='/' element={token ? <Navigate to='/symptoms' replace/> : <div>Welcome to the medical assistant page. Login to use the app</div>} />
          </Routes>
        </div>
      )}

    </BrowserRouter>
  );
};

export default App;