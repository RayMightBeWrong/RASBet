import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from './Button';
import { PayMethod } from "./PayMethods"



export const Carteira = ({
    wallet_id,
    verificaCarteira
}) => {
    const [rerender, setRerender] = useState(true);
    const [value, setValue] = useState('0')
    const [wallet, setWallet] = useState({
        wallet_id: 0,
        balance: 0,
        coinId: 0
    })

    useEffect(() => {
        console.log("render")
        const requestOptions = {
            method: 'GET',
        }
        fetch("http://localhost:8080/api/wallets/wallet/" + wallet_id, requestOptions)
            .then(res => res.json())
            .then((result) => {
                setWallet({
                    wallet_id: result.id,
                    balance: result.balance,
                    coinId: result.coin.id
                })
            })

    }, [wallet_id, rerender])

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '')
        setValue(result)
    }

    const levantarDinheiro = () => {
        const requestOptions = {
            method: "PUT"
        }
        fetch("http://localhost:8080/api/transactions/withdraw?wallet_id=" + wallet_id + "&value=" + value, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Levantamento bem sucedido")
                }
            })
            .then(() => {
                setRerender(!rerender)
            })
            .catch(_ => alert("Error occured"))
    }

    const depositarDinheiro = () => {
        const requestOptions = {
            method: "PUT"
        }
        fetch("http://localhost:8080/api/transactions/deposit?wallet_id=" + wallet_id + "&value=" + value, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    console.log("depositado")
                    alert("Deposito bem sucedido")
                }
            })
            .then(() => {
                setRerender(!rerender)
            })
            .catch(_ => alert("Error occured"))
    }

    return (
        <>
            <div className='carteira'>
                <div className='carteira-header'>
                    <div>{wallet.coinId}</div>
                    <div>Balance: {wallet.balance}</div>
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
                            onClick={() => levantarDinheiro()}>
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
                        onClick={() => verificaCarteira()}>
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
