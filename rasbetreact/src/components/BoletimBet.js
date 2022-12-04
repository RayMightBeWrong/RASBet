import React from 'react';
import './BoletimBet.css';
import { Button } from './Button';



export const BoletimBet = ({
    title,
    winner,
    cota
}) => {
    return (
        <>
            <div className='boletimbet-gameChoice'>
                <div className='boletimbet-title'>
                    <h3>{title}</h3>
                </div>
                <div className='boletimbet-winner-cota'>
                    <div className='winner'><h3>{winner}</h3></div>
                    <div className='cota'><h3>{cota}</h3></div>
                </div>
            </div>
        </>
    );
};
