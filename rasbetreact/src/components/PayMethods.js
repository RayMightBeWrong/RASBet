import React, { useState } from "react";
import "./PayMethods.css"
import { Button } from "./Button";

export const PayMethod = ({
    options,
    closePopup,
    rBack
}) => {
    const [value, setValue] = useState('');
    const [selected, setSelected] = useState(0);

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');
        setValue(result);
    };

    const handleClick = (event) => {
        if (selected === event) {
            setSelected(0)
        } else {
            setSelected(event)
        }
    };

    return (
        <div className="payMethod-container">
            < div className="payMethod-body">
                <h1>Pagamento</h1>
                <div className="metodo-pagamento">
                    <h2>Metodo de pagamento:</h2>
                    <Button buttonStyle={'btn--flex'} onClick={rBack}> X </Button >
                    <div className="botton-payMethod">
                        <Button onClick={() => handleClick(1)} buttonStyle={(selected === 1) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/mbway.png'} alt={''}/>
                        </Button >
                        <Button onClick={() => handleClick(2)} buttonStyle={(selected === 2) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/multibanco.png'} alt={''}/>
                        </Button >
                        <Button onClick={() => handleClick(3)} buttonStyle={(selected === 3) ? 'btn--image--selected' : 'btn--image'}>
                            <img className='imagem-payMethod' src={'images/visa.png'} alt={''}/>
                        </Button >
                    </div>
                    {selected !== 0 ?
                        <form action={() => closePopup()} class="container-form-pay">
                            <input type="txtPM" placeholder={"Quantia de dinheiro"} value={value} onChange={handleChange} required />
                            <select required>
                                {options.map((opt) => (
                                    <option key={opt} value={opt}>
                                        {opt}
                                    </option>
                                ))}
                            </select>
                            <Button type="submit" buttonStyle={'btn--flex'}> Pagar </Button >
                        </form>
                        : null
                    }
                </div>
            </div>
        </div >
    );
};
