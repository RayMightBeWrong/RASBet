import React, { useState, useEffect } from 'react'
import "./PopupNewOdd.css"
import "./PayMethods.css"
import { Button } from "./Button"

export const PopupNewOdd = ({
    bet,
    participantId,
    closePopup,
    rerender
}) => {
    const [odd, setOdd] = useState(0)
    const [value, setValue] = useState('0.00')

    const handleChange = event => {
        const result = ("000" + event.target.value.replace(/\D/g, '')).match(/(000|00[123456789]|0[123456789]\d|[123456789]\d*)$/g).toString();
        setValue(result.slice(0, -2) + '.' + result.slice(-2));
        setOdd(parseFloat(result) / 100)
    }

    const atualizaOdd = () => {
        const requestOptions = {
            method: "PUT"
        }
        fetch("http://localhost:8080/api/games/participants/" + participantId + "/odd/" + odd, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    let errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Odd Não inserida"
                    alert(errorMsg)
                }
                else {
                    alert("Odd inserida")
                    rerender()
                    closePopup()
                }
            })
            .catch(_ => alert("Odd Não inserida"))
    }
    return (
        <div className="payMethod-container">
            <div className="payMethod-body">
                <div className="payMethods-close-button">
                    <div className="pmAline"> </div>
                    <h1> Alterar Odd de {bet} </h1>
                    <Button buttonStyle={'btn--flex'} onClick={() => closePopup()}> X </Button >
                </div>
                <div className='submit-change-odd'>
                    <input type="txtNOdds" value={value} onChange={handleChange} />
                    <Button onClick={atualizaOdd}>Submeter</Button >
                </div>
            </div>
        </div >
    )
}