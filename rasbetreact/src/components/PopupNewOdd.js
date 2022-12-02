import React, { useState, useEffect } from 'react';
import "./PopupNewOdd.css";

export const PopupNewOdd = ({
    bet,
    closePopup
}) => {
    const [odd, setOdd] = useState('');


    const changeOdd = event => {
        setOdd(event.target.value)
    };
    const atualizaOdd = () => {
        if (odd !== '') {
            {/*todo verificações mais completas*/ }
            alert('Odd Inserida');
        }
        else {
            alert('Odd Não inserida');
        }
        closePopup();
    }
    return (
        <div className="popupNewOdd-container" >
            < div className="popupNewOdd-body" >
                <h1> Alterar Odd de {bet} </h1>
                <input type="text"
                    placeholder="Nova odd"
                    value={odd}
                    onChange={changeOdd} />
                <button onClick={atualizaOdd}>Fechar menu das carteiras</button >
            </div>
        </div >
    );
};