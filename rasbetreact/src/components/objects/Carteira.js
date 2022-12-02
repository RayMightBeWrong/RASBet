import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from '../Button';
import { PayMethod } from "../PayMethods"



export const Carteira = ({
    wallet_id,
    verificaCarteira
}) => {

    const [open, setOpen] = useState(false);
    const [rerender, setRerender] = useState(true);
    const [value, setValue] = useState('0')
    const [wallet, setWallet] = useState({
        wallet_id: 0,
        balance: 0,
        coinId: 0
    })

    useEffect(() => {
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
                    alert("Deposito bem sucedido")
                    setOpen(false)
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
                    <Button buttonStyle={"btn--inverted"}
                        buttonSize={'btn--medium'}
                        onClick={() => setOpen(true)}
                    >
                        Depositar
                    </Button>
                    <Button buttonStyle={"btn"}
                        buttonSize={'btn--medium'}
                        onClick={() => verificaCarteira()}>
                        Submeter
                    </Button>
                </div>
                {open ?
                    <PayMethod
                        option={wallet.coinId}
                        hasDeposit={true}
                        hasWithdraw={false}
                        setSelected={() => ""}
                        setVal={setValue}
                        deposit={() => depositarDinheiro()}
                        rBack={() => setOpen(false)}
                    />
                    : null
                }
            </div >
        </>
    );
};

/* 
<div className='carteira-transferencia'>
    <input type="text"
        required
        placeholder="Quantia de dinheiro"
        value={value}
        onChange={handleChange} />
    <Button buttonStyle={"btn--bet"}
        buttonSize={'btn--flex'}
        onClick={() => depositarDinheiro()}
    >
        Depositar
    </Button>
</div>
 */



export const CarteiraSimplificada = ({
    ratioEuro,
    balance,
    coin,
    setOpen
}) => {
    return (
        <>
            <div className='carteiraSimples'>
                <h4>Moeda: {coin}</h4>
                <h4>Ratio euro: {ratioEuro}</h4>
                <h4>Balance: {balance}({coin})</h4>
                <Button
                    buttonStyle={"btn--inverted"}
                    buttonSize={'btn--medium'}
                    onClick={() => setOpen()}>
                    Levantar/Depositar
                </Button>
            </div >
        </>
    );
};
