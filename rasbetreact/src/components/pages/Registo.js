import React from 'react';
import './Login.css';
import { Button } from '../Button';

function Registo() {
    return (
        <>
            <div className='registo'>
                <div className='white-box'>
                    <div className='container'>
                        <form action="/action_page.php" class="container-form">
                            <h1>Registo</h1>

                            <input type="text" placeholder="E-mail" name="email" required />
                            <input type="text" placeholder="Palavra-passe" name="psw" required />
                            <input type="text" placeholder="Data de Nascimento" name="dataNascimento" required />
                            <input type="text" placeholder="NIF" name="nif" required />

                            <Button type="submit" buttonSize='btn--flex'>Concluir</Button>

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
}

export default Registo;
