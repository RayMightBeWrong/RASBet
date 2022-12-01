import React, { useState, useEffect } from 'react';
import './Game.css';
import { Button } from '../Button';
import { PopupNewOdd } from './PopupNewOdd';



export const Game = ({
    id,
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

    const handleClick = (name, odd, participantId) => {
        if (userState === 'gambler') {
            if (locked.fechada && locked.bet === name) {
                console.log("remover beeett")
                setLock({ fechada: false, bet: "" })
                removeBet(id)
            } else if (locked.fechada && locked.bet !== name) {
                console.log("change beeett")
                setLock({ fechada: true, bet: name })
                changeBet(id, title, name, odd, participantId)
            } else if (!locked.fechada) {
                console.log("add beeett")
                setLock({ fechada: true, bet: name })
                console.log("Locked bet =" + locked.bet)
                addBet(id, title, name, odd, participantId)
            }
        }
        else if (userState === 'expert') {
            console.log("yoooooooooooooooo");
            setOddPopUp({ activated: true, bet: name })
        }
    }

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
                                buttonSize={'btn--flex'} onClick={() => handleClick(dic.name, dic.odd, dic.id)}>
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
