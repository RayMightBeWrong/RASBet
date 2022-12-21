import React, { useState, useEffect } from 'react';
import './Game.css';
import { Button } from '../Button';
import { PopupNewOdd } from '../PopupNewOdd';



const getDate = (date) => {
    let dateS = date.split('T')
    return (
        <div className='date-hour'>
            <h4>{dateS[0]}</h4>
            <h4>{dateS[1].split('.')[0]}</h4>
        </div>
    )
}

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
            <div className={`game-OPEN`}>
                <div className='game-title-state'>
                    <h2>{title}</h2>
                    <h3> subscribe</h3>
                </div>
                <div className='game-time-bet'>
                    {getDate(date)}
                    <div className='game-bet'>
                        {participants.map(dic => (
                            <Button key={dic.id} buttonStyle={locked.fechada && locked.bet === dic.name ? "btn--bet-clicked" : "btn--bet"}
                                buttonSize={'btn--flex'} onClick={() => handleClick(dic.name, dic.odd, dic.id)}>
                                <h5>{dic.name}</h5>
                                <h5>{dic.odd}</h5>
                            </Button>
                        ))}
                    </div>
                </div>
            </div>
        </>
    )
}

export const GameExpert = ({
    title,
    date,
    participants,
    gameState,
    rerender
}) => {
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
            <div className={`game-${estado}`}>
                <div className='game-title-state'>
                    <h2>{title}</h2>
                    <h4>Estado do jogo: {estado}</h4>
                </div>
                <div className='game-time-bet'>
                    {getDate(date)}
                    <div className='game-bet'>
                        {participants.map(dic => (
                            <Button buttonStyle={"btn--bet"}
                                buttonSize={'btn--flex'} onClick={() => handleClick(dic.name, dic.id)}>
                                <div>{dic.name}</div>
                                <div>{dic.odd}</div>
                            </Button>
                        ))}
                    </div>
                </div>
            </div>
        </>
    )
}


export const GameAdmin = ({
    id,
    title,
    date,
    gameState,
    rerender
}) => {
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

    function changeGameState(state) {
        let computedState
        let printMsg

        switch (state) {
            case "OPEN":
                computedState = "1"
                printMsg = "aberto"
                break;
            case "SUSPEND":
                computedState = "3"
                printMsg = "suspenso"
                break;
            case "CLOSE":
                printMsg = "fechado"
                computedState = "close"
                break;
            default:
                return
        }

        let requestOptions = {
            method: 'PUT'
        }
        fetch("http://localhost:8080/api/games/" + id + "/state/" + computedState, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Jogo " + printMsg)
                    rerender()
                }
            })
            .catch(_ => alert("Error occured"))
    }

    return (
        <>
            <div className={`game-${estado}`}>
                <div className='game-title-state'>
                    <h2>{title}</h2>
                    <h4>Estado do jogo: {estado}</h4>
                </div>
                <div className='game-time-bet'>
                    {getDate(date)}
                    <div className='game-bet'>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => changeGameState("SUSPEND")}>
                            Suspender
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => changeGameState("OPEN")}>
                            Abrir
                        </Button>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => changeGameState("CLOSE")}>
                            Fechar
                        </Button>
                    </div>
                </div>
            </div>
        </>
    )
}

export const GameSelected = ({
    title,
    date,
    participants,
    winnerId
}) => {
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
                            <Button buttonStyle={winnerId === dic.id ? "btn--bet-clicked" : "btn--bet"}
                                buttonSize={'btn--flex'}>
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
