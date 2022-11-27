import React, { useState, useEffect } from 'react';
import './Login.css';
import { Link } from 'react-router-dom';
import { Button } from '../Button';

function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = event => {
    console.log("yoooooooooo")
    /*"localhost:8080/api/users?email=" + email + "&password=" + password*/

    const requestOptions = {
      method: 'GET',
      headers: { "Content-Type": "application/json" },
      redirect: 'follow'
    };

    fetch("localhost:8080/api/users/login?email=tiago@hotmail.com&password=tiago", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result))
      .catch(error => console.log('error', error));
  }



  return (
    <>
      <div className='login'>
        <div className='white-box'>
          <div className='container'>
            <form class="container-form" onSubmit={handleSubmit}>
              <h1>BEM VINDO</h1>

              <input type="txtL" placeholder="E-mail" value={email} onChange={(e) => setEmail(e.target.value)} required />
              <input type="txtL" placeholder="Palavra-passe" value={password} onChange={(e) => setPassword(e.target.value)} required />

              <Link to='/password_recovery' className='registerbutton'>
                Esqueci-me da palavra-passe
              </Link>
              <Button type="submit" buttonSize='btn--flex'>Aceder</Button>

            </form>
            <Button type="submit" buttonSize='btn--flex' onClick={handleSubmit}>Teste</Button>
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
