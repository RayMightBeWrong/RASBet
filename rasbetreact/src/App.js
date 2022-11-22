import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/pages/Login';
import Perfil from './components/pages/Perfil';
import Registo from './components/pages/Registo';
import Sport from './components/pages/Sport';

function App() {
  /*Possible userStates:
    loggedOff: no current logged user
    gambler: gambler logged in
    expert: expert logged in
    admin: admin logged in
  */
  const [userState, setUserState] = useState('loggedOff');


  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Sport sportType="any" />} />
          <Route path='/futebol' element={<Sport sportType="futebol" />} />
          <Route path='/basquetebol' element={<Sport sportType="basquetebol" />} />
          <Route path='/tenis' element={<Sport sportType="tenis" />} />
          <Route path='/motogp' element={<Sport sportType="motogp" />} />
          <Route path='/login' element={<Login />} />
          <Route path='/perfil' element={<Perfil />} />
          <Route path='/registo' element={<Registo />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
