import React, { useState, useEffect } from 'react';
import './User.css';
import { Button } from '../Button';

export const User = ({
    nome,
    email
}) => {
    return (
        <>
            <div className='user'>
                <div className='info'>
                    <h4>Nome: {nome}</h4>
                    <h4>Email: {email}</h4>
                </div>
            </div>
        </>
    )
}

export const UserFunction = ({
    nome,
    email,
    runnable,
    butonName
}) => {
    return (
        <>
            <div className='user'>
                <div className='info'>
                    <h4>Nome: {nome}</h4>
                    <h4>Email: {email}</h4>
                </div>
                <Button buttonStyle="btn--inverted" onClick={() => runnable()}>
                    {butonName}
                </Button>
            </div>
        </>
    )
}