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
    wallet_id,
    withdraw,
    deposit
}) => {

    const [value, setValue] = useState('');
    const [open, setOpen] = useState(false);

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');
        setValue(result);
    };

    const [wallet, setWallet] = useState({
        depositar: false,
        levantar: false,
        wallet_id: ""
    });

    const handleClick = (wallet_id, value) => {
        setOpen(true);
        if (wallet.wallet_id == wallet_id && wallet.depositar == false && wallet.levantar == true) {
            setWallet({ depositar: false, levantar: true, wallet_id: wallet_id, gambler_id: "", coupon: "" })
            withdraw(wallet_id, value)
            const levantar = { wallet_id, value }
            console.log(levantar)
            fetch("http://localhost:8080/api/transactions/withdraw", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(levantar)
            }).then(() => {
                console.log("Levantamento")
            })
        } else if (wallet.wallet_id == wallet_id && wallet.depositar == true && wallet.levantar == false) {
            setWallet({ depositar: true, levantar: false, wallet_id: wallet_id, gambler_id: "", coupon: "" })
            deposit(wallet_id, value)
            const deposito = { wallet_id, value }
            console.log(deposito)
            fetch("http://localhost:8080/api/transactions/deposit", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(deposito)
            }).then(() => {
                console.log("Depósito")
            })
        }
    };

    return (
        <>
            <div className='carteiraSimples'>
                <div className='carteira-headerS'>
                    <div>Ratio euro: {ratioEuro}</div>
                    <div>Balance: {balance}</div>
                </div>
                <div className='carteira-transferenciaS'>
                    <input type="txtC" placeholder="Quantia de dinheiro" value={value} onChange={handleChange} />
                    <div className='carteira-transferencia-botoes'>
                        <Button
                            buttonStyle={"btn--bet"}
                            buttonSize={'btn--medium'}
                            onClick={() => handleClick(wallet_id, value)}>
                            {/*todo on click diminui*/}
                            Levantar
                        </Button>
                        <Button
                            buttonStyle={"btn--bet"}
                            buttonSize={'btn--medium'}
                            onClick={() => handleClick(wallet_id, value)}>
                            {/*todo on click aumenta*/}
                            Depositar
                        </Button>
                        {open ?
                            <PayMethod options={["nao sei qual é"]} closePopup={() => setOpen(false)} rBack={() => setOpen(false)} />
                            : null
                        }
                    </div>
                </div>
            </div >
        </>
    );
};
