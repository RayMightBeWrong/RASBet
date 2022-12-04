import React, { useState } from 'react';
import './Comons.css';
import { Button } from '../Button';

function Registo({
    expertMode,
    userState
}) {

    const [name, setName] = useState("")
    const [nif, setNif] = useState("")
    const [cc, setCc] = useState("")
    const [psw, setPsw] = useState("")
    const [email, setEmail] = useState("")
    const [nationality, setNationality] = useState("")
    const [city, setCity] = useState("")
    const [address, setAddress] = useState("")
    const [phone_number, setPhone_number] = useState("")
    const [occupation, setOccupation] = useState("")
    const [postal_code, setPostal_code] = useState("")
    const [date_of_birth, setDate_of_birth] = useState("")
    const [referral, setReferral] = useState("")


    const handleSubmit = event => {
        event.preventDefault()
        if (expertMode === "false") {
            const registo = {
                name: name, password: psw, email: email, cc: cc,
                nationality: nationality, nif: nif, city: city, address: address, phone_number: phone_number,
                occupation: occupation, date_of_birth: date_of_birth, postal_code: postal_code
            }
            console.log(registo)

            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(registo)
            }
            let link = "http://localhost:8080/api/users/gambler"
            if (referral !== "")
                link += "?referral=" + referral
            fetch(link, requestOptions)
                .then(res => {
                    if (res.status !== 200) {
                        let errorMsg;
                        if ((errorMsg = res.headers.get("x-error")) == null)
                            errorMsg = "Error occured"
                        alert(errorMsg)
                    }
                    else {
                        alert("Gambler account created successfully")
                    }
                })
                .catch(_ => alert("Error occured"))
        }
        else {
            const registo = {
                name: name,
                password: psw,
                email: email
            }

            const requestOptions = {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(registo)
            }
            fetch("http://localhost:8080/api/users/expert", requestOptions)
                .then(res => {
                    if (res.status !== 200) {
                        let errorMsg;
                        if ((errorMsg = res.headers.get("x-error")) == null)
                            errorMsg = "Error occured"
                        alert(errorMsg)
                    }
                    else {
                        alert("Expert account created successfully")
                    }
                })
                .catch(_ => alert("Error occured"))
        }
    }


    if (expertMode === "false") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div className='container'>
                            <form className="container-form" onSubmit={handleSubmit}>
                                <h1>Registo</h1>
                                Name <input type="txtL" placeholder="Cristiano Ronaldo" value={name} onChange={(e) => setName(e.target.value)} required />
                                Password <input type="txtL" placeholder="*****" value={psw} onChange={(e) => setPsw(e.target.value)} required />
                                Email <input type="txtL" placeholder="cr7@gmail.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                Cartão Cidadão <input type="txtL" placeholder="12345678" value={cc} onChange={(e) => setCc(e.target.value)} required />
                                Nationality <input type="txtL" placeholder="Portugal" value={nationality} onChange={(e) => setNationality(e.target.value)} required />
                                NIF <input type="txtL" placeholder="238788888" value={nif} onChange={(e) => setNif(e.target.value)} required />
                                City <input type="txtL" placeholder="Funchal" value={city} onChange={(e) => setCity(e.target.value)} required />
                                Address<input type="txtL" placeholder="Rua cidade do Porto, porta nº17 8ºA" value={address} onChange={(e) => setAddress(e.target.value)} required />
                                Phone Number <input type="txtL" placeholder="930321123" value={phone_number} onChange={(e) => setPhone_number(e.target.value)} required />
                                Occupation <input type="txtL" placeholder="Football player" value={occupation} onChange={(e) => setOccupation(e.target.value)} required />
                                Date of birth <input type="txtL" placeholder="1985-02-05" value={date_of_birth} onChange={(e) => setDate_of_birth(e.target.value)} required />
                                Postal code <input type="txtL" placeholder="4710-057" value={postal_code} onChange={(e) => setPostal_code(e.target.value)} required />
                                Referral <input type="txtL" placeholder="31231" value={referral} onChange={(e) => setReferral(e.target.value)} required />
                                <Button buttonSize='btn--flex' type="submit">Concluir</Button>

                            </form>
                        </div>
                        <img className='imagem'
                            src={'images/fallingmoney.jpg'}
                            alt={''}
                        />
                    </div>
                </div>
            </>
        );
    } else if (expertMode === "true" && userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div className='container'>
                            <form className="container-form" onSubmit={handleSubmit}>
                                <h1>Registo</h1>
                                Name <input type="txtL" placeholder="Cristiano Ronaldo" value={name} onChange={(e) => setName(e.target.value)} required />
                                Password <input type="txtL" placeholder="*****" value={psw} onChange={(e) => setPsw(e.target.value)} required />
                                Email <input type="txtL" placeholder="cr7@gmail.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                <Button buttonSize='btn--flex' type="submit">Concluir</Button>

                            </form>
                        </div>
                        <img className='imagem'
                            src={'../images/fallingmoney.jpg'}
                            alt={''}
                        />
                    </div>
                </div>
            </>
        );
    }
    else return;
}

export default Registo;
