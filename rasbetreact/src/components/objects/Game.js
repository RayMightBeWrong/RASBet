import React, { useState, useEffect } from 'react';
import './Game.css';
import { Button } from '../Button';
import { PopupNewOdd } from '../PopupNewOdd';



export const GameGambler = ({
    id,
    title,
    date,
    participants,
    addBet,
    removeBet,
    changeBet
}) => {
    const [locked, setLock] = useState({
        fechada: false,
        bet: ""
    })

    const handleClick = (name, odd, participantId) => {
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

    return (
        <>
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
    )
}

export const GameExpert = ({
    title,
    date,
    participants,
    rerender
}) => {
    const [oddPopUp, setOddPopUp] = useState({
        activated: false,
        bet: "",
        participantId: -1
    })

    const handleClick = (name, participantId) => {
        setOddPopUp({ activated: true, bet: name, participantId: participantId })
    }

    return (
        <>
            {oddPopUp.activated ?
                <PopupNewOdd rerender={() => rerender()} bet={oddPopUp.bet} participantId={oddPopUp.participantId} closePopup={() => setOddPopUp({ activated: false, bet: "" })} />
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
                            <Button buttonStyle={"btn--bet"}
                                buttonSize={'btn--flex'} onClick={() => handleClick(dic.name, dic.id)}>
                                <div>{dic.name}</div>
                                <div>{dic.odd}</div>
                            </Button>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}

export const GameAdmin = ({
    id,
    title,
    date,
    gameState
}) => {
    const [locked, setLock] = useState({
        fechada: false,
        bet: ""
    });

    const [oddPopUp, setOddPopUp] = useState({
        activated: false,
        bet: ""
    });

    function handleClick() {

    }

    let estado
    switch (gameState) {
        case 1:
            estado = "OPEN"
            break
        case 2:
            estado = "CLOSED"
            break
        case 3:
            estado = "SUSPENDED"
            break
        default:
    }
    return (
        <>
            <div className='game'>
                <div className='title-hour'>
                    <div className='title'>{title}</div>
                    <div className='date'>{date}</div>
                </div>
                <div>
                    Estado do jogo: {estado}
                </div>
                <Button buttonStyle={"btn--bet"}
                    buttonSize={'btn--flex'}
                    onClick={handleClick}>
                    Mudar Estado
                </Button>
            </div>
        </>
    )
}
