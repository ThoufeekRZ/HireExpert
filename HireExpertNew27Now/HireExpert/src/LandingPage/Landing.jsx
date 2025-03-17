

import React, { useState, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../images/logo1.png';
import './Landing.css';
import SignUp from '../Login/SignUp';
import LoginPage from '../Login/LoginPage';
import ForgotPassword from '../ForgotPassword/ForgotPassword';
import EnterOTP from '../ForgotPassword/EnterOTP';
import ConfirmPassword from '../ForgotPassword/ConfirmPassword';
import About from './About';
import { useTheme } from "../ThemeContext";
import styled from "styled-components";

const Landing = () => {
    const navigate = useNavigate();
    const [showLoginPopup, setShowLoginPopup] = useState(false);
    const [showSignUpPopup, setShowSignUpPopup] = useState(false);
    const [showForgotPasswordPopup, setShowForgotPasswordPopup] = useState(false);
    const [showEnterOTPPopup, setShowEnterOTPPopup] = useState(false);
    const [showConfirmPasswordPopup, setShowConfirmPasswordPopup] = useState(false);
    const { theme, toggleTheme } = useTheme();

    // const openLoginPopup = () => setShowLoginPopup(true);
    const openLoginPopup = () => {
        setShowSignUpPopup(false);
        closeConfirmPasswordPopup();
        closeEnterOTPPopup();
        closeForgotPasswordPopup();
        setShowLoginPopup(true);
        // setTimeout(() => setShowLoginPopup(true), 100);
    };
    const openAboutPage = () => {
        navigate("/about");
    }


    const closeLoginPopup = () => setShowLoginPopup(false);

    const openSignUpPopup = () => setShowSignUpPopup(true);
    const closeSignUpPopup = () => setShowSignUpPopup(false);

    const openForgotPasswordPopup = useCallback(() => {
        console.log("hi.......")
        // closeAllPopups();
        closeLoginPopup();
        setShowForgotPasswordPopup(true);
        // console.log(showForgotPasswordPopup + openEnterOTPPopup)
    }, []);

    // const openEnterOTPPopup = () => {
    //     // closeAllPopups();
    //     setShowEnterOTPPopup(true);
    // };
    const openEnterOTPPopup = useCallback(() => {
        setShowEnterOTPPopup(true);
    }, []);


    const openConfirmPasswordPopup = () => {
        // closeAllPopups();

        setShowConfirmPasswordPopup(true);
    };
    const closeForgotPasswordPopup = () => {
        setShowForgotPasswordPopup(false);
    };
    const closeEnterOTPPopup = () => {
        // setShowForgotPasswordPopup(false);
        // setShowConfirmPasswordPopup(false);
        setShowEnterOTPPopup(false)
    };
    const closeConfirmPasswordPopup = () => {
        // setShowForgotPasswordPopup(false);
        setShowConfirmPasswordPopup(false);
    };

    const [email, setEmail] = useState("");
    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handleSendOTP = async () => {
        if (!email) {
            setPopupData({ title: "Error", message: "Please enter an email!" });
            setShowPopup(true);
            return;
        }

        try {
            const response = await fetch("http://localhost:8082/ResumeAnalyser/SendOTP", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email }),
            });

            const data = await response.json();

            if (response.ok && data.status === "success") {
                sessionStorage.setItem("otp", data.otp);

                setPopupData({ title: "Success", message: "OTP sent successfully!" });
                setShowPopup(true);

                setTimeout(() => {
                    setShowPopup(false);
                    // navigate("/enter-otp");
                    onSwitchToEnterOTP();
                }, 2000);
            } else {
                setPopupData({ title: "Error", message: data.message || "Failed to send OTP. Please try again." });
                setShowPopup(true);
            }
        } catch (error) {
            console.error("Error:", error);
            setPopupData({ title: "Error", message: "Something went wrong!" });
            setShowPopup(true);
        }
    };

    return (
        <div className="landing-container">
            <header className="landing-header">
                <nav className="landing-nav">
                    <a href="/" className="landing-logo"><img src={logo} alt="Logo" /></a>
                    <div className="landing-nav-links">
                        <button className="landing-nav-link" onClick={openSignUpPopup}>SIGN UP</button>
                        <button className="landing-nav-link" onClick={openLoginPopup}>LOGIN</button>
                        {/* <a href="#contact" className="landing-nav-link">About</a> */}
                        <button className="landing-nav-link" onClick={openAboutPage}>ABOUT</button>
                        {/* <button onClick={toggleTheme} className="theme-toggle">
                            {theme === "light" ? "🌙 Dark Mode" : "☀️ Light Mode"}
                        </button> */}
                        {/* <ToggleButton onClick={toggleTheme}>
                            {theme === "light" ? "🌙 Dark Mode" : "☀️ Light Mode"}
                        </ToggleButton> */}
                    </div>
                </nav>
            </header>

            <section className="landing-hero">
                <div className="landing-hero-content">
                    <h1 className="landing-hero-title">Smart & Quick Hire</h1>
                    <p className="landing-hero-subtitle">
                        Transform your hiring process with AI-powered resume parsing and intelligent candidate filtering
                    </p>
                    <button className="landing-button" onClick={openSignUpPopup}>Get Started</button>
                </div>
            </section>
            <section className="landing-features">
                <h2 className="landing-section-title">Why Choose Us</h2>
                <div className="landing-features-grid">
                    <div className="landing-feature-card">
                        <div className="landing-feature-icon">🚀</div>
                        <h3 className="landing-feature-title">Bulk Resume Processing</h3>             <p className="landing-feature-description">Upload multiple resumes at once and let our AI handle the heavy lifting</p>
                    </div>
                    <div className="landing-feature-card">
                        <div className="landing-feature-icon">🎯</div>
                        <h3 className="landing-feature-title">Smart Filtering</h3>
                        <p className="landing-feature-description">Advanced AI algorithms to match candidates with your requirements</p>
                    </div>
                    <div className="landing-feature-card">
                        <div className="landing-feature-icon">⚡</div>
                        <h3 className="landing-feature-title">Quick Results</h3>
                        <p className="landing-feature-description">Get detailed candidate analysis in minutes, not hours</p>
                    </div>
                </div>
            </section>
            <section className="landing-cta">
                <h2 className="landing-cta-title">Ready to Transform Your Hiring?</h2>
                <p className="landing-cta-subtitle">Join thousands of HRs who have streamlined their recruitment process</p>
                <button className="landing-button" onClick={openSignUpPopup}>Start Now</button>
            </section>

            <footer className="landing-footer">
                <div className="landing-footer-content">
                    <div className="landing-footer-links">
                        <Link to={''} className="landing-footer-link">About</Link>
                        <Link to={''} className="landing-footer-link">Privacy</Link>
                        <Link to={''} className="landing-footer-link">Terms</Link>
                        <Link to={''} className="landing-footer-link">Contact</Link>
                    </div>
                    <p className="landing-footer-text">© 2025 HireXpert. All rights reserved.</p>
                </div>
            </footer>

            {/* Login Popup Modal */}
            {showLoginPopup && (
                <div className="modal-overlay" onClick={closeLoginPopup}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeLoginPopup}>✖</button>
                        {/* <LoginPage /> */}

                        {/* {showLoginPopup && <LoginPage onClose={() => setShowLoginPopup(false)} />} */}
                        {showLoginPopup && <LoginPage onClose={closeLoginPopup} onSwitchToSignup={openSignUpPopup} onForgotPassword={openForgotPasswordPopup} />}
                    </div>
                </div>
            )}

            {/* SignUp Popup Modal */}
            {showSignUpPopup && (
                <div className="modal-overlay" onClick={closeSignUpPopup}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeSignUpPopup}>✖</button>
                        <SignUp onSignupSuccess={openLoginPopup} onSwitchToLogin={openLoginPopup} />
                    </div>
                </div>
            )}

            {/* {showForgotPasswordPopup && <ForgotPassword onSwitchToEnterOTP={openEnterOTPPopup} />} */}
            {showForgotPasswordPopup && (
                <div className="modal-overlay" onClick={closeForgotPasswordPopup}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="close-button" onClick={closeForgotPasswordPopup}>✖</button>

                        {showForgotPasswordPopup && <ForgotPassword onSwitchToEnterOTP={openEnterOTPPopup} />}
                    </div>
                </div>
            )}

            {showEnterOTPPopup && (
                <div className="modal-overlay" onClick={closeEnterOTPPopup}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        {/* <button className="close-button" onClick={closeEnterOTPPopup}>✖</button> */}

                        {showEnterOTPPopup && <EnterOTP onSwitchToConfirmPassword={openConfirmPasswordPopup} />}
                    </div>
                </div>
            )}
            {/* {showEnterOTPPopup && <EnterOTP onSwitchToConfirmPassword={openConfirmPasswordPopup} />} */}

            {showConfirmPasswordPopup && (
                <div className="modal-overlay" onClick={closeConfirmPasswordPopup}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        {/* <button className="close-button" onClick={closeConfirmPasswordPopup}>✖</button> */}

                        {showConfirmPasswordPopup && <ConfirmPassword onSwitchToLogin={openLoginPopup} />}
                    </div>
                </div>
            )}


        </div>
    );
};


export default Landing;
