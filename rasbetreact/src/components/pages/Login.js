import React, { useState, useEffect } from 'react';
import './Comons.css';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from '../Button';

function Login({
  setUserState,
  setUserId
}) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleSubmit = event => {
    event.preventDefault()
    const requestOptions = {
      method: 'GET',
      headers: { "Content-Type": "application/json" }
    }

    fetch("http://localhost:8080/api/users?email=" + email + "&password=" + password, requestOptions)
      .then(res => res.json())
      .then(result => {
        setUserId(result.user_id)
        switch (result.type) {
          case 0:
            alert("Gambler Logged in")
            setUserState("gambler")
            navigate("/")
            break;
          case 1:
            alert("Admin Logged in")
            setUserState("admin")
            navigate("/")
            break;
          case 2:
            alert("Expert Logged in")
            setUserState("expert")
            navigate("/")
            break;
          default:
            alert("Error logging in")
            setUserState("loggedOff")
        }
      })
      .catch(error => alert("Error logging in"));
  }



  return (
    <>
      <div className='greenBackGround'>
        <div className='white-box'>
          <div className='container'>
            <form className="container-form" onSubmit={handleSubmit}>
              <h1>BEM VINDO</h1>

              <input type="txtL" placeholder="E-mail" value={email} onChange={(e) => setEmail(e.target.value)} required />
              <input type="txtL" placeholder="Palavra-passe" value={password} onChange={(e) => setPassword(e.target.value)} required />

              <Link to='/password_recovery' className='registerbutton'>
                Esqueci-me da palavra-passe
              </Link>
              <Button type="submit" buttonSize='btn--flex'>Aceder</Button>

            </form>
            Não tem conta?
            <Link to='/registo' className='registerbutton'>
              Registe-se ja!
            </Link>
          </div>
          <img className='imagem'
            src={'images/fallingmoney.jpg'}
            alt={''}
          />
        </div>
      </div>
    </>
  );
}

export default Login;
