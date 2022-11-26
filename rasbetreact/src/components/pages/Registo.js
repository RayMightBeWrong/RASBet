import React from 'react';
import './Login.css';
import { Button } from '../Button';

function Registo({
    expertMode,
    userState
}) {

const handleClick=(e)=>{
    if (expertMode === "false"){
        e.preventDefault()
        const registo={email,psw,dataNascimento,nif}
        console.log(registo)
        fetch("http://localhost:8080/api/users/gambler",{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(registo)
        }).then(()=>{
            console.log("Nova Conta Registada")})
    }
    else{
        e.preventDefault()
        const registo={email,psw}
        console.log(registo)
        fetch("http://localhost:8080/api/users/expert",{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(registo)
        }).then(()=>{
            console.log("Nova Conta Registada de Expert")})
    }
}
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

                                <Button type="submit" buttonSize='btn--flex' onclick=(handleClick)>Concluir</Button>

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
                            <form action="/action_page.php" class="container-form">
                                <h1>Registo</h1>

                                <input type="txtL" placeholder="E-mail" name="email" required />
                                <input type="txtL" placeholder="Palavra-passe" name="psw" required />

                                <Button type="submit" buttonSize='btn--flex' onclick=(handleClick)>Concluir</Button>

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
