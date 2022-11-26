import React, { useState, useEffect } from 'react';
import './Login.css';
import { Button } from '../Button';
import { Link } from 'react-router-dom';
import { NewCoinPopUp } from '../NewCoinPopUp'

function CriaMoeda({
    userState
}) {
    const [open, setOpen] = useState(false);
    if (userState === "admin") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>
                        TODO mostrar as moedas existentes
                        <Button buttonStyle={"btn--bet"}
                            buttonSize={'btn--large'}
                            onClick={() => setOpen(true)}>
                            Nova Moeda
                        </Button>
                        {open ?
                            <NewCoinPopUp closePopup={() => setOpen(false)} />
                            : null
                        }
                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default CriaMoeda;
