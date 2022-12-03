import React, { useState, useEffect } from 'react';
import '../Comons.css';
import { Button } from '../../Button';
import { Link } from 'react-router-dom';
import { UserFunction } from '../../objects/User'

function ExpertManager({
    userState
}) {

    const [users, setUsers] = useState([])

    const [rerender, setRerender] = useState(false)

    useEffect(() => {
        const requestOptions = {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        }
        fetch("http://localhost:8080/api/users/expert/all/", requestOptions)
            .then(res => res.json())
            .then((result) => {
                setUsers(result)
            })
    }, [rerender])


    function deleteExpert(id) {
        const requestOptions = {
            method: 'DELETE'
        }
        fetch("http://localhost:8080/api/users/expert/" + id, requestOptions)
            .then(res => {
                if (res.status !== 200) {
                    var errorMsg;
                    if ((errorMsg = res.headers.get("x-error")) == null)
                        errorMsg = "Error occured"
                    alert(errorMsg)
                }
                else {
                    alert("Expert deleted")
                }
            })
            .then(() => setRerender(!rerender))
            .catch(err => alert(err))
    }

    if (userState === "admin") {
        return (
            <>
                <div className='greenBackGround'>
                    <div className='white-box'>
                        <div>
                            <h1>Consulta de experts</h1>
                            {users.map(user => (
                                <div key={user.id}>
                                    <UserFunction nome={user.name}
                                        id={user.id} email={user.email}
                                        butonName={"Apagar"}
                                        runnable={() => deleteExpert(user.id)} />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </>
        )
    } else {
        return ("")
    }
}

export default ExpertManager;
