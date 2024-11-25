import React from "react";
import ApiService from "../../service/ApiService";

const UserResult = ({ userSearchResults, onDelete }) => {
    const isAdmin = ApiService.isAdmin();

    const handleDelete = async (user) => {
        const confirmDelete = window.confirm(`Are you sure you want to delete the user "${user.name}"?`);
        if (confirmDelete) {
            try {
                const response = await ApiService.deleteUser(user.id);
                if (response.statusCode === 200) {
                    alert("User deleted successfully!");
                    onDelete(user.id);
                } else {
                    alert(`Failed to delete user: ${response.message}`);
                }
            } catch (error) {
                console.error("Error deleting user:", error);
                alert("An error occurred while deleting the user.");
            }
        }
    };

    return (
        <section className="user-results">
            {userSearchResults && userSearchResults.length > 0 ? (
                <div className="user-list">
                    <table className="user-table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone Number</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {userSearchResults.map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>{user.role}</td>
                                    <td>
                                        {isAdmin && (
                                            <button
                                                className="delete-user-button"
                                                onClick={() => handleDelete(user)}
                                            >
                                                Delete
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <p>No users found.</p>
            )}
        </section>
    );
};

export default UserResult;
