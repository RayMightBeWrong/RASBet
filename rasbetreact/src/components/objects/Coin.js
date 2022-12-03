import React, { useState, useEffect } from 'react';
import './Coin.css';
import { Button } from '../Button';

export const Coin = ({
    coinName,
    ratioEuro
}) => {
    return (
        <>
            <div className='coin'>
                <h4>Moeda: {coinName}</h4>
                <h4>Ratio euro: {ratioEuro}</h4>
            </div >
        </>
    );
};