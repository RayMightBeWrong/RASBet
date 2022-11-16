import React from 'react';
import Navbar from './components/Navbar';
import './App.css';
import Home from './components/pages/Home';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Futebol from './components/pages/Sports/Futebol';
import Basquetebol from './components/pages/Sports/Basquetebol';
import Tenis from './components/pages/Sports/Tenis';
import Motogp from './components/pages/Sports/Motogp';
import Login from './components/pages/Login';
import Perfil from './components/pages/Perfil';
import Registo from './components/pages/Registo';

function App() {
  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path='/futebol' element={<Futebol />} />
          <Route path='/basquetebol' element={<Basquetebol />} />
          <Route path='/tenis' element={<Tenis />} />
          <Route path='/motogp' element={<Motogp />} />
          <Route path='/login' element={<Login />} />
          <Route path='/perfil' element={<Perfil />} />
          <Route path='/registo' element={<Registo />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
