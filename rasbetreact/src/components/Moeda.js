import React, { useState, useEffect } from 'react';
import './Moeda.css';
import { Button } from '../Button';



export const Moeda = ({
    ratioEuro,
    balance,
    verificaCarteira
}) => {


    const [value, setValue] = useState('');

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');

        setValue(result);
    };

    const handleClick = (balance) => {
        console.log("entrar");
        console.log(verificaCarteira);
        console.log("meio");
        verificaCarteira(balance);
        console.log("sair");
    };

    return (
        <>
            <div className='moeda'>
                <div className='moeda-header'>
                    <div>Ratio euro: {ratioEuro}</div>
                    <div>Balance: {balance}</div>
                </div>
            </div>
        </>
    );
};
