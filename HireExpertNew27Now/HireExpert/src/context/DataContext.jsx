
import React, { useEffect, useState, useRef } from "react";
import api from "../api/post";
import { createContext } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const DataContext = createContext({});

export const DataProvider = ({ children }) => {


  // const data = JSON.parse(localStorage.getItem("uploadedResumes")) || "[]"

  // const [resumes,setResumes] = useState(data);

  const data = JSON.parse(localStorage.getItem("uploadedResumes") || "[]"); // Ensure JSON.parse gets a valid string
  console.log("data " + data)
  const [resumes, setResumes] = useState(data);

  const navigate = useNavigate();


  const [popUpResume, setPopUpResume] = useState({});
  const [loading, setLoading] = useState(false);
  const [totalResumes, setTotalResumes] = useState(localStorage.getItem("totalResumes") || 0);
  const [searchResume, setSearchResume] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [sendEmailTo, setSendEmailTo] = useState("All candidates");
  const [Recruit, setRecruit] = useState(localStorage.getItem("recruit") || null);

  const [accepted, setAccepted] = useState(0);
  const [reject, setReject] = useState(0);
  const [mailSented, setMailSented] = useState(0);
  const [resumeTotal, setResumeTotal] = useState(0);
  const [selectedStatus, setSelectedStatus] = useState("");


  const [emailSuccessState, setEmailSuccessState] = useState("");

  const [selectAfterDetailsRecruit, setSelectAfterDetailsRecruit] = useState(null);


  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    {
      type: 'bot',
      content: 'Hello! I\'m your HR assistant. How can I help you today?'
    }
  ]);
  const [inputValue, setInputValue] = useState('');
  const messagesEndRef = useRef(null);


  useEffect(()=>{
   if(!localStorage.getItem("sessionId")){
        navigate("/")
   }
  },[])


  useEffect(() => {
    const fetchResumes = async () => {
      if (!Recruit) return;
       setResumes([])
      // Extract status and update state
      const status = Recruit.label?.split(":")[0].trim() || "";
      setSelectedStatus(status);
      console.log("Selected Status:", status);
  
      // Only fetch resumes if status is "In progress"
      if (status === "In progress") {
        try {
          console.log("Recruit:", Recruit);
  
          const response = await axios.post(
            "http://localhost:8084/resumeAnalyser_war_exploded/getCorrespondingResumes?recruitId="+Recruit.value,
            { recruitId: Recruit.value },
            { headers: { "Content-Type": "application/json" } }
          );
  
          if (response.data) {
            setResumes(response.data);
            localStorage.setItem("uploadedResumes",JSON.stringify(response.data));
            console.log("Resumes fetched:", response.data);
          }
        } catch (error) {
          console.error("Error fetching resumes:", error.message);
        }
      }
    };
  
    fetchResumes();
  }, [Recruit]);
  





  // Function to send the user's question to the Flask API
  const processMessage = async (userMessage) => {
    // Add user message to the chat
    setMessages(prev => [...prev, { type: 'user', content: userMessage }]);

    const redirectArray = { "/dashboard": "to home page", "/dashboard/hiringboard": "to the hiring board", "/dashboard/profile": "to the profile page", "/dashboard/upload": "to resume uploading page" }

    try {
      // Call the Flask API for a response
      const response = await axios.post('http://localhost:5006/analyze-achievement', {
        question: userMessage,
        user_id: localStorage.getItem("sessionId")
      }, {
        headers: { 'Content-Type': 'application/json' }
      });

      // Extract the bot's reply from the API response
      let botResponse = response.data.response || "I'm not sure how to help with that.";
      const redirect = response.data.redirect ?? "#";

      console.log(redirect);

      const redirectAvailable = false;

      if (Object.keys(redirectArray).includes(botResponse)) {
        navigate(botResponse);
        botResponse = ` Redirecting ${redirectArray[botResponse]}`;
      }


      // Append the bot's reply after a delay for better UX
      setTimeout(() => {
        setMessages(prev => [...prev, { type: 'bot', content: botResponse }]);
      }, 500);


    } catch (error) {
      console.error("Error fetching response:", error);
      setMessages(prev => [...prev, { type: 'bot', content: "I couldn't connect to the server. Please try again later." }]);
    }
  };

  // Handle form submission
  const handleMessageSubmit = (e) => {
    e.preventDefault();
    if (inputValue.trim() === '') return;

    processMessage(inputValue);
    setInputValue('');
  };


  useEffect(() => {
    console.log(sendEmailTo)
  }, [sendEmailTo])


  const handleSendEmailTo = async () => {
    const formData = new FormData();
    formData.append("recruitId", 42);
    formData.append("sendEmailTo", "Shortlisted candidates");
    console.log("its me");
    const id = localStorage.getItem("recruitId");

    if (!sendEmailTo) {
      alert("Please provide an email address.");
      return;
    }
    console.log(sendEmailTo);

    try {
      const response = await axios.post(
        "http://localhost:8080/newResumeAnalyser_war_exploded/SendEmails",
        // formData,
        { recruitId: id, sendEmailTo: sendEmailTo },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
      console.log("Response:", response.data);
      if (response.data) {
        setEmailSuccessState(response.data);
        if (response.data.success) {
          setResumes([]);
          localStorage.setItem("uploadedResumes", JSON.stringify([]))
        }
      }
    } catch (error) {
      console.error("Error sending email:", error);
      setEmailSuccessState("Failed to send email. Try again.");
    }
  };

  useEffect(() => {

    console.log(emailSuccessState);


  }, [emailSuccessState])


  const handleResumeClick = async (id,status) => {
    try {
      console.log("44");

      const response = await axios.post("http://localhost:8084/resumeAnalyser_war_exploded/getResume?id=" + id + "&recruitId=" + Recruit.value+"&status="+status, {
        headers: { "Content-Type": "application/json" }
      })

      if (response.data) {
        setPopUpResume(response.data);
        let data = (JSON.parse(localStorage.getItem("uploadedResumes")));
        let obj = data.find(obj => obj.id === id);
        if (obj.status == "New") {
          obj.status = "Reviewed";
          localStorage.setItem("uploadedResumes", JSON.stringify(data));
          setResumes(data);
        }
        console.log(response.data);
      }


    } catch (error) {
      console.log(error.message);

    }
  }

  const handleRejectResume = async (id) => {
    const data = JSON.parse(localStorage.getItem("uploadedResumes"));
    console.log(data);

    const obj = data.find(obj => obj.id === id);
    if (obj.status !== "Rejected") {

      const response = await axios.post("http://localhost:8084/resumeAnalyser_war_exploded/changeStatus?id=" + obj.id + "&status=Rejected&recruitId="+Recruit.value, {
        headers: { "Content-Type": "application/json" }
      })
      console.log(response);

      obj.status = "Rejected";
      localStorage.setItem("uploadedResumes", JSON.stringify(data));
      setResumes(data);
    }
  }

  const handleShortlistResume = async (id) => {
    const data = JSON.parse(localStorage.getItem("uploadedResumes"));
    const obj = data.find(obj => obj.id === id);


    if (obj.status !== "Shortlisted") {

      console.log({ obj, id });
      const response = await axios.post("http://localhost:8084/resumeAnalyser_war_exploded/changeStatus?id=" + obj.id + "&status=Shortlisted&recruitId="+Recruit.value, {
        headers: { "Content-Type": "application/json" }
      })
      console.log(response);

      obj.status = "Shortlisted";
      localStorage.setItem("uploadedResumes", JSON.stringify(data));
      setResumes(data);
    }
  }


  return (
    <DataContext.Provider value={{ resumes, setResumes, loading, setLoading, handleResumeClick, popUpResume, setPopUpResume, handleRejectResume, handleShortlistResume,
     totalResumes, setTotalResumes, searchResume, setSearchResume, setFilterStatus, filterStatus, sendEmailTo,
      setSendEmailTo, handleSendEmailTo, emailSuccessState, accepted, setAccepted, reject, setReject, mailSented, setMailSented,
       resumeTotal, setResumeTotal, Recruit, setRecruit, isOpen, setIsOpen, messages, setMessages, messagesEndRef,
        handleMessageSubmit, inputValue, setInputValue, navigate, selectedStatus, selectAfterDetailsRecruit, setSelectAfterDetailsRecruit }}>

      {children}

    </DataContext.Provider>
  )
}


export default DataContext;