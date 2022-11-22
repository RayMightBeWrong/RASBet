import React from 'react';
import { GamesTab } from '../Bets/GamesTab'

function Sport({
    sportType,
    userState
}) {
    const jogo1 = { sport: "futebol", title: "Benfica - Sporting", time: "Hoje 22 horas", betsArray: ["Benfica", "Empate", "Sporting"], odsArray: [1.4, 2.2, 3.9] }
    const jogo2 = { sport: "futebol", title: "Braga - Maritimo", time: "Amanha 19 horas", betsArray: ["Braga", "Empate", "Maritimo"], odsArray: [1.4, 2.2, 3.9] }
    const jogo3 = { sport: "motogp", title: "Corrida motogp", time: "Hoje 15 horas", betsArray: ["Zeca", "Luis", "Ray", "Alex", "Diogo", "Gama"], odsArray: [1, 0.0, 1, 9999, 1, 1] }
    const jogos = [jogo1, jogo2, jogo3]
    return (
        <>
            <div>
                <GamesTab games={jogos} sport={sportType} userState={userState} />
            </div>
        </>
    );
}

export default Sport;
