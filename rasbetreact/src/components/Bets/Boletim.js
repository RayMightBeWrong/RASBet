import React from 'react';
import './Game.css';
import { Button } from '../Button';



export const Game = ({
    title,
    time,
    betsArray,
    odsArray
}) => {

    const dicionario = [];
    for (let i = 0; i < betsArray.length; i++) {
        dicionario[i] = { bet: betsArray[i], odd: odsArray[i], count: i }
    }
    return (
        <>
            <div className='boletim'>
                BOLETIM
            </div>
        </>
    );
};
