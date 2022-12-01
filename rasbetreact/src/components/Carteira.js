import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from './Button';
import { PayMethod } from "./PayMethods"



export const Carteira = ({
    coinId,
    balance,
    wallet_id,
    verificaCarteira,
}) => {
    const [value, setValue] = useState('')

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '')

        setValue(result)
    }

    const depositarDinheiro = () => {
        console.log("yooo")
        const requestOptions = {
            method: "PUT",
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/transactions/deposit?wallet_id=" + wallet_id + "&value=" + value, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Deposito bem sucedido")
                }
            })
            .catch(_ => alert("Error occured"))
    }

    return (
        <>
            <div className='carteira'>
                <div className='carteira-header'>
                    <div>{coinId}</div>
                    <div>Balance: {balance}</div>
                </div>
                <div className='carteira-body'>
                    <div className='carteira-transferencia'>
                        <input type="text"
                            required
                            placeholder="Quantia de dinheiro"
                            value={value}
                            onChange={handleChange} />
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            /*onclick={() => handleClick(wallet_id, value, gambler_id, coupon)} */> {/* todo on click diminui */}
                            Levantar
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => depositarDinheiro()}
                        > {/*todo on click aumenta*/}
                            Depositar
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            /*onclick={() => handleClick(wallet_id, value, gambler_id, coupon)} */> {/*todo on click aumenta*/}
                            Cupão
                        </Button>
                    </div>
                </div >
                <div>
                    <Button buttonStyle={"btn"}
                        buttonSize={'btn--flex'}
                        /*onClick={() => handleClick(balance)}*/>
                        x
                    </Button>
                </div>
            </div >
        </>
    );
};

export const CarteiraSimplificada = ({
    ratioEuro,
    balance,
    coin,
    setOpen
}) => {
    return (
        <>
            <div className='carteiraSimples'>
                <h3>Moeda: {coin}</h3>
                <h3>Ratio euro: {ratioEuro}</h3>
                <h3>Balance: {balance}({coin})</h3>
                <Button
                    buttonStyle={"btn--bet"}
                    buttonSize={'btn--medium'}
                    onClick={() => setOpen()}>
                    Levantar/Depositar
                </Button>
            </div >
        </>
    );
};
