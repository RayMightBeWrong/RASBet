import React, { useState, useEffect } from 'react';
import './Moeda.css';
import { Button } from './Button';



export const Moeda = ({
    id,
    ratio_EUR
}) => {
    return (
        <>
            <div className='moeda'>
                <div className='moeda-header'>
                    <div>{id}</div>
                    <div>Ratio euro: {ratio_EUR}</div>
                </div>
            </div>
        </>
    )
}
