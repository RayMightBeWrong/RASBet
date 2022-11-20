import React, { useState, useEffect } from 'react';
import './Boletim.css';
import { Button } from '../Button';
import { BoletimBet } from './BoletimBet';


export const Boletim = ({
    bets
}) => {
    const [value, setValue] = useState('');
    const [tipoAposta, setTipoAposta] = useState("Simples");
    const [finalWin, setFinalWin] = useState(0);

    const handleChange = event => {
        const result = event.target.value.replace(/\D/g, '');

        setValue(result);
    };

    useEffect(() => {
        var soma = 0
        if (value !== '' && bets.length !== 0)
            soma = parseFloat(value)
        {
            bets.map(game => (
                soma = soma * parseFloat(game.cota)
            ))
        }
        setFinalWin(soma)
        if (bets.length <= 1) {
            setTipoAposta("Simples")
        } else setTipoAposta("Múltipla")
    });

    return (
        <>
            <div className='boletim'>
                <div className='boletim-header'>
                    <div>BOLETIM</div>
                    <div>{tipoAposta}</div>
                </div>
                <div className='boletim-body'>
                    <div>
                        {bets.map(game => (
                            <div><BoletimBet title={game.title} winner={game.winner} cota={game.cota} /></div>
                        ))}
                    </div>
                    <div>
                        <div className='boletim-ganhos'>
                            Aposta:
                            <input type="text"
                                required
                                placeholder="Valor da aposta"
                                value={value}
                                onChange={handleChange} />
                        </div>
                        <div className='boletim-ganhos'>
                            Possivel ganho: {finalWin}
                        </div>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}>
                            Submeter
                        </Button>
                    </div>
                </div>
            </div>
        </>
    );
};
