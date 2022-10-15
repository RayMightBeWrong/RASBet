import React from 'react';
import './WhiteBox.css';
import { Link } from 'react-router-dom';
import { Button } from './Button';

function WhiteBox() {
  return (
    <div className='white-box'>
      <div className='container'>
        <form action="/action_page.php" class="container-form">
        <img className='icon'
        src={'images/icon.png'}
        alt={''}
        />
      <h1>BEM VINDO</h1>

          <input type="text" placeholder="E-mail" name="email" required/>

          <input type="password" placeholder="Palavra-passe" name="psw" required/>

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
  );
}

export default WhiteBox;
 