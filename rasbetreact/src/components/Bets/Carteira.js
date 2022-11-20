import React, { useState, useEffect } from 'react';
import './Carteira.css';
import { Button } from '../Button';



export const Carteira = ({
    ratioEuro,
    balance
}) => {
    return (
        <>
            <div className='carteira'>
                <div className='carteira-header'>
                    <div>Tatio euro: {ratioEuro}</div>
                    <div>Balance: {balance}</div>
                </div>
                <div className='carteira-body'>
                    <div className='carteira-transferencia'>
                        Aposta:
                        <input type="text"
                            required
                            placeholder="Levantar"
                            value={value}
                            onChange={handleChange} />
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => handleClick(dic.bet, dic.odd)}>
                        </Button>
                    </div>
                    <div className='carteira-transferencia'>
                        Aposta:
                        <input type="text"
                            required
                            placeholder="Depositar"
                            value={value}
                            onChange={handleChange} />
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => handleClick(dic.bet, dic.odd)}>
                        </Button>
                    </div>

                </div>

            </div>
        </>
    );
};
