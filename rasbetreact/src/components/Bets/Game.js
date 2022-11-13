import React, { useState, useEffect } from 'react';
import './Game.css';
import { Button } from '../Button';



export const Game = ({
    title,
    time,
    betsArray,
    odsArray,
    addBet,
    removeBet
}) => {
    
    const [locked, setLock] = useState({
        fechada: false,
        bet: ""
      });
    const handleClick = (title,bet,odd) => {
        if(locked.fechada&&locked.bet==bet){
            setLock({fechada:false,bet:""});
            removeBet(title);
        } else if(!locked.fechada){
            setLock({fechada:true,bet:bet});
            addBet(title,bet,odd);
        }
    };

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
                            <Button buttonStyle={locked.fechada&&locked.bet==dic.bet?"btn--bet-clicked":"btn--bet"} 
                            buttonSize={'btn--flex'} onClick={()=>handleClick(title,dic.bet,dic.odd)}>
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
