import React, { useState } from "react";
import "./PayMethods.css"
import { Button } from "./Button";

export const NewWallet = ({
    options,
    setSelected,
    create,
    rBack
}) => {
    const [value, setValue] = useState('0,00');
    const [payM, setPayM] = useState(0);

    const handleClick = (event) => {
        if (payM === event) {
            setPayM(0)
        } else {
            setPayM(event)
        }
    };

    return (
        <div className="payMethod-container">
            <div className="payMethod-body">
                <div className="payMethods-close-button">
                    <div className="pmAline"> </div>
                    <h1>Nova Carteira</h1>
                    <Button buttonStyle={'btn--flex'} onClick={() => rBack()}> X </Button >
                </div>
                <div className="metodo-pagamento">
                    <div className="container-form-pay">
                        <select onClick={e => setSelected(e.target.value)}>
                            {options.map((opt) => (
                                <option key={opt.id} value={opt.id}>
                                    {opt.id} (Ratio €: {opt.ratio_EUR})
                                </option>
                            ))}
                        </select>
                        <Button buttonStyle={'btn--flex'} onClick={create}> Nova Carteira </Button >
                    </div>
                </div>
            </div>
        </div >
    );
};