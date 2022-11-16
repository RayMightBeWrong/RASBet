import React from 'react';
import './Perfil.css';
import { Link } from 'react-router-dom';
import { Button } from '../Button';

function Perfil() {
  return (
    <div className='perfil'>
      <div className='perfil-box'>
        <h1>Carlos Pereira</h1>
        <h2>Saldo:</h2>
        <hr class='sp'></hr>
        <div className='levantar-depositar'>
          <Button className='btn' buttonStyle={"btn--inverted"} buttonSize={'btn--flex'}>Levantar</Button>
          <Button className='btn' buttonStyle={"btn--inverted"} buttonSize='btn--flex'>Depositar</Button>
        </div>
        <div className='informacoes-perfil'>
          <s1>
            <t>Nome:</t>
            <input type="txtP" placeholder="Nome" name="nome" required />
          </s1>
          <s1>
            <t>Apelido:</t>
            <input type="txtP" placeholder="Apelido" name="apelido" required />
          </s1>
          <s1>
            <t>Mudar palavra-passe:</t>
            <input type="txtP" placeholder="Palavra-passe" name="psw" required />
          </s1>
        </div>
        <div className='save'>
          <Button  buttonSize={'btn--medium'}>Gravar Alterações</Button>
        </div>
      </div>
    </div>
  );
}

export default Perfil;