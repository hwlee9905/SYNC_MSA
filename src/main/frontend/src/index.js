import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import Signout from './pages/Signout';
import Signin from './pages/Signin';
import Signup from './pages/Signup';
import MyPage from './pages/MyPage.jsx';
import UserDelete from './pages/UserDelete.jsx';
import UserModify from './pages/UserModify';
import NotFound from './pages/NotFound';
import PasswordSearch from './pages/PasswordSearch';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <NotFound />,
    children: [
      {index: true, path: '/', element: <Home />},
      {path: 'logout', element: <Signout/>},
      {path: 'signup', element: <Signup/>},
      {path: 'login', element: <Signin/>},
      {path: 'mypage', element: <MyPage/>},
      {path: 'passwordsearch', element: <PasswordSearch/>},
      {path: 'mypage/delete', element: <UserDelete/>},
      {path: 'mypage/modify', element: <UserModify/>},

    ]
  }
])

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

