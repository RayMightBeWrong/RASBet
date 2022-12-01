import React, { useState, useEffect } from 'react';
import "./MoedasPopUp.css";
import { NewWallet } from "./NewWallet";

export const MoedasPopUp = ({
    closePopup,
    userId
}) => {

    const [moedas, setMoedas] = useState([]);
    const [selected, setSelected] = useState([]);

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/wallets/coin/", requestOptions)
        .then(res => res.json())
        .then((curr) => { console.log(curr);
            fetch("http://localhost:8080/api/wallets/gambler/" + userId, requestOptions)
            .then(res => res.json())
            .then((gw) => {
                let flag
                let currLeft = []
                curr.forEach(c => {
                    flag = false
                    gw.forEach(w => {
                        if (w.coin.id === c.id) flag = true
                    })
                    if (!flag) 
                        currLeft.push(c)
                })
                setMoedas(currLeft)
            })
            .catch(err => console.log(err))
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
            <NewWallet 
                options={moedas}
                setSelected={setSelected}
                create={() => criaCarteira(selected)}
                rBack={closePopup}
            />
        </>
    );
};
