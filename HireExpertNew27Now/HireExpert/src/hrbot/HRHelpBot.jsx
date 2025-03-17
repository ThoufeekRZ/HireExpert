import React, { useState, useRef, useEffect, useContext } from 'react';
import { MessageSquare, Send } from 'lucide-react';
import axios from 'axios';
import './HRHelpBot.css';
import DataContext from '../context/DataContext';

const HRHelpBot = () => {

    const { isOpen, setIsOpen, messages, setMessages, messagesEndRef, handleMessageSubmit, inputValue, setInputValue } = useContext(DataContext)


    // Auto-scroll to bottom of messages
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <div className="hire-helpbot-container">
            {!isOpen && (
                <button
                    className="hire-helpbot-button"
                    onClick={() => setIsOpen(true)}
                >
                    <MessageSquare size={24} />
                    <span>Help</span>
                </button>
            )}

            {isOpen && (
                <div className="hire-helpbot-panel">
                    <div className="hire-helpbot-header">
                        <h3>HR Assistant</h3>
                        <button
                            className="hire-helpbot-close"
                            onClick={() => setIsOpen(false)}
                        >
                            ×
                        </button>
                    </div>

                    <div className="hire-helpbot-messages">
                        {messages.map((msg, index) => (
                            <div
                                key={index}
                                className={`hire-helpbot-message ${msg.type === 'user' ? 'hire-user-message' : 'hire-bot-message'}`}
                            >
                                {msg.content}
                            </div>
                        ))}
                        <div ref={messagesEndRef} />
                    </div>

                    <form className="hire-helpbot-input" onSubmit={handleMessageSubmit}>
                        <input
                            type="text"
                            value={inputValue}
                            onChange={(e) => setInputValue(e.target.value)}
                            placeholder="Type your question here..."
                        />
                        <button type="submit">
                            <Send size={18} />
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
};

export default HRHelpBot;
