import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from './Button';
import { PayMethod } from "./PayMethods"



export const Carteira = ({
    coinId,
    balance,
    verificaCarteira,
    addToBalance,
    removeFromBalance
    //walletIDs
}) => {

    const [wallet, setWallet] = useState({
        depositar: false,
        levantar: false,
        wallet_id: "",
        gambler_id: "",
        coupon: ""
    });

    const [value, setValue] = useState('');

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');

        setValue(result);
    };

    const handleClick = (wallet_id, value, gambler_id, coupon) => {
        if (wallet.wallet_id == wallet_id && wallet.depositar == false && wallet.levantar == true) {
            setWallet({ depositar: false, levantar: true, wallet_id: wallet_id, gambler_id: "", coupon: "" })
            removeFromBalance(wallet_id, value)
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
            addToBalance(wallet_id, value)
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
        else if (wallet.coupon == coupon && wallet.gambler_id == gambler_id) {
            setWallet({ depositar: false, levantar: false, wallet_id: "", gambler_id: gambler_id, coupon: coupon })
            /*claimPromotionWithCoupon(gambler_id, coupon) TODO*/
            const coupon = { wallet_id, value }
            console.log(coupon)
            fetch("http://localhost:8080/api/promotions/gambler/claimPromoWithCoupon", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(coupon)
            }).then(() => {
                console.log("Coupon")
            })
        }
    };

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
                            /*onclick={() => handleClick(wallet_id, value, gambler_id, coupon)} */> {/*todo on click aumenta*/}
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
                        onClick={() => handleClick(balance)}>
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
