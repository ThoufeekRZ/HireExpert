import React, { useContext} from "react";
import DataContext from "./context/DataContext";


const FeedbackForm = () => {

    const {setFile,submitted,handleSubmit} = useContext(DataContext)

  return (
    <main className="loginPage"> {/* Reusing the same container class */}
      <form className="loginForm" onSubmit={(e)=>e.preventDefault()}>
        <h1>OCR</h1>
        {submitted ? (
          <p className="success-message">Thank you for your file! 🎉</p>
        ) : (
          <>
            <label htmlFor="file">Your Resume</label>
            <input type="file" id="feedback" 
              onChange={(e) => setFile(e.target.files[0])}
              required />
            <button type="submit" onClick={handleSubmit}>Submit</button>
          </>
        )}
      </form>
    </main>
  );
};

export default FeedbackForm;

