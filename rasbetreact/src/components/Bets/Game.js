import React, { useState, useEffect } from 'react';
import './Game.css';
import { Button } from '../Button';
import { PopupNewOdd } from './PopupNewOdd';



export const Game = ({
    title,
    date,
    participants,
    addBet,
    removeBet,
    changeBet,
    userState
}) => {
    const [locked, setLock] = useState({
        fechada: false,
        bet: ""
    });

    const [oddPopUp, setOddPopUp] = useState({
        activated: false,
        bet: ""
    });

    const handleClick = (name, odd) => {
        if (userState === 'gambler') {
            if (locked.fechada && locked.bet === name) {
                setLock({ fechada: false, bet: "" });
                removeBet(title);
            } else if (locked.fechada && locked.bet !== name) {
                setLock({ fechada: true, bet: name });
                changeBet(title, name, odd)
            } else if (!locked.fechada) {
                setLock({ fechada: true, bet: name });
                addBet(title, name, odd);
            }
        }
        else if (userState === 'expert') {
            console.log("yoooooooooooooooo");
            setOddPopUp({ activated: true, bet: name });
        }
    };

    return (
        <>
            {oddPopUp.activated ?
                <PopupNewOdd bet={oddPopUp.bet} closePopup={() => setOddPopUp({ activated: false, bet: "" })} />
                : null
            }
            <div className='game'>
                <div className='title-hour'>
                    <div className='title'>{title}</div>
                    <div className='date'>{date}</div>
                </div>
                <div className='bets'>
                    {participants.map(dic => (
                        <div key={dic.id}>
                            <Button buttonStyle={locked.fechada && locked.bet === dic.name ? "btn--bet-clicked" : "btn--bet"}
                                buttonSize={'btn--flex'} onClick={() => handleClick(dic.bet, dic.odd)}>
                                <div>{dic.name}</div>
                                <div>{dic.odd}</div>
                            </Button>
                        </div>
                    ))}
                </div>

            </div>
        </>
    );
};
