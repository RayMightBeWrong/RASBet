import React from 'react';
import './Login.css';
import { Button } from '../Button';

function Registo({
    expertMode,
    userState
}) {

    const handleSubmit = event => {
        event.preventDefault()
        if (expertMode === "false") {
            const registo = {
                name: "Tiago Martins", password: "tiago", email: "tiago@hotmail.com", cc: 30557672,
                nationality: "Portuguese", nif: 238788888, city: "Braga", address: "Rua", phone_number: 961324242,
                occupation: "Student", date_of_birth: "2001-07-29", postal_code: "4705-651"
            }
            console.log(registo)
            fetch("http://localhost:8080/api/users/gambler", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(registo)
            }).then(
                res => console.log('An error occurred.', res)
            )
        }
        else {
            const registo = { email: "", psw: "" }
            console.log(registo)
            fetch("http://localhost:8080/api/users/expert", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(registo)
            }).then(() => {
                console.log("Nova Conta Registada de Expert")
            })
        }
    }


    if (expertMode === "false") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        <div className='container'>
                            <form class="container-form" onSubmit={handleSubmit}>
                                <h1>Registo</h1>

                                <input type="txtL" placeholder="E-mail" name="email" required />
                                <input type="txtL" placeholder="Palavra-passe" name="psw" required />
                                <input type="txtL" placeholder="Data de Nascimento" name="dataNascimento" required />
                                <input type="txtL" placeholder="NIF" name="nif" required />

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
                <div className='registo'>
                    <div className='white-box'>
                        <div className='container'>
                            <form class="container-form" onSubmit={handleSubmit}>
                                <h1>Registo</h1>

                                <input type="txtL" placeholder="E-mail" name="email" required />
                                <input type="txtL" placeholder="Palavra-passe" name="psw" required />

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
    }
    else return;
}

export default Registo;
