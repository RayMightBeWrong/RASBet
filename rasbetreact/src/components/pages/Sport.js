import React, { useState, useEffect } from 'react';
import { GamesTab } from '../GamesTab'

function Sport({
    sportType,
    userState,
    userId
}) {

    const [jogos, setJogos] = useState([])

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        let link = "http://localhost:8080/api/games/sport/" + sportType
        if (sportType === "any") {
            link = "http://localhost:8080/api/games/"
        }
        fetch(link, requestOptions)
            .then(res => res.json())
            .then((result) => {
                setJogos(result)
            })
    }, [sportType])

    return (
        <>
            <div>
                <GamesTab games={jogos} sportType={sportType} userState={userState} userId={userId} />
            </div>
        </>
    );
}

export default Sport;
