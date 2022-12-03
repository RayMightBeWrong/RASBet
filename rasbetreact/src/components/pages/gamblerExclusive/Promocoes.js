import React from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';

function Promocoes({
    userState
}) {
    if (userState === "gambler") {
        return (
            <>
                <div className='registo'>
                    <div className='white-box'>


                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default Promocoes;
