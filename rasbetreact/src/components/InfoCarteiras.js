import React, {useEffect, useState} from 'react';
import './pages/Perfil.css';
import { Button } from './Button';
import { CarteiraSimplificada } from './Carteira';
import { PayMethod } from './PayMethods'
import { Link } from 'react-router-dom';
import { render } from '@testing-library/react';


export const InfoCarteiras = ({
  userId
}) => {

    const [open, setOpen] = useState(-1);
    const [selected, setSelected] = useState("");
    const [value, setValue] = useState(0);
    const [currency,setCurrency] = useState([])
    const [gamblerWallets,setGamblerWallets] = useState([])
    const [rerender,setRerender] = useState(false)

    const deposit = (idWallet,value) =>{
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
        .catch(err => alert(err))
    }

    const withdraw = (idWallet,value) =>{
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
        .catch(err => alert(err))
        setRerender(!rerender)
    }

    const createWallet = (newWallet, value) =>{
        let requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newWallet)
        }
        fetch("http://localhost:8080/api/wallets/wallet/", requestOptions)
        .then(() => {
            console.log("Nova Carteira Criada")
        }).catch(err => {alert(err); return})

        requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/wallets/gambler/" + userId, requestOptions)
        .then(res => res.json())
        .then((gw) => {
            let flag
            let currLeft = []
            let idWallet
            currency.forEach(c => {
                flag = false
                gw.forEach(w => {
                    if (w.coin.id === c.id) { 
                        w.coin["ratio_EUR"] = c.ratio_EUR
                        flag = true
                    }
                })
            if (!flag) 
                currLeft.push(c.id)
            })
            console.log("new gambler wallets:",gw,"\n new currency:", currLeft)
            setGamblerWallets(gw)
            setCurrency(currLeft)

            gw.map(elem => {
                if (elem.coin.id === newWallet.coin_id) idWallet = elem.id
            })
            deposit(idWallet, value)
        })
        .catch(err => console.log(err))
    }



    const HandlePayment = (isDeposit) =>{
        if (value !== 0) {
            setOpen(-1)
            let listC = open===0 ? currency : [gamblerWallets[open-1].coin.id];
            let sel = selected === "" ? listC[0] : selected
            let idWallet
            
            gamblerWallets.map(elem => {
                if (elem.coin.id === sel) idWallet = elem.id
            })

            console.log(sel, idWallet, value, isDeposit)
        
            if (!isDeposit) {
                if (open===0)
                    alert("Operação Impossível")
                else 
                    withdraw(idWallet, value)
            } else {
                if (open===0) {
                    let newWallet = {"coin_id": sel,"gambler_id": userId}
                    createWallet(newWallet, value)
                } else 
                    deposit(idWallet, value)
            }
        }
        setSelected("")
        setValue(0)
        setRerender(!rerender)
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
                        currLeft.push(c.id)
                })
                console.log("gambler wallets:",gw,"\ncurrency:", currLeft)
                setGamblerWallets(gw)
                setCurrency(currLeft)
            })
            .catch(err => console.log(err))
        })
    },[rerender,userId])

    return (
        <>
            <h1> Informação das carteiras </h1>
            {gamblerWallets.map(wallet => (
                <div className='carteiras-body' key={wallet.id}>
                    
                    <CarteiraSimplificada ratioEuro={wallet.coin.ratio_EUR} balance={wallet.balance} coin={wallet.coin.id} setOpen={() => setOpen(wallet.id)}/>
                </div>
            ))}
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
            {open !== -1 ?
                <PayMethod options={open===0 ? currency : [gamblerWallets[open-1].coin.id]} setSelected={setSelected} setVal={setValue} deposit={() => HandlePayment(true)} withdraw={() => HandlePayment(false)} rBack={() => setOpen(-1)} />
                :
                null
            }
        </>
    );
};