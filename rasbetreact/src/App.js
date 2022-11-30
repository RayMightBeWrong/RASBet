import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/pages/Login';
import Perfil from './components/pages/Perfil';
import Registo from './components/pages/Registo';
import Sport from './components/pages/Sport';
import AdminMenu from './components/pages/AdminMenu';
import MenuCoins from './components/pages/MenuCoins';
import MenuPromocoes from './components/pages/MenuPromocoes';
import ConsultaPerfil from './components/pages/ConsultaPerfil';
import Historico from './components/pages/Historico';

function App() {
  /*Possible userStates:
    loggedOff: no current logged user
    gambler: gambler logged in
    expert: expert logged in
    admin: admin logged in
  */
  const [userState, setUserState] = useState('gambler')
  const [userId, setUserId] = useState(0)

  return (
    <>
      <Router>
        <Navbar userState={userState} setUserState={setUserState} />
        <Routes>
          <Route path="/" element={<Sport sportType="any" userState={userState} userId={userId} />} />
          <Route path='/futebol' element={<Sport sportType="futebol" userState={userState} userId={userId} />} />
          <Route path='/basquetebol' element={<Sport sportType="basquetebol" userState={userState} userId={userId} />} />
          <Route path='/tenis' element={<Sport sportType="tenis" userState={userState} userId={userId} />} />
          <Route path='/motogp' element={<Sport sportType="motogp" userState={userState} userId={userId} />} />
          <Route path='/login' element={<Login setUserState={setUserState} setUserId={setUserId} />} />
          <Route path='/perfil' element={<Perfil />} />
          <Route path='/historico' element={<Historico />} />
          <Route path='/registo' element={<Registo userState={userState} expertMode="false" />} />
          <Route path='/admin_Options' element={<AdminMenu userState={userState} />} />
          <Route path='/admin_Options/registo_Expert' element={<Registo userState={userState} expertMode="true" />} />
          <Route path='/admin_Options/coins' element={<MenuCoins userState={userState} />} />
          <Route path='/admin_Options/promocoes' element={<MenuPromocoes userState={userState} />} />
          <Route path='/admin_Options/consultaPerfil' element={<ConsultaPerfil userState={userState} />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
