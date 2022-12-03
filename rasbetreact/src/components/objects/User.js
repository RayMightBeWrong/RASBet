import React, { useState, useEffect } from 'react';
import './User.css';
import { Button } from '../Button';

export const User = ({
    nome,
    id,
    email
}) => {
    return (
        <>
            <div className='user'>
                <h4>Nome: {nome}</h4>
                <h4>Email: {email}</h4>
            </div>
        </>
    )
}