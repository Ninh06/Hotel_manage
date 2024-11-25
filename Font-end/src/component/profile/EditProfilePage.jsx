import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

const EditProfilePage = () => {
    const [user, setUser] = useState({
        name: '',
        phoneNumber: '',
        password: '',
    });
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await ApiService.getUserProfile();
                setUser({
                    ...response.user,
                    password: '',
                });
            } catch (error) {
                setError(error.message);
            }
        };

        fetchUserProfile();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUser((prevUser) => ({
            ...prevUser,
            [name]: value,
        }));
    };

    const handleUpdateProfile = async (e) => {
        e.preventDefault();
        try {
            await ApiService.updateUser(user.id, {
                name: user.name,
                phoneNumber: user.phoneNumber,
                password: user.password || undefined,
            });
            alert('Profile updated successfully!');
            navigate('/profile');
        } catch (error) {
            setError(error.message);
        }
    };

    const handleDeleteProfile = async () => {
        if (!window.confirm('Are you sure you want to delete your account?')) {
            return;
        }
        try {
            await ApiService.deleteUser(user.id);
            navigate('/signup');
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div className="edit-profile-page">
            <h2>Edit Profile</h2>
            {error && <p className="error-message">{error}</p>}
            {user && (
                <form onSubmit={handleUpdateProfile} className="edit-profile-form">
                    <div className="form-group">
                        <label>Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={user.name}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Phone Number:</label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={user.phoneNumber}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Password:</label>
                        <input
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={handleInputChange}
                        />
                        <br />
                        <small>Leave blank if you don't want to change the password</small>
                    </div>
                    <button type="submit" className="update-profile-button">
                        Update Profile
                    </button>
                </form>
            )}
            {/* <button
                className="delete-profile-button"
                onClick={handleDeleteProfile}
            >
                Delete Profile
            </button> */}
        </div>
    );
};

export default EditProfilePage;
