import React, { useState, useEffect } from 'react';
import { GamesTab } from '../GamesTab'

function Sport({
    sportType,
    userState,
    userId
}) {

    const [jogos, setJogos] = useState([])
    const [rerender, setRerender] = useState(false)

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
    }, [sportType, rerender])

    return (
        <>
            <GamesTab games={jogos}
                sportType={sportType}
                userState={userState}
                userId={userId}
                rerender={() => setRerender(!rerender)}
            />
        </>
    );
}

export default Sport;
