import React, { useState, useEffect } from 'react';
import './Boletim.css';
import { Button } from '../Button';
import { BoletimBet } from './BoletimBet';
import { CarteirasPopUp } from '../CarteirasPopUp';

const carteira1 = { ratioEuro: "0.35", balance: "30" }
const carteira2 = { ratioEuro: "1", balance: "700" }
const carteiras = [carteira1, carteira2]

export const Boletim = ({
    bets
}) => {
    const [value, setValue] = useState('');
    const [tipoAposta, setTipoAposta] = useState("Simples");
    const [finalWin, setFinalWin] = useState(0);
    const [open, setOpen] = useState(false);
    const [cupao, setCupao] = useState('');

    const changeCupao = event => {
        setCupao(event.target.value)
    };

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
                                placeholder="Valor da aposta"
                                value={value}
                                onChange={handleChange} />
                        </div>
                        <div className='boletim-ganhos'>
                            Cupão:
                            <input type="text"
                                placeholder="Cupão da aposta"
                                value={cupao}
                                onChange={changeCupao} />
                            <button>Apply</button>
                        </div>
                        <div className='boletim-ganhos'>
                            Possivel ganho: {finalWin}
                        </div>
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--flex'}
                            onClick={() => setOpen(true)}>
                            Submeter
                        </Button>
                        {open ?
                            <CarteirasPopUp carteiras={carteiras} closePopup={() => setOpen(false)} valormin={value} />
                            : null
                        }
                    </div>
                </div>
            </div>
        </>
    );
};
