import React from 'react';
import './Login.css';
import { Button } from '../Button';
import { Link } from 'react-router-dom';

function ConsultaPerfil({
    userState
}) {
    if (userState === "admin") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        <form action="/action_page.php" class="container-form">
                            <h1>Procurar utilizador</h1>

                            <input type="txtL" placeholder="Nome" name="nome" required />
                            <Button type="submit" buttonSize='btn--flex'>Procurar</Button>
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
