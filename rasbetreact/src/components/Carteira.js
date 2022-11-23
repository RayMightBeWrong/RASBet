import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from './Button';



export const Carteira = ({
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
            <div className='carteira'>
                <div className='carteira-header'>
                    <div>Ratio euro: {ratioEuro}</div>
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
                            buttonSize={'btn--flex'}> {/* todo on click diminui */}
                            Levantar
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}> {/*todo on click aumenta*/}
                            Depositar
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}> {/todo on click aumenta/}
                            Cupão
                        </Button>
                    </div>
                </div>
                <div>
                    <Button buttonStyle={"btn"}
                        buttonSize={'btn--flex'}
                        onClick={() => handleClick(balance)}>
                        x
                    </Button>
                </div>
            </div>
        </>
    );
};

export const CarteiraSimplificada = ({
    ratioEuro,
    balance
}) => {

    const [value, setValue] = useState('');

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');
        setValue(result);
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
                            buttonSize={'btn--medium'}>
                            {/*todo on click diminui*/}
                            Levantar
                        </Button>
                        <Button
                            buttonStyle={"btn--bet"}
                            buttonSize={'btn--medium'}>
                            {/*todo on click aumenta*/}
                            Depositar
                        </Button>
                    </div>
                </div>
            </div>
        </>
    );
};
