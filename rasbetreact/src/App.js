import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/pages/Login';
import Perfil from './components/pages/gamblerExclusive/Perfil';
import Registo from './components/pages/Registo';
import Sport from './components/pages/Sport';
import AdminMenu from './components/pages/adminExclusive/AdminMenu';
import MenuCoins from './components/pages/adminExclusive/MenuCoins';
import GestorPromocoes from './components/pages/adminExclusive/GestorPromocoes';
import Promocoes from './components/pages/gamblerExclusive/Promocoes';
import ConsultaPerfil from './components/pages/adminExclusive/ConsultaPerfil';
import Historico from './components/pages/gamblerExclusive/Historico';
import CriarPromocao from './components/pages/adminExclusive/CriarPromocao'
import ExpertManager from './components/pages/adminExclusive/ExpertManager';

function App() {
  /*Possible userStates:
    loggedOff: no current logged user
    gambler: gambler logged in
    expert: expert logged in
    admin: admin logged in
  */

  const [userState, setUserState] = useState(() => {
    const userState = localStorage.getItem('userState')
    if (userState) {
      return userState
    }
    else return "loggedOff"
  })

  const [userId, setUserId] = useState(() => {
    const userId = localStorage.getItem('userId')
    if (userId) {
      return userId
    }
    else return 0
  })

  const [notificationList, setNotificationList] = useState({
    nova: false,
    lista: []
  })


  useEffect(() => {
    if (userState === "gambler") {
      let urlEndPoint = "http://localhost:8080/sse/subscribe?gambler_id=" + userId
      let eventSource = new EventSource(urlEndPoint)

      eventSource.addEventListener("NOTIFICATION", function (event) {
        let notification = JSON.parse(event.data)
        console.log(notification)
        setNotificationList(oldList => ({
          nova: true, lista: [...oldList.lista, notification]
        }))
      })
    }
  }, [userId, userState])

  const [updateNotification, setUpdateNotification] = useState(true)
  useEffect(() => {
    if (updateNotification) {
      setUpdateNotification(false)

      const requestOptions = {
        method: 'GET',
        headers: { "Content-Type": "application/json" }
      }
      fetch("http://localhost:8080/api/games/notifications?gambler_id=" + userId, requestOptions)
        .then(res => res.json())
        .then(result => {
          console.log(result)
          if (result !== []){
            console.log("fetch")
            setNotificationList({
              nova: true,
              lista: result
            })
          }
        })
    }
  }, [updateNotification, userState, userId])



  useEffect(() => {
    console.log("Changed to" + userState)
    localStorage.setItem('userState', userState)
  }, [userState])

  useEffect(() => {
    localStorage.setItem('userId', userId)
  }, [userId])

  return (
    <Router>
      <div className='inv'>
        <div className='inv2'>
          <Routes>
            {/*Mutual pages*/}
            <Route path="/" element={<Sport sportType="any" userState={userState} userId={userId} />} />
            <Route path='/futebol' element={<Sport sportType="Football" userState={userState} userId={userId} />} />
            <Route path='/nba' element={<Sport sportType="NBA" userState={userState} userId={userId} />} />
            <Route path='/f1' element={<Sport sportType="F1" userState={userState} userId={userId} />} />
            <Route path='/nfl' element={<Sport sportType="NFL" userState={userState} userId={userId} />} />
            <Route path='/login' element={<Login setUserState={setUserState} setUserId={setUserId} updateNotifications={() => setUpdateNotification(true)} />} />

            {/*gambler pages*/}
            <Route path='/perfil' element={<Perfil userId={userId} userState={userState} />} />
            <Route path='/promocoes' element={<Promocoes userState={userState} />} />
            <Route path='/historico' element={<Historico userId={userId} userState={userState} />} />
            <Route path='/registo' element={<Registo userState={userState} expertMode="false" />} />
            <Route path='/subscribedGames' element={<Sport sportType="Subscriptions" userState={userState} userId={userId} />} />

            {/*Admin TOOLS*/}
            <Route path='/admin_Options' element={<AdminMenu userState={userState} />} />
            <Route path='/admin_Options/registo_Expert' element={<Registo userState={userState} expertMode="true" />} />
            <Route path='/admin_Options/coins' element={<MenuCoins userState={userState} />} />
            <Route path='/admin_Options/promocoes' element={<GestorPromocoes userState={userState} />} />
            <Route path='/admin_Options/promocoes/creation' element={<CriarPromocao userState={userState} />} />
            <Route path='/admin_Options/consultaPerfil' element={<ConsultaPerfil userState={userState} setUserState={setUserState} setUserId={setUserId} updateNotifications={() => setUpdateNotification(true)} />} />
            <Route path='/admin_Options/expertDelete' element={<ExpertManager userState={userState} />} />

          </Routes>
        </div>
        <Navbar userState={userState} setUserState={setUserState} notificationList={notificationList}
          markAsSeen={() => setNotificationList(oldList => ({
            nova: false, lista: oldList.lista
          }))}
        />
      </div>
    </Router>
  );
}

export default App
