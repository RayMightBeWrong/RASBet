import React, {useEffect, useState} from 'react';
import './Perfil.css';
import { Button } from '../Button';
import { CarteiraSimplificada } from '../Carteira';
import { PayMethod } from '../PayMethods'
import { Link } from 'react-router-dom';

const carteira1 = { ratioEuro: "0.35", balance: "30" }
const carteira2 = { ratioEuro: "1", balance: "700" }
const carteiras = [carteira1, carteira2]
const nome = "Carteiro Paulo"
const saldo = "13"


function Perfil({
  userId
}) {
  const [open, setOpen] = useState(-1);

  const [currentName, setCurrentName] = useState("");
  const [name, setName] = useState("");
  const [psw, setPsw] = useState("");
  const [email, setEmail] = useState("");
  const [cc, setCc] = useState("");
  const [nationality, setNationality] = useState("");
  const [nif, setNif] = useState("");
  const [city, setCity] = useState("");
  const [address, setAddress] = useState("");
  const [phone_number, setPhone_number] = useState("");
  const [occupation, setOccupation] = useState("");
  const [date_of_birth, setDate_of_birth] = useState("");
  const [postal_code, setPostal_code] = useState("");

  const [currency,setCurrency] = useState([])
  const [gamblerWallets,setGamblerWallets] = useState([])


  const handleChange = event => {
      const result = event.target.value.replace(/\D/g, '');
      console.log(result);
  };

  const handleClick = () =>{}

  useEffect(() => {
    const requestOptions = {
      method: 'GET',
      headers: { "Content-Type": "application/json" }
    }

    fetch("http://localhost:8080/api/wallets/coin/", requestOptions)
    .then(res => res.json())
    .then((curr) => {
      fetch("http://localhost:8080/api/wallets/gambler/" + userId, requestOptions)
      .then(res => res.json())
      .then((gw) => {
        let flag
        let currLeft = []
        curr.forEach(c => {
          flag = false
          gw.forEach(w => {
            if (w.coin.id === c.id) { 
              w.coin["currency"] = c.ratio_EUR
              flag = true
            }
          })
          if (!flag) 
            currLeft.push(c.id)
        })
        console.log("gambler wallets:",gw,"\ncurrency:", currLeft)
        setGamblerWallets(gw)
        setCurrency(currLeft)
      })
    })

    

    fetch("http://localhost:8080/api/users/gambler?id=" + userId, requestOptions)
    .then(res => res.json())
    .then((result) => {
      setCurrentName(result.name)
      setName(result.name)
      setPsw(result.password)
      setEmail(result.email)
      setCc(result.cc)
      setNationality(result.nationality)
      setNif(result.nif)
      setCity(result.city)
      setAddress(result.address)
      setPhone_number(result.phone_number)
      setOccupation(result.occupation)
      setDate_of_birth(result.date_of_birth)
      setPostal_code(result.postal_code)
    })
  }, [userId])



  return (
    <div className='perfil'>
      <div className='perfil-box'>
        <h1>{currentName}</h1>
        <div className='informacoes-perfil'>
          <div className='informacoes-perfil-content'>
            <h3>Nome:</h3>
            <input type="txtP" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Email:</h3>
            <input type="txtP" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Número De Telemóvel:</h3>
            <input type="txtP" placeholder="Phone_number" value={phone_number} onChange={(e) => setPhone_number(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Cartão De Cidadão:</h3>
            <input type="txtP" placeholder="Cc" value={cc} onChange={(e) => setCc(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Ocupação:</h3>
            <input type="txtP" placeholder="Occupation" value={occupation} onChange={(e) => setOccupation(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>NIF:</h3>
            <input type="txtP" placeholder="Nif" value={nif} onChange={(e) => setNif(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Nacionalidade:</h3>
            <input type="txtP" placeholder="Nationality" value={nationality} onChange={(e) => setNationality(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Cidade:</h3>
            <input type="txtP" placeholder="City" value={city} onChange={(e) => setCity(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Morada:</h3>
            <input type="txtP" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Data De Nascimento:</h3>
            <input type="txtP" placeholder="Date_of_birth" value={date_of_birth} onChange={(e) => setDate_of_birth(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Código Postal:</h3>
            <input type="txtP" placeholder="Postal_code" value={postal_code} onChange={(e) => setPostal_code(e.target.value)}  required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Nova palavra-passe:</h3>
            <input type="txtP" placeholder="Passeword" value={psw} onChange={(e) => setPsw(e.target.value)}  required />
          </div>
          <div className='save'>
            <Button buttonSize={'btn--medium'} onClick={() => handleClick()} >Gravar Alterações</Button>
          </div>
        </div>
        <h1> Informação das carteiras </h1>
        {gamblerWallets.map(wallet => (
          <div className='carteiras-body' key={wallet.id}>
            <CarteiraSimplificada ratioEuro={wallet.coin.currency} balance={wallet.balance} coin={wallet.coin.id} setOpen={() => setOpen(wallet.id)}/>
          </div>
        ))}
        <div className='save'>
          {currency.length !== 0 ?
              <Button buttonSize={'btn--medium'} onClick={() => setOpen(0)}>Nova Carteira</Button>
            :
              null
          }
          <Link to='/historico' className='registerbutton'>
            <Button buttonSize={'btn--medium'} buttonStyle={'btn--inverted'} onClick={() => setOpen(true)}>
              Consultar Historico
            </Button>
          </Link>
        </div>
        {open !== -1 ?
            <PayMethod options={open===0 ? currency : [gamblerWallets[open-1].coin.id]} closePopup={() => setOpen(0)} rBack={() => setOpen(-1)} />
          :
            null
        }
      </div>
    </div >
  );
}

export default Perfil;