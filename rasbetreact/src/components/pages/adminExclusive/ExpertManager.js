import React, { useState, useEffect } from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';

function ExpertManager({
    userState
}) {

    const [users, setUsers] = useState([])

    const [rerender, setRerender] = useState(false)

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/users/expert/all/", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setUsers(result)
            })
    }, [rerender])

    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div>
                            <form className="container-form" onSubmit={handleSubmit}>
                                <h1>Consulta de perfil</h1>
                                Insere o email do utilizador

                                <input type="txtL" placeholder="user@gmail.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                <Button type="submit" buttonSize='btn--flex'>Aceder</Button>
                            </form>
                        </div>
                    </div>
                </div>
            </>
        )
    } else {
        return ("")
    }
}

export default ExpertManager;
