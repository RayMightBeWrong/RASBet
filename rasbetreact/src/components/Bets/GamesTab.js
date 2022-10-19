import React from 'react';
import { Game } from './Game';
import './GamesTab.css';

const SPORTS = ['any', 'futebol', 'basquetebol', 'tenis', 'motogp'];



export const GamesTab = ({
    sport,
    games
}) => {
    const checkSport = SPORTS.includes(sport)
        ? sport
        : SPORTS[0];

    const filtredGames = [];

    console.log(games[0].sport);
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
                            <div><Game title={game.title} time={game.time} betsArray={game.betsArray} odsArray={game.odsArray} /></div>
                        ))}
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
