import React, { useState, useEffect } from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link, useNavigate } from 'react-router-dom';

function ConsultaPerfil({
    userState,
    setUserState,
    setUserId
}) {

    const navigate = useNavigate();
    const [email, setEmail] = useState('')

    const handleSubmit = event => {
        event.preventDefault()
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }

        fetch("http://localhost:8080/api/users/gambler?email=" + email, requestOptions)
            .then(res => res.json())
            .then(result => {
                console.log(result)
                alert("Changed to gambler profile")
                setUserState("gambler")
                setUserId(result.user_id)
                navigate("/")
            })
            .catch(error => {
                alert("Email not found")
                console.log(error)
                console.log("yoo")
            });
    }

    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <form className="container-form" onSubmit={handleSubmit}>
                            <h1>Consulta de perfil</h1>
                            Insere o email do utilizador

                            <input type="txtL" placeholder="user@gmail.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                            <Button type="submit" buttonSize='btn--flex'>Aceder</Button>
                        </form>
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default ConsultaPerfil;
