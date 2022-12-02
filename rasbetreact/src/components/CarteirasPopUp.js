import React, { useState, useEffect } from 'react';
import "./CarteirasPopUp.css";
import { Carteira } from './objects/Carteira';
import { Button } from './Button';
import { MoedasPopUp } from './MoedasPopUp';
import "./PayMethods.css"

export const CarteirasPopUp = ({
    valormin,
    closePopup,
    userId,
    bets
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

    const handleClick = () => {
        setOpen(true)
    }

    function verificaCarteira(wallet_id, coin_id) {
        let game_choices = []
        bets.map(bet => (
            game_choices.push({
                game_id: bet.id,
                participant_id: bet.participantId
            })
        ))

        let bet = {
            value: parseFloat(valormin), //pode dar erro maybe
            gambler_id: userId,
            wallet_id: wallet_id,
            coin_id: coin_id,
            coupon: null,
            game_choices: game_choices
        }

        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bet)
        }
        fetch("http://localhost:8080/api/bets/", requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Bet criada")
                    closePopup()
                }
            })
            .catch(_ => alert("Error occured"))
    }
    return (
        <>
            < div className="carteirasPopUp-container" >
                < div className="carteirasPopUp-body" >
                    <div className="payMethods-close-button">
                        <div className="pmAline"> </div>
                        <h1> Seleção de carteiras </h1>
                        <Button buttonStyle={'btn--flex'} onClick={closePopup}> X </Button >
                    </div>

                    <h3> Aposta do valor de {valormin}</h3>
                    <div className='carteirasPopUp-list'>
                        {carteiras.map(wallet => (
                            <div key={wallet.id}>
                                <Carteira wallet_id={wallet.id} verificaCarteira={() => verificaCarteira(wallet.id, wallet.coin.id)} />
                            </div>
                        ))}
                    </div>
                    <Button buttonStyle={"btn--inverted"} onClick={() => handleClick()}>
                        Criar carteira
                    </Button>
                    {open ?
                        < MoedasPopUp userId={userId} closePopup={() => setOpen(false)} />
                        : null
                    }
                </div>
            </div >
        </>
    )
}