import React from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';

function GestorPromocoes({
    userState
}) {
    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>


                    </div>
                </div>
            </>
        );
    } else {
        return ("");
    }
}

export default GestorPromocoes;
