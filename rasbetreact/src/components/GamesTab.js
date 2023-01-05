import React, { useState, useEffect } from 'react';
import { GameGambler, GameExpert, GameAdmin } from './objects/Game';
import { Boletim } from './Boletim';
import './GamesTab.css';
import './PayMethods.css';
import Sport from './pages/Sport';
import { Button } from './Button';

export const GamesTab = ({
    games,
    sportType,
    userState,
    userId,
    rerender
}) => {

    const [bets, setBets] = useState([]);
    const [openBoletim, setOpenBoletim] = useState(false);

    function BoletimLock() {
        if (userState === 'gambler') {
            return (
                <>
                    <div className="blackBackGround">
                        {null}
                    </div>
                    <div className="Boletim-container">
                        <div className="Boletim-body">
                            <div className="payMethods-close-button">
                                <div className="pmAline"> </div>
                                <h1>BOLETIM</h1>
                                <Button buttonStyle={'btn--flex'} onClick={() => setOpenBoletim(false)}> X </Button >
                            </div>
                            <Boletim bets={bets} userId={userId}/>
                        </div>
                    </div>
                </>
            );
        }
        else return;
    }

    const addBet = (id, title, winner, cota, participantId) => {
        console.log("adicionar")
        const updateBets = [
            ...bets,
            {
                id: id,
                title: title,
                winner: winner,
                cota: cota,
                participantId: participantId
            }
        ]
        setBets(updateBets)
    }

    const removeBet = (id) => {
        console.log("remover")
        const newbets = []
        bets.forEach((element) => { if (element.id !== id) newbets.push(element) })
        setBets(newbets)
    }

    const changeBet = (id, title, winner, cota, participantId) => {
        const newbets = []
        bets.forEach((element) => { if (element.id !== id) newbets.push(element) })
        newbets.push({
            id: id,
            title: title,
            winner: winner,
            cota: cota,
            participantId: participantId
        })
        setBets(newbets);
    }

    function subscribeClick(gameId, userId, isSubscribed) {
        console.log("yo")
        if (isSubscribed) {
            const requestOptions = {
                method: 'DELETE'
            }
            fetch("http://localhost:8080/api/games/unsubscribe?gambler_id=" + userId + "&game_id=" + gameId, requestOptions)
                .then(res => {
                    if (res.status !== 200) {
                        var errorMsg;
                        if ((errorMsg = res.headers.get("x-error")) == null)
                            errorMsg = "Error occured"
                        alert(errorMsg)
                    }
                    else {
                        alert("Unsubscribed")
                    }
                })
                .then(() => rerender())
                .catch(err => alert(err))
        } else {
            const requestOptions = {
                method: 'POST'
            }
            fetch("http://localhost:8080/api/games/subscribe?gambler_id=" + userId + "&game_id=" + gameId, requestOptions)
                .then(res => {
                    if (res.status !== 200) {
                        var errorMsg;
                        if ((errorMsg = res.headers.get("x-error")) == null)
                            errorMsg = "Error occured"
                        alert(errorMsg)
                    }
                    else {
                        alert("Subscribed")
                    }
                })
                .then(() => rerender())
                .catch(err => alert(err))
        }
    }

    function gameType(game) {
        if ((userState === 'gambler' || userState === 'loggedOff') && game.state === 1) {
            return (
                <GameGambler id={game.id} title={game.title} date={game.date} participants={game.participants} isSubscribed={game.subscribed}
                    removeBet={removeBet} addBet={addBet} changeBet={changeBet} subscribeClick={() => subscribeClick(game.id, userId, game.subscribed)} />
            )
        } else if (userState === 'admin') {
            return (
                <GameAdmin id={game.id} title={game.title} date={game.date} gameState={game.state} rerender={() => rerender()} />
            )
        } else if (userState === 'expert') {
            return (
                <GameExpert title={game.title} date={game.date} participants={game.participants} gameState={game.state} rerender={() => rerender()} />
            )
        }
    }
    if (games.length !== 0) {
        return (
            <>
                {openBoletim ?
                    <BoletimLock/>
                    : null
                }
                <div className='gamestab'>
                    <div className='gamestab-box'>
                        <div className='gamestab-content'>
                            <div className='buttonSubmit'>
                                <img src={'images/cart.png'} alt={''} onClick={() => setOpenBoletim(true)}/>
                            </div>
                            <div className='bets-tab'>
                                {games.map(game => (
                                    <div key={game.id}>{gameType(game)}</div>
                                    ))}
                            </div>
                        </div>
                    </div>
                </div>
            </>
        );
    }
    else {
        if (sportType === 'any') {
            return (
                <h1>Não há jogos disponíveis</h1>
            );
        } else {
            return (
                <h1> Não há jogos de {sportType} disponíveis </h1>
            );
        }
    }
}

export default GamesTab;
