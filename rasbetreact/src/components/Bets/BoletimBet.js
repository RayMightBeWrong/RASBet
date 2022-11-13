import React from 'react';
import './BoletimBet.css';
import { Button } from '../Button';



export const BoletimBet = ({
    title,
    winner,
    cota
}) => {
    return (
        <>
            <div className='boletimbet-gameChoice'>
                <div className='boletimbet-title'>{title}</div>
                <div className='boletimbet-winner-cota'>
                    <div className='winner'>{winner}</div>
                    <div className='cota'>{cota}</div>
                </div>

            </div>
        </>
    );
};
