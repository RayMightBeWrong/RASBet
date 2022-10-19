import React from 'react';
import { Button } from '../Button';
import './GamesTab.css';
import { Link } from 'react-router-dom';

export const GamesTab = ({
    games
}) => {
    return (
        <>
            <div className='gamestab'>
                <div className='bets-tab'>
                    {games.map(game => (
                        <div>{game}</div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default GamesTab;
