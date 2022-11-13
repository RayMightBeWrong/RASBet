import React, { useState, useEffect } from 'react';
import { Game } from './Game';
import { Boletim } from './Boletim';
import './GamesTab.css';

const SPORTS = ['any', 'futebol', 'basquetebol', 'tenis', 'motogp'];

const bet1 = { title: "Benfica - Sporting", winner: "Benfica", cota: "1.4"}
const bet2 = { title: "Benfica - Sporting", winner: "Benfica", cota: "1.4"}
const bet3 = { title: "Benfica - Sporting", winner: "Benfica", cota: "1.4"}

export const GamesTab = ({
    sport,
    games
}) => {

    const [bets, setBets] = useState([]);

    const addBet = (title,winner,cota) => {
        const updateBets = [
            // copy the current bets state
            ...bets,
            // now you can add a new object to add to the array
            {
                // add the ids to update database
                //id: users.length + 1,
                // adding a new bet
                title: title,
                winner: winner,
                cota: cota
            }
        ];
        // update the state to the updateBets
        setBets(updateBets);
    }

    const removeBet = (title) => {
        const newbets=[]
        bets.forEach((element) => {if(element.title!==title) newbets.push(element)})
        setBets(newbets)
    }

    const checkSport = SPORTS.includes(sport)
        ? sport
        : SPORTS[0];

    const filtredGames = [];

    games.forEach(function (game) {
        if (checkSport === 'any' || game.sport === checkSport) {
            filtredGames.push(game)
        }
    })
    if (filtredGames.length !== 0) {
        return (
            <>
                <div className='gamestab'>
                    <div className='bets-tab'>
                        {filtredGames.map(game => (
                            <div><Game title={game.title} time={game.time} betsArray={game.betsArray} odsArray={game.odsArray} removeBet={removeBet} addBet={addBet}/></div>
                        ))}
                    </div>
                    <div className='boletimbox'>
                        <div><Boletim bets={bets}/></div>
                    </div>
                </div>
            </>
        );
    }
    else {
        const noGames = "Não há jogos de" + { checkSport } + "disponíveis";
        if (checkSport === 'any') {
            return (
                <h1>Não há jogos disponíveis</h1>
            );
        } else {
            return (
                <h1> Não há jogos de {checkSport} disponíveis </h1>
            );
        }
    }
}

export default GamesTab;
