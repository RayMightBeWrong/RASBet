import React, { useState, useEffect } from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';
import { NewCoinPopUp } from '../../NewCoinPopUp'
import { Coin } from '../../objects/Coin';

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
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div className='container'>
                            <div className="titulo-comons">
                                <h1>Coins</h1>
                            </div>
                            {moedas.map(coin => (
                                <Coin coinName={coin.id} ratioEuro={coin.ratio_EUR} />
                            ))}
                            <br/>

                            {open ?
                                <NewCoinPopUp closePopup={() => setOpen(false)} />
                                : 
                                <Button
                                    buttonSize={'btn--large'}
                                    onClick={() => setOpen(true)}>
                                    Nova Moeda
                                </Button>
                            }
                        </div>
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default CriaMoeda;
