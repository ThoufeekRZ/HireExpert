import React from 'react';
// import { Link } from 'react-router-dom';
import './About.css'; // You'll need to create this CSS file with the styles
import { Facebook, Instagram, Linkedin, Twitter } from "lucide-react";


const About = () => {
  return (
    <>
      {/* Navigation */}
      
      {/* Header */}
      <div className='big'>
        <div className='small'>

       
      <header className='hh'>
        <div className='back-btn'  onClick={() => (window.location.href = "http://localhost:5173/")}>Back</div>
        <div className="hero-text">
          <h1 className='h1'>About HIREXPERT</h1>
          <p>Empowering recruiters with AI-powered resume analysis tools</p>
        </div>
      </header>
      
      {/* Main Content */}
      <div className="container1">
        <section>
          <h2 className='h2'>Our Mission</h2>
          <p>At HireXpert, We believe in transforming the recruitment process through advanced technology. Our mission is to give recruiters powerful tools to efficiently identify qualified candidates, saving time and improving hiring outcomes.</p>
        </section>
        
        <section>
          <h2 className='h2'>Who We Are</h2>
          <p>At HireXpert, We are a team of passionate developers and innovators dedicated to revolutionizing the hiring process with AI-powered resume analysis. Our goal is to simplify and enhance recruitment by providing job seekers and recruiters with smart, data-driven insights.</p>
        </section>
        
        <section>
          {/* <h2>Our Features</h2> */}
          <div className="features-centered">
            <div className="feature-column-centered">
              <h3 className='h3'>For Recruiters</h3>
              
              <div className="feature-item">
                <div className="feature-title">Bulk Resume Processing</div>
                <p>Quickly analyze dozens of applications to identify top candidates.</p>
              </div>
              
              <div className="feature-item">
                <div className="feature-title">Skill Matching</div>
                <p>Automatically match candidate skills against job requirements.</p>
              </div>
              
              <div className="feature-item">
                <div className="feature-title">Experience Verification</div>
                <p>Verify employment history and validate claimed experience.</p>
              </div>
              
              <div className="feature-item">
                <div className="feature-title">Candidate Comparison</div>
                <p>Compare multiple candidates side-by-side with visual analytics.</p>
              </div>
              
              <div className="feature-item">
                <div className="feature-title">Custom Screening Criteria</div>
                <p>Set your own parameters for what makes an ideal candidate.</p>
              </div>
            </div>
          </div>
        </section>
        
        <section>
          <h2 className='h2'>Our Technology</h2>
          <p>We combine natural language processing, machine learning, and years of recruitment expertise to provide accurate, helpful resume analysis. Our algorithms are continuously refined based on the latest hiring trends and feedback from industry professionals.</p>
        </section>
        
        <section>
          <h2 className='h2'>Privacy Commitment</h2>
          <p>We understand resumes contain sensitive personal information. That's why we've built our platform with privacy at its core:</p>
          <ul style={{ listStyleType: 'none', marginLeft: '1rem' }}>
            <li style={{ marginBottom: '0.5rem' }}>• All data is encrypted both in transit and at rest</li>
            <li style={{ marginBottom: '0.5rem' }}>• Resumes are analyzed but never shared without explicit permission</li>
            <li style={{ marginBottom: '0.5rem' }}>• Option to delete all your data from our systems at any time</li>
          </ul>
        </section>
      </div>
      
      {/* Call to Action */}
      <section className="cta-section">
        <h2 className='h2'>Join Us Today</h2>
        <p>Discover how HireXpert can transform your recruitment process, helping you find the perfect candidates faster and more efficiently.</p>
        <div className="cta-buttons">
        </div>
      </section>
      
      {/* Footer */}
      <footer className='ff'>
        <div className="social-links">
        <a href="https://facebook.com" target="_blank" rel="noopener noreferrer">
        <Facebook size={32} className="hover:text-blue-600" />
      </a>

      <a href="https://x.com" target="_blank" rel="noopener noreferrer">
        <Twitter size={32} className="hover:text-black" />
      </a>

      <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer">
        <Linkedin size={32} className="hover:text-blue-500" />
      </a>

      <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">
        <Instagram size={32} className="hover:text-pink-500" />
      </a>
        </div>
        <p>&copy; {new Date().getFullYear()} HireXpert. All rights reserved.</p>
      </footer>
      </div>
      </div>
    </>
  );
};

export default About;