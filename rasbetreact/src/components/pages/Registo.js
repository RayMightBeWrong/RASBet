import React from 'react';
import './Login.css';
import { Button } from '../Button';

function Registo({
    expertMode
}) {
    if (expertMode === "false") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        <div className='container'>
                            <form action="/action_page.php" class="container-form">
                                <h1>Registo</h1>

                                <input type="txtL" placeholder="E-mail" name="email" required />
                                <input type="txtL" placeholder="Palavra-passe" name="psw" required />
                                <input type="txtL" placeholder="Data de Nascimento" name="dataNascimento" required />
                                <input type="txtL" placeholder="NIF" name="nif" required />

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
    } else {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        <div className='container'>
                            <form action="/action_page.php" class="container-form">
                                <h1>Registo</h1>

                                <input type="txtL" placeholder="E-mail" name="email" required />
                                <input type="txtL" placeholder="Palavra-passe" name="psw" required />

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
}

export default Registo;
