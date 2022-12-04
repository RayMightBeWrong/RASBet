import React, { useEffect, useState } from 'react';
import './pages/gamblerExclusive/Perfil.css';
import { Button } from './Button';
import { CarteiraSimplificada } from './objects/Carteira';
import { PayMethod } from './PayMethods'
import { Link } from 'react-router-dom';
import { NewWallet } from "./NewWallet";


export const InfoCarteiras = ({
    userId
}) => {

    const [open, setOpen] = useState(-1);
    const [cupon, setCupon] = useState("");
    const [selected, setSelected] = useState("");
    const [value, setValue] = useState(0);
    const [currency, setCurrency] = useState([])
    const [gamblerWallets, setGamblerWallets] = useState([])
    const [rerender, setRerender] = useState(false)

    const deposit = (idWallet, value) => {
        let requestOptions = {
            method: 'PUT',
            headers: { "Access-Control-Allow-Origin": "*" }
        }
        let s = "http://localhost:8080/api/transactions/deposit?wallet_id=" + idWallet + "&value=" + value
        console.log(s);
        fetch(s, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Operação realizada com sucesso")
                }
            })
            .then(() => setRerender(!rerender))
            .catch(err => alert(err))
    }

    const withdraw = (idWallet, value) => {
        let requestOptions = {
            method: 'PUT',
            headers: { "Access-Control-Allow-Origin": "*" }
        }
        let s = "http://localhost:8080/api/transactions/withdraw?wallet_id=" + idWallet + "&value=" + value
        console.log(s);
        fetch(s, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Operação realizada com sucesso")
                }
            })
            .then(() => setRerender(!rerender))
            .catch(err => alert(err))
    }

    const createWallet = (newWallet) => {
        let requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newWallet)
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
            .then(() => setRerender(!rerender))
            .catch(_ => alert("Error occured"))
    }

    const HandlePayment = (option) => {
        if (value !== 0 || option == 2) {
            setOpen(-1)
            let listC = open === 0 ? currency : [getId(open)];
            let sel = selected === "" ? listC[0] : selected
            let idWallet

            gamblerWallets.map(elem => {
                if (elem.coin.id === sel) idWallet = elem.id
            })

            console.log(sel, idWallet, value, option)

            switch (option) {
                case 0:
                    withdraw(idWallet, value)
                    break
                case 1:
                    deposit(idWallet, value)
                    break
                case 2:
                    let newWallet = { "coin_id": sel, "gambler_id": userId }
                    console.log(newWallet);
                    createWallet(newWallet, value)
                    break
                default:
            }
        } else
            alert("Operação Impossível")

        setSelected("")
        setValue(0)
    }

    useEffect(() => {
        let requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/wallets/coin/", requestOptions)
            .then(res => res.json())
            .then((curr) => {
                fetch("http://localhost:8080/api/wallets/gambler/" + userId, requestOptions)
                    .then(res => res.json())
                    .then((gw) => {
                        let flag
                        let currLeft = []
                        curr.forEach(c => {
                            flag = false
                            gw.forEach(w => {
                                if (w.coin.id === c.id) {
                                    w.coin["ratio_EUR"] = c.ratio_EUR
                                    flag = true
                                }
                            })
                            if (!flag)
                                currLeft.push(c)
                        })
                        console.log("gambler wallets:", gw, "\ncurrency:", currLeft)
                        setGamblerWallets(gw)
                        setCurrency(currLeft)
                    })
                    .catch(err => console.log(err))
            })
    }, [rerender, userId, open])



    const getId = (wallet_id) => {
        let id
        gamblerWallets.map(wallet => {
            if (wallet.id == wallet_id)
                id = wallet.coin.id
        })
        return id
    }

    const aplicaCupao = () => {
        let requestOptions = {
            method: 'PUT',
            headers: { "Access-Control-Allow-Origin": "*" }
        }

        fetch("http://localhost:8080/api/transactions/claimBalancePromotion?gambler_id=" + userId + "&coupon=" + cupon, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Cupão aplicado")
                }
            })
            .then(() => setRerender(!rerender))
            .catch(err => alert("Error occured"))
    }
    return (
        <>
            <h1> Informação das carteiras </h1>
            <div className='carteiras'>
                <div className='carteiras-body'>
                    {gamblerWallets.map(wallet => (
                        <div className='carteiras-line' key={wallet.id}>
                            <CarteiraSimplificada ratioEuro={wallet.coin.ratio_EUR} balance={wallet.balance} coin={wallet.coin.id} setOpen={() => setOpen(wallet.id)} />
                        </div>
                    ))}
                </div>
                <br></br>
            </div>
            <div className='save'>
                {currency.length !== 0 ?
                    <Button buttonSize={'btn--medium'} onClick={() => setOpen(0)}>Nova Carteira</Button>
                    :
                    null
                }
                <Link to='/historico' className='registerbutton'>
                    <Button buttonSize={'btn--medium'} buttonStyle={'btn--inverted'} onClick={() => setOpen(true)}> Consultar Historico </Button>
                </Link>
            </div>
            <div className='promos-box'>
                <h2>Cupão</h2>
                <div className='promos-box-data'>
                    <input type="txtP2" placeholder="Codigo do cupão" value={cupon} onChange={(e) => setCupon(e.target.value)} required />
                    <Button buttonSize={'btn--medium'}  onClick={() => aplicaCupao()}> Aplicar Promoção </Button>
                </div>
            </div>
            {open > 0 ?
                <PayMethod
                    option={getId(open)}
                    hasDeposit={true}
                    hasWithdraw={true}
                    setVal={setValue}
                    deposit={() => HandlePayment(1)}
                    withdraw={() => HandlePayment(0)}
                    rBack={() => setOpen(-1)}
                />
                : null
            }
            {open === 0 ?
                <NewWallet
                    options={currency}
                    setSelected={setSelected}
                    create={() => HandlePayment(2)}
                    rBack={() => setOpen(-1)}
                />
                : null
            }
        </>
    );
};
