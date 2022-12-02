import React, { useEffect, useState } from 'react';
import './Perfil.css';
import { Button } from '../../Button';
import { InfoCarteiras } from '../../InfoCarteiras';


function Perfil({
  userId
}) {

  const [currentName, setCurrentName] = useState("");
  const [name, setName] = useState("");
  const [psw, setPsw] = useState("");
  const [email, setEmail] = useState("");
  const [nationality, setNationality] = useState("");
  const [city, setCity] = useState("");
  const [address, setAddress] = useState("");
  const [phone_number, setPhone_number] = useState("");
  const [occupation, setOccupation] = useState("");
  const [postal_code, setPostal_code] = useState("");



  const handleClick = () => {
    let requestOptions = {
      method: 'PUT',
      headers: { "Access-Control-Allow-Origin": "*" }
    }
    let s = "http://localhost:8080/api/users/gambler/update?id=" + userId
    s += "&name=" + name
    s += "&password=" + psw
    s += "&email=" + email
    s += "&nationality=" + nationality
    s += "&city=" + city
    s += "&address=" + address
    s += "&phone_number=" + phone_number
    s += "&occupation=" + occupation
    s += "&postal_code=" + postal_code
    console.log(s);
    fetch(s, requestOptions)
      .then(res => {
        if (res.status !== 200) {
          var errorMsg;
          if ((errorMsg = res.headers.get("x-error")) == null)
            errorMsg = "Error occured"
          alert(errorMsg)
        }
        else {
          alert("Registos atualizados")
        }
      })
      .catch(err => alert(err))
  }

  useEffect(() => {
    let requestOptions = {
      method: 'GET',
      headers: { "Content-Type": "application/json" }
    }
    fetch("http://localhost:8080/api/users/gambler?id=" + userId, requestOptions)
      .then(res => res.json())
      .then((result) => {
        setCurrentName(result.name)
        setName(result.name)
        setPsw(result.password)
        setEmail(result.email)
        setNationality(result.nationality)
        setCity(result.city)
        setAddress(result.address)
        setPhone_number(result.phone_number)
        setOccupation(result.occupation)
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
            <input type="txtP" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Email:</h3>
            <input type="txtP" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Número De Telemóvel:</h3>
            <input type="txtP" placeholder="Phone_number" value={phone_number} onChange={(e) => setPhone_number(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Ocupação:</h3>
            <input type="txtP" placeholder="Occupation" value={occupation} onChange={(e) => setOccupation(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Nacionalidade:</h3>
            <input type="txtP" placeholder="Nationality" value={nationality} onChange={(e) => setNationality(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Cidade:</h3>
            <input type="txtP" placeholder="City" value={city} onChange={(e) => setCity(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Morada:</h3>
            <input type="txtP" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Código Postal:</h3>
            <input type="txtP" placeholder="Postal_code" value={postal_code} onChange={(e) => setPostal_code(e.target.value)} required />
          </div>
          <div className='informacoes-perfil-content'>
            <h3>Nova palavra-passe:</h3>
            <input type="txtP" placeholder="Password" value={psw} onChange={(e) => setPsw(e.target.value)} required />
          </div>
          <div className='save'>
            <Button buttonSize={'btn--medium'} onClick={() => handleClick()} >Gravar Alterações</Button>
          </div>
        </div>
        <InfoCarteiras userId={userId} />
      </div>
    </div >
  );
}

export default Perfil;