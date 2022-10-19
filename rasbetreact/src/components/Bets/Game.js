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
            <div className='game'>
                <div className='title-hour'>
                    <div className='title'>{title}</div>
                    <div className='time'>{time}</div>
                </div>
                <div className='bets'>
                    {dicionario.map(dic => (
                        <div key={dic.count}>
                            <Button buttonStyle={"btn--bet"} buttonSize={'btn--flex'}>
                                <div>{dic.bet}</div>
                                <div>{dic.odd}</div>
                            </Button>
                        </div>
                    ))}
                </div>

            </div>
        </>
    );
};
