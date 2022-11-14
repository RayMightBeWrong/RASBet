import React, { useState, useEffect } from 'react';
import './Boletim.css';
import { Button } from '../Button';
import { BoletimBet } from './BoletimBet';


export const Boletim = ({
    bets
}) => {
    const [value, setValue] = useState('');

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');

        setValue(result);
    };


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
                        <div><BoletimBet title={game.title} winner={game.winner} cota={game.cota} /></div>
                    ))}
                </div>
                <div>
                    <input type="text" placeholder="Valor da aposta" name="aposta" required />
                    <input type="text"
                        value={value}
                        onChange={handleChange} />
                </div>
            </div>
        </>
    );
};
