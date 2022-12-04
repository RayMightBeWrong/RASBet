import React, { useState } from "react";
import "./CarteirasPopUp.css";
import { Carteira } from './objects/Carteira';
import { Button } from './Button';

export const NewCoinPopUp = ({
    closePopup
}) => {

    const [nomeCoin, setNomeCoin] = useState("");
    const [ratio_EUR, setRatio_EUR] = useState(0);
    const [value, setValue] = useState('0.00000');

    const handleChange = event => {
        const result = ("000000" + event.target.value.replace(/\D/g, '')).match(/(000000|00000[123456789]|0000[123456789]\d|000[123456789]\d\d|00[123456789]\d\d\d|0[123456789]\d\d\d\d|[123456789]\d*)$/g).toString();
        setValue(result.slice(0, -5) + '.' + result.slice(-5));
        setRatio_EUR(parseFloat(result) / 100000)
        console.log(ratio_EUR)
    };

    const handleSubmit = event => {
        event.preventDefault()
        const newCoin = {
            id: nomeCoin,
            ratio_EUR: ratio_EUR
        }
        console.log(ratio_EUR)
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newCoin)
        }
        fetch("http://localhost:8080/api/wallets/coin/", requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("New coin created successfully")
                    closePopup()
                }
            })
            .catch(_ => alert("Error occured"))
    }

    return (
        <>
            <form className="container-form" onSubmit={handleSubmit}>
                <h1>Criar Coin</h1>

                Nome:
                <input type="txtL" placeholder="Nome da Coin" value={nomeCoin} onChange={(e) => setNomeCoin(e.target.value)} required />

                Ratio Euro:
                <input type="txtL" placeholder="Ratio-Eur" value={value} onChange={handleChange} />

                <div className="botton-flexing">
                    <Button type="submit" buttonSize='btn--flex' onClick={handleSubmit}>Concluir</Button>
                    <Button buttonStyle={'btn--inverted'} buttonSize='btn--flex' onClick={closePopup} >Cancelar</Button>
                </div>
            </form>


        </>
    );
};