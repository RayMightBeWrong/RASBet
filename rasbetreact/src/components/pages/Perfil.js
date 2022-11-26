import React from 'react';
import './Perfil.css';
import { Button } from '../Button';
import { CarteiraSimplificada } from '../Carteira';

const carteira1 = { ratioEuro: "0.35", balance: "30" }
const carteira2 = { ratioEuro: "1", balance: "700" }
const carteiras = [carteira1, carteira2]
const nome = "Carteiro Paulo"
const saldo = "13"


function Perfil() {

const handleClick=(e)=>{
    e.preventDefault()
    const perfil={nome,apelido,psw}
    console.log(perfil)
    fetch("http://localhost:8080/api/users/gambler/update",{
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(perfil)}).then(()=>{
    console.log("Alterações Gravadas")})
}

const handleClick=(ratioEuro,balance)=>{
    e.preventDefault()
    const carteira={ratioEuro,balance}
    console.log(carteira)
    fetch("http://localhost:8080/api/wallets/wallet",{
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(carteira)}).then(()=>{
    console.log("Nova Carteira adicionada")})
}

  return (
    <div className='perfil'>
      <div className='perfil-box'>
        <h1>{nome}</h1>
        <h2>Saldo total: {saldo}€</h2>
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
            <t>Antiga palavra-passe:</t>
            <input type="txtP" placeholder="Palavra-passe" name="psw" required />
          </s1>
          <s1>
            <t>Nova palavra-passe:</t>
            <input type="txtP" placeholder="Palavra-passe" name="psw" required />
          </s1>
          <s1>
            <t>Confirme:</t>
            <input type="txtP" placeholder="Palavra-passe" name="psw" required />
          </s1>
          <div className='save'>
            <Button buttonSize={'btn--medium'} onclick=(handleClick) >Gravar Alterações</Button>
          </div>
        </div>
        <h1> Informação das carteiras </h1>
        <div className='carteiras-body'>
          {carteiras.map(wallet => (
            <CarteiraSimplificada ratioEuro={wallet.ratioEuro} balance={wallet.balance} />
          ))}
        </div>
        <div className='save'>
          <Button buttonSize={'btn--medium'} onclick=(handleClick(wallet.ratioEuro,wallet.balance)) >Nova Carteira</Button>
        </div>
      </div>
    </div>
  );
}

export default Perfil;