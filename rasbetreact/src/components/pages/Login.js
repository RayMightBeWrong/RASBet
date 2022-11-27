import React from 'react';
import './Login.css';
import { Link } from 'react-router-dom';
import { Button } from '../Button';

function Login() {

  const handleClick = (e) => {
    e.preventDefault()
    const login = { email: "", psw: "" }
    console.log(login)
    fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(login)
    }).
      then(() => {
        console.log("Login da Conta")
      })
  }

  return (
    <>
      <div className='login'>
        <div className='white-box'>
          <div className='container'>
            <form action="/action_page.php" class="container-form">
              <h1>BEM VINDO</h1>

              <input type="txtL" placeholder="E-mail" name="email" required />

              <input type="txtL" placeholder="Palavra-passe" name="psw" required />
              <Link to='/password_recovery' className='registerbutton'>
                Esqueci-me da palavra-passe
              </Link>
              <Button type="submit" buttonSize='btn--flex' onclick={() => handleClick} >Aceder</Button>

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
