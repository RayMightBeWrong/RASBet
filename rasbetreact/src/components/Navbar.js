import React, { useState, useEffect } from 'react';
import { Button } from './Button';
import { Link } from 'react-router-dom';
import { NotificationBell } from './NotificationBell';
import './Navbar.css';

function Navbar({
  userState,
  setUserState,
  notificationList,
  markAsSeen
}) {
  const [click, setClick] = useState(false);
  const [loggedIn, setloggedIn] = useState(false);
  const [open, setOpen] = useState(false);
  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  function getImage() {
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
              PROMOÇÕES
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

  useEffect(() => {
    logButtonLogic();
  }, [userState]);

  return (
    <>
      <nav className='navbar'>
        <div className='navbar-container'>
          <Link to='/' className='navbar-logo' onClick={closeMobileMenu}>
            {getImage()}
          </Link>
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
          <div className='menu-icon' onClick={handleClick}>
            <i className={click ? 'fas fa-times' : 'fas fa-bars'}/>
          </div>
          <div className='bellButtonMenuIcon'>
            {userState === 'gambler' && loggedIn &&
              <div className='bellButton'>
                { notificationList.nova ?
                  <img className='btn--Notify' onClick={function () { setOpen(true) }} src={'../images/bell_notification.png'} alt={''} />
                  :
                  <img className='btn--Notify' onClick={function () { setOpen(true) }} src={'../images/bell.png'} alt={''} />
                }
              </div>
            }
          </div>
          {!loggedIn && <Link to='/login' className='sizeBTN'><Button buttonWidth={'btn--content'}  buttonStyle='btn--outline'>Log In</Button></Link>}
          {loggedIn && <Link to='/' className='sizeBTN'><Button buttonStyle='btn--outline' onClick={() => setUserState("loggedOff")}>Log Off</Button></Link>}

          {open ?
            <NotificationBell notificationList={notificationList} closePopup={() => setOpen(false)} markAsSeen={markAsSeen} />
            :
            null
          }
        </div>
      </nav>
    </>
  );
}

export default Navbar;
