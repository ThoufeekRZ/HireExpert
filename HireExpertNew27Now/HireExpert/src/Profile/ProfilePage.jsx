import React, { useState, useEffect } from 'react';
import {
    Camera, Mail, User, Briefcase, Phone, Edit, Save, X,LogOut 
} from 'lucide-react';
import axios from "axios";
import './Profile.css';

const ProfilePage = () => {
    const [isEditing, setIsEditing] = useState(false);
    const [profileData, setProfileData] = useState(null);
    const [tempData, setTempData] = useState(null);
    const [image, setImage] = useState(null);
    const [imageUrl, setImageUrl] = useState("");
    const sessionId = localStorage.getItem("sessionId");

    useEffect(() => {
        fetchProfileData();
    }, []);

    const fetchProfileData = async () => {
        try {
            console.log("hi...")
            //   const response = await fetch('http://localhost:8082/ResumeAnalyser/GetUserExtraInfo');
            const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/GetUserExtraInfo", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                // credentials:"include",
                // body: JSON.stringify(tempData)
                body: JSON.stringify({ ...tempData, sessionId })

            });

            // const data="hi"
            const data = await response.json();
            console.log(data);
            setProfileData(data);
            setTempData(data);
        } catch (error) {
            console.error('Error fetching profile data:', error);
        }
    };
    const handleImageChange = (event) => {
        const file = event.target.files[0];
        setImage(file);
    };

    const handleEdit = () => {
        // setTempData(profileData);
        // // fetchProfileData();
        // setIsEditing(true);
        if (profileData) {
            setTempData({ ...profileData }); // Ensure tempData is set with profileData
            setIsEditing(true);
        }
    };

    const handleSave = async () => {
        try {
            const uploadedImageUrl = await uploadImage(); // Wait for image upload to complete

            const updatedData = {
                ...tempData,
                profileImage: uploadedImageUrl || tempData.profileImage || "" // Ensure profileImage is included
            };

            console.log("Sending data:", updatedData);

            const response = await fetch('http://localhost:8080/newResumeAnalyser_war_exploded/InsertUserExtraInfo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                // body: JSON.stringify(updatedData),
                body: JSON.stringify({ ...updatedData, sessionId })
            });

            if (response.ok) {
                setProfileData(updatedData);
                setIsEditing(false);
                fetchProfileData(); // Refresh data after update
            }
        } catch (error) {
            console.error('Error saving profile data:', error);
        }
    };


    const handleCancel = () => {
        setTempData(profileData);
        setIsEditing(false);
    };

    const uploadImage = async () => {
        if (!image) {
            alert("Please select an image first!");
            return null;  // Return null if no image is selected
        }

        const formData = new FormData();
        formData.append("file", image);
        formData.append("upload_preset", "hireXpert"); // Replace with your Cloudinary upload preset

        try {
            const response = await axios.post(
                "https://api.cloudinary.com/v1_1/dkyjnn3of/image/upload",
                formData
            );

            console.log("Cloudinary Response:", response.data);
            return response.data.secure_url; // Return the uploaded image URL
        } catch (error) {
            console.error("Upload error:", error);
            return null; // Return null on error
        }
    };

    if (!profileData) return <p>Loading...</p>;

    const handleLogout = async () => {
        try {
            const response = await fetch("http://localhost:8080/newResumeAnalyser_war_exploded/Logout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ sessionId })
                // credentials: "include"  // To ensure session cookies are included
            });
    
            if (response.ok) {
                localStorage.removeItem("sessionId"); // Remove session ID
                window.location.href = "http://localhost:5173"; // Redirect to Landing Page
            } else {
                console.error("Logout failed");
            }
        } catch (error) {
            console.error("Error during logout:", error);
        }
    };

    return (
        <div className='whole-profile-container'>
            <div className="profile-container">
                <header className="profile-header">
                    <h1>Profile Settings</h1>
                    {!isEditing ? (
                        // <button className="edit-button" onClick={handleEdit}>
                        //     <Edit size={18} /> Edit Profile
                        // </button>
                        <div className="edit-actions">
                            <button className="edit-button" onClick={handleEdit}>
                                <Edit size={18} /> Edit Profile
                            </button>
                            <button className="logout-button" onClick={handleLogout}>
                                <LogOut size={18} /> Logout
                            </button>
                        </div>

                    ) : (
                        <div className="edit-actions">
                            <button className="save-button" onClick={handleSave}>
                                <Save size={18} /> Save Changes
                            </button>
                            <button className="cancel-button" onClick={handleCancel}>
                                <X size={18} /> Cancel
                            </button>
                        </div>
                    )}
                </header>

                <main className="profile-content">
                    <section className="profile-main-info">
                        <div className="profile-image-container">
                            {/* <div>  */}
                            <img
                                src={tempData.profileImage || "https://via.placeholder.com/150"}
                                alt="Profile"
                                className="profile-image"
                            />
                            {/* </div> */}
                            {isEditing && (
                                <div className="image-upload-overlay">
                                    <label htmlFor="profile-image-upload" className="image-upload-label">
                                        <Camera size={24} />
                                        <span>Change Photo</span>
                                    </label>
                                    <input
                                        type="file"
                                        id="profile-image-upload"
                                        accept="image/*"
                                        onChange={handleImageChange}
                                        className="image-upload-input"
                                    />
                                </div>
                            )}
                        </div>
                        <div className="profile-info-container">
                            <div className="info-group">
                                <label>Full Name</label>
                                <div className="info-value">
                                    <User size={18} /> {profileData.userName}
                                </div>
                            </div>

                            <div className="info-group">
                                <label>Email</label>
                                <div className="info-value">
                                    <Mail size={18} /> {profileData.email}
                                </div>
                            </div>

                            <div className="info-grid">
                                <div className="info-group">
                                    <label>Role</label>
                                    {isEditing ? (
                                        <input
                                            type="text"
                                            value={tempData.role}
                                            onChange={(e) => setTempData({ ...tempData, role: e.target.value })}
                                            className="edit-input"
                                        />
                                    ) : (
                                        <div className="info-value">
                                            <Briefcase size={18} /> {profileData.role}
                                        </div>
                                    )}
                                </div>
                                <div className="info-group">
                                    <label>Phone</label>
                                    {isEditing ? (
                                        <input
                                            type="tel"
                                            value={tempData.phoneNumber}
                                            onChange={(e) => setTempData({ ...tempData, phoneNumber: e.target.value })}
                                            className="edit-input"
                                        />
                                    ) : (
                                        <div className="info-value">
                                            <Phone size={18} /> {profileData.phoneNumber}
                                        </div>
                                    )}
                                </div>
                            </div>

                            <div className="info-group">
                                <label>Bio</label>
                                {isEditing ? (
                                    <textarea
                                        value={tempData.bio}
                                        onChange={(e) => setTempData({ ...tempData, bio: e.target.value })}
                                        className="edit-input bio-input"
                                        rows={4}
                                    />
                                ) : (
                                    <div className="info-value bio-text">
                                        {profileData.bio}
                                    </div>
                                )}
                            </div>
                        </div>
                    </section>
                </main>
            </div>
        </div>
    );
};

export default ProfilePage;