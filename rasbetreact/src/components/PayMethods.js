import React, { useState } from "react";
import "./PayMethods.css"
import { Button } from "./Button";

export const PayMethod = ({
    options,
    setSelected,
    setVal,
    deposit,
    withdraw,
    rBack
}) => {
    const [value, setValue] = useState('0,00');
    const [payM, setPayM] = useState(0);

    const handleChange = event => {
        const result = ("000"+event.target.value.replace(/\D/g, '')).match(/(000|00[123456789]|0[123456789]\d|[123456789]\d*)$/g).toString();
        setValue(result.slice(0,-2) + ',' + result.slice(-2));
        setVal(parseFloat(result)/100)
    };

    const handleClick = (event) => {
        if (payM === event) {
            setPayM(0)
        } else {
            setPayM(event)
        }
    };

    return (
        <div className="payMethod-container">
            < div className="payMethod-body">
                <h1>Pagamento</h1>
                <div className="metodo-pagamento">
                    <h2>Metodo de pagamento:</h2>
                    <Button buttonStyle={'btn--flex'} onClick={() => {setVal(0);rBack()}}> X </Button >
                    <div className="botton-payMethod">
                        <Button onClick={() => handleClick(1)} buttonStyle={(payM === 1) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/mbway.png'} alt={''}/>
                        </Button >
                        <Button onClick={() => handleClick(2)} buttonStyle={(payM === 2) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/multibanco.png'} alt={''}/>
                        </Button >
                        <Button onClick={() => handleClick(3)} buttonStyle={(payM === 3) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/visa.png'} alt={''}/>
                        </Button >
                    </div>
                    {payM !== 0 ?
                        <div class="container-form-pay">
                            <input type="txtPM" value={value} onChange={handleChange}  />
                            <select onClick={e => setSelected(e.target.value)}>
                                {options.map((opt) => (
                                    <option key={opt} value={opt}>
                                        {opt}
                                    </option>
                                ))}
                            </select>
                            <Button type="submit" buttonStyle={'btn--flex'} onClick={deposit}> Deposito </Button >
                            <Button type="submit" buttonStyle={'btn--flex'} onClick={withdraw}> Levantamento </Button >
                        </div>
                        : null
                    }
                </div>
            </div>
        </div >
    );
};
