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
  const [open, setOpen] = useState(false);

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

  const currency = useState([])

  carteiras.forEach((elem) => (currency.push(elem.currency)))

  useEffect(() => {
    const requestOptions = {
      method: 'GET',
      headers: { "Content-Type": "application/json" }
    }
    let s = "http://localhost:8080/api/users/gambler?id=" + userId
    fetch(s, requestOptions)
      .then(res => res.json())
      .then((result) => {
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
  }, [])

  return (
    <div className='perfil'>
      <div className='perfil-box'>
        <h1>{nome}</h1>
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
            <Button buttonSize={'btn--medium'}  >Gravar Alterações</Button>
          </div>
        </div>
        <h1> Informação das carteiras </h1>
        <div className='carteiras-body'>
          {carteiras.map(wallet => (
            <CarteiraSimplificada ratioEuro={wallet.ratioEuro} balance={wallet.balance} />
          ))}
        </div>
        <div className='save'>
          <Button buttonSize={'btn--medium'} onClick={() => setOpen(true)}>Nova Carteira</Button>
          <Link to='/historico' className='registerbutton'>
            <Button buttonSize={'btn--medium'} buttonStyle={'btn--inverted'} onClick={() => setOpen(true)}>
              Consultar Historico
            </Button>
          </Link>
        </div>
        {open ?
            <PayMethod options={currency} closePopup={() => setOpen(false)} rBack={() => setOpen(false)} />
          : null
        }
      </div>
    </div >
  );
}

export default Perfil;