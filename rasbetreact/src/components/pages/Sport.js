import React, { useState, useEffect } from 'react';
import { GamesTab } from '../Bets/GamesTab'

function Sport({
    sportType,
    userState
}) {

    const [jogos, setJogos] = useState([])

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/games/", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setJogos(result)
            }
            )
    }, [])

    return (
        <>
            <div>
                <GamesTab games={jogos} sport={sportType} userState={userState} />
            </div>
        </>
    );
}

export default Sport;
