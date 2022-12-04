import React, { useState, useEffect } from 'react';
import { GameGambler, GameExpert, GameAdmin } from './objects/Game';
import { Boletim } from './Boletim';
import './GamesTab.css';
import Sport from './pages/Sport';

export const GamesTab = ({
    games,
    sportType,
    userState,
    userId,
    rerender
}) => {

    const [bets, setBets] = useState([]);

    function BoletimLock() {
        if (userState === 'gambler') {
            return (
                <div className='boletimbox'>
                    <div><Boletim bets={bets} userId={userId} /></div>
                </div>
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

    function gameType(game) {
        if ((userState === 'gambler' || userState === 'loggedOff') && game.state === 1) {
            return (
                <GameGambler id={game.id} title={game.title} date={game.date} participants={game.participants}
                    removeBet={removeBet} addBet={addBet} changeBet={changeBet} />
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
                <div className='gamestab'>
                    <div className='bets-tab'>
                        {games.map(game => (
                            <div key={game.id}>{gameType(game)}</div>
                        ))}
                    </div>
                    <BoletimLock />
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
