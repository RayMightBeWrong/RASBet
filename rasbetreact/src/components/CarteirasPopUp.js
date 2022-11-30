import React, { useState, useEffect } from 'react';
import "./CarteirasPopUp.css";
import { Carteira } from './Carteira';
import { Button } from './Button';

export const CarteirasPopUp = ({
    valormin,
    closePopup
}) => {

    const [carteiras, setCarteiras] = useState([]);

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/wallets/gambler/2", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setCarteiras(result)
            }
            )
    }, [])


    const handleClick = (e) => {
        e.preventDefault()
        const carteira = { ratioEuro: "", balance: "" } //TODO
        console.log(carteira)
        fetch("http://localhost:8080/api/wallets/wallet", { //TODO
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(carteira)
        })
            .then(() => { console.log("Nova Carteira Criada") })
    }

    const verificaCarteira = (valor) => {
        console.log("yoo")
        if (parseFloat(valormin) <= parseFloat(valor) || valormin === '') {
            alert('Aposta inserida'); {/*todo verificar se aposta é possível no backend*/ }
            closePopup()
        }
        else { alert('Dinheiro insuficiente'); }
    }
    return (
        <>
            < div className="carteirasPopUp-container" >
                < div className="carteirasPopUp-body" >
                    <h1> Seleção de carteiras </h1>
                    {carteiras.map(wallet => (
                        <div><Carteira ratioEuro={wallet.ratioEuro} balance={wallet.balance} verificaCarteira={verificaCarteira} /></div>
                    ))}

                    <Button buttonStyle={"btn--bet"} onClick={() => handleClick}> {/* todo on click diminui */}
                        Criar carteira
                    </Button>
                    <button onClick={closePopup}>Fechar menu das carteiras</button >

                </div>
            </div >
        </>
    );
};