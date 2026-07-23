import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { TTokenData } from 'react-oauth2-code-pkce/dist/types';

export interface AuthState {
  user: TTokenData | null;
  token: string | null;
  userId: string | null;
}

const getParsedStorage = <T>(key: string): T | null => {
  const item = localStorage.getItem(key);
  if (!item) return null;
  try {
    return JSON.parse(item) as T;
  } catch {
    return null;
  }
};

const initialState: AuthState = {
  user: getParsedStorage<TTokenData>('user'),
  token: localStorage.getItem('token'), 
  userId: localStorage.getItem('userId'),
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ user: TTokenData; token: string }>
    ) => {
      const { user, token } = action.payload;
      state.user = user;
      state.token = token;
      state.userId = user.sub;

      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('userId', user.sub);
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.userId = null; 

      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('userId');
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;