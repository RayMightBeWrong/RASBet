import React, { useState, useEffect } from 'react';
import "./MoedasPopUp.css";
import { Moeda } from "./Moeda.js"
import { Button } from './Button';

export const MoedasPopUp = ({
    closePopup,
    userId
}) => {

    const [moedas, setMoedas] = useState([]);

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/wallets/coin/", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setMoedas(result)
            })
    }, [])


    const criaCarteira = (coin_id) => {
        console.log("User id:" + userId)
        const carteira = {
            "coin_id": coin_id, "gambler_id": userId
        }
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(carteira)
        }
        fetch("http://localhost:8080/api/wallets/wallet/", requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Carteira criada")
                }
            })
            .catch(_ => alert("Error occured"))
    }

    return (
        <>
            < div className="moedasPopUp-container" >
                < div className="moedasPopUp-body" >
                    <h1> Seleção de moedas </h1>
                    {moedas.map(moeda => (
                        <div><Button onClick={() => criaCarteira(moeda.id)}><Moeda id={moeda.id} ratio_EUR={moeda.ratio_EUR} /></Button></div>
                    ))}

                    <button onClick={() => closePopup()}>Fechar menu das moedas</button >

                </div>
            </div >
        </>
    );
};