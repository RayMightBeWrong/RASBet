import React, { useState, useEffect } from 'react';
import './Boletim.css';
import { Button } from '../Button';
import { BoletimBet } from './BoletimBet';


export const Boletim = ({
    bets
}) => {
    const [value, setValue] = useState('');

    const [finalWin, setFinalWin] = useState(0);

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');

        setValue(result);
    };

    useEffect(() => {
        var soma = 0
        if(value!=='' && bets.length!==0)
            soma=parseFloat(value)
        {bets.map(game => (
            soma = soma*parseFloat(game.cota)
        ))}
        setFinalWin(soma)
    });

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
                    Aposta:
                    <input type="text"
                        required
                        placeholder="Valor da aposta"
                        value={value}
                        onChange={handleChange} />
                    Possivel ganho: {finalWin}
                    <Button buttonStyle={"btn--bet"} 
                            buttonSize={'btn--flex'}>
                                Submeter
                            </Button>
                </div>
            </div>
        </>
    );
};
