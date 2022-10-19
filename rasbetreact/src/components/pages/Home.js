import React from 'react';
import '../../App.css';
import { Game } from '../Bets/Game'
import { GamesTab } from '../Bets/GamesTab'


function Home() {
  const jogo1 = { sport: "futebol", title: "Benfica - Sporting", time: "Hoje 22 horas", betsArray: ["Benfica", "Empate", "Sporting"], odsArray: [1.4, 2.2, 3.9] }
  const jogo2 = { sport: "futebol", title: "Braga - Maritimo", time: "Amanha 19 horas", betsArray: ["Braga", "Empate", "Maritimo"], odsArray: [1.4, 2.2, 3.9] }
  const jogo3 = { sport: "motogp", title: "Corrida motogp", time: "Hoje 15 horas", betsArray: ["Zeca", "Luis", "Ray", "Alex", "Diogo", "Gama"], odsArray: [1, 0.0, 1, 9999, 1, 1] }
  const jogos = [jogo1, jogo2, jogo3]
  return (
    <>
      <div>
        <GamesTab games={jogos} sport='any' />
      </div>
    </>
  );
}

export default Home;
