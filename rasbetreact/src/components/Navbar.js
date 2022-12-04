import React, { useState, useEffect } from 'react';
import { Button } from './Button';
import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar({
  userState,
  setUserState
}) {
  const [click, setClick] = useState(false);
  const [button, setButton] = useState(true);
  const [loggedIn, setloggedIn] = useState(false);
  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  function getImage(){
    var path = window.location.href.split('/');
    let difDirectory = path[3] === 'admin_Options'

    if (difDirectory)
      return <img className='icon' src={'../images/icon.png'} alt={''} />
    else
      return <img className='icon' src={'images/icon.png'} alt={''} />
  }

  function adminOptions() {
    if (userState === 'admin') {
      return (
        <li className='nav-item'>
          <Link
            to='/admin_Options'
            className='nav-links'
            onClick={closeMobileMenu}
          >
            TOOLS
          </Link>
        </li>
      );
    }
    else return;
  }

  function gamblerOptions() {
    if (userState === 'gambler') {
      return (
        <>
          <li className='nav-item'>
            <Link
              to='/promocoes'
              className='nav-links'
              onClick={closeMobileMenu}
            >
              PROMOCÕES
            </Link>
          </li>
          <li>
            <Link
              to='/perfil'
              className='nav-links'
              onClick={closeMobileMenu}
            >
              PERFIL
            </Link>
          </li>
        </>
      );
    }
    else return;
  }

  function logButtonMobile() {
    if (userState === 'loggedOff') {
      return (

        <Link
          to='/login'
          className='nav-links-mobile'
          onClick={closeMobileMenu}
        >
          Log In
        </Link>
      )
    }
    else
      return (
        <Link
          to='/'
          className='nav-links-mobile'
          onClick={function (event) { closeMobileMenu(); setUserState("loggedOff") }}
        >
          Log Off
        </Link>
      )
  }

  const logButtonLogic = () => {
    if (userState === 'loggedOff') {
      setloggedIn(false)
    }
    else {
      setloggedIn(true)
    }
  }

  const showButton = () => {
    if (window.innerWidth <= 1250) {
      setButton(false);
    } else {
      setButton(true);
    }
  };

  useEffect(() => {
    showButton();
  }, []);

  useEffect(() => {
    logButtonLogic();
  }, [userState]);

  window.addEventListener('resize', showButton);

  return (
    <>
      <nav className='navbar'>
        <div className='navbar-container'>
          <Link to='/' className='navbar-logo' onClick={closeMobileMenu}>
            {getImage()}
          </Link>
          <div className='menu-icon' onClick={handleClick}>
            <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
          </div>
          <ul className={click ? 'nav-menu active' : 'nav-menu'}>
            <li className='nav-item'>
              <Link
                to='/futebol'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                FUTEBOL
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/nba'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                NBA
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/f1'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                F1
              </Link>
            </li>
            <li className='nav-item'>
              <Link
                to='/nfl'
                className='nav-links'
                onClick={closeMobileMenu}
              >
                NFL
              </Link>
            </li>
            {gamblerOptions()}
            {adminOptions()}
            <li>
              {logButtonMobile()}
            </li>
          </ul>
          {!loggedIn && button && <Link to='/login' ><Button buttonStyle='btn--outline'>Log In</Button> </Link>}
          {loggedIn && button && <Link to='/'><Button buttonStyle='btn--outline' onClick={() => setUserState("loggedOff")}>Log Off</Button> </Link>}
        </div>
      </nav>
    </>
  );
}

export default Navbar;
