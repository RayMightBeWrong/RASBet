import React from 'react';
import './Boletim.css';
import { Button } from '../Button';
import { BoletimBet } from './BoletimBet';


export const Boletim = ({
    bets
}) => {
    return (
        <>
            <div className='boletim'>
                <div>BOLETIM</div>
                <div className='tipoAposta'>
                    <div>Simples</div>
                    <div>Múltipla</div>
                </div>
                <div>
                    {bets.map(game => (
                      <div><BoletimBet title={game.title} winner={game.winner} cota={game.cota}/></div>
                    ))}
                </div>
            </div>
        </>
    );
};
