import React, { useState, useEffect } from 'react';
import './Login.css';
import { Button } from '../Button';
import { Link } from 'react-router-dom';
import { NewCoinPopUp } from '../NewCoinPopUp'
import "../PayMethods.css"

function CriaMoeda({
    userState
}) {

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

    const [open, setOpen] = useState(false);
    if (userState === "admin") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        Coins
                        <div className='carteirasPopUp-list'>
                            {moedas.map(coin => (
                                <div key={coin.id}>
                                    Coin name: {coin.id} | Ratio Euro: {coin.ratio_EUR}
                                </div>
                            ))}
                        </div>
                        {open ?
                            <NewCoinPopUp closePopup={() => setOpen(false)} />
                            : <Button buttonStyle={"btn--bet"}
                                buttonSize={'btn--large'}
                                onClick={() => setOpen(true)}>
                                Nova Moeda
                            </Button>
                        }
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default CriaMoeda;
