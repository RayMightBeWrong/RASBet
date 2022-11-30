import React, { useState, useEffect } from 'react';
import "./CarteirasPopUp.css";
import { Carteira } from './Carteira';
import { Button } from './Button';
import { MoedasPopUp } from './MoedasPopUp';

export const CarteirasPopUp = ({
    valormin,
    closePopup,
    userId
}) => {

    const [carteiras, setCarteiras] = useState([]);
    const [open, setOpen] = useState(false);

    useEffect(() => {
        if (!open) {
            const requestOptions = {
                method: 'GET',
                headers: { "Content-Type": "application/json" }
            }
            fetch("http://localhost:8080/api/wallets/gambler/" + userId, requestOptions)
                .then(res => res.json())
                .then((result) => {
                    setCarteiras(result)
                })
        }
    }, [userId, open])

    /*
    const handleClick = (e) => {
        e.preventDefault()
        const carteira = { coin_id: "" }
        console.log(carteira)
        fetch("http://localhost:8080/api/wallets/wallet", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(carteira)
        })
            .then(() => { console.log("Nova Carteira Criada") })
    }
    */
    const handleClick = () => {
        console.log("setopentrue")
        setOpen(true)
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
                        <div><Carteira wallet_id={wallet.id} coinId={wallet.coin.id} balance={wallet.balance} verificaCarteira={() => verificaCarteira()} /></div>
                    ))}

                    <Button buttonStyle={"btn--bet"} onClick={() => handleClick()}>
                        Criar carteira
                    </Button>
                    <button onClick={closePopup}>Fechar menu das carteiras</button >
                    {open ?
                        < MoedasPopUp userId={userId} closePopup={() => setOpen(false)} />
                        : null
                    }
                </div>
            </div >
        </>
    )
}