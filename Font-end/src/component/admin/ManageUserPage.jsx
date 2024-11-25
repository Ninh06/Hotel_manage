import React, { useState, useEffect } from 'react';
import ApiService from '../../service/ApiService';
import Pagination from '../common/Pagination';
import UserResult from '../common/UserResult';

const ManageUserPage = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [userRoles, setUserRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [usersPerPage] = useState(5);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await ApiService.getAllUsers();
        const allUsers = response.userList;
        setUsers(allUsers);
        setFilteredUsers(allUsers);
      } catch (error) {
        console.error('Error fetching users:', error.message);
      }
    };

    const fetchUserRoles = async () => {
      const roles = ['USER', 'ADMIN'];
      setUserRoles(roles);
    };

    fetchUsers();
    fetchUserRoles();
  }, []);

  const handleRoleChange = (e) => {
    setSelectedRole(e.target.value);
    filterUsersByRole(e.target.value);
  };

  const filterUsersByRole = (role) => {
    if (role === '') {
      setFilteredUsers(users);
    } else {
      const filtered = users.filter((user) => user.role === role);
      setFilteredUsers(filtered);
    }
    setCurrentPage(1);
  };

  const handleUserDelete = (userId) => {
    const updatedUsers = users.filter(user => user.id !== userId);
    setUsers(updatedUsers);
    setFilteredUsers(updatedUsers);
  };

  const indexOfLastUser = currentPage * usersPerPage;
  const indexOfFirstUser = indexOfLastUser - usersPerPage;
  const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className='all-users'>
      <h2>All Users</h2>
      <div className='all-user-filter-div' style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div className='filter-select-div'>
          <label>Filter by Role:</label>
          <select value={selectedRole} onChange={handleRoleChange}>
            <option value="">All</option>
            {userRoles.map((role) => (
              <option key={role} value={role}>
                {role}
              </option>
            ))}
          </select>
        </div>
      </div>

      <UserResult userSearchResults={currentUsers} onDelete={handleUserDelete} />

      <Pagination
        itemsPerPage={usersPerPage}
        totalItems={filteredUsers.length}
        currentPage={currentPage}
        paginate={paginate}
      />
    </div>
  );
};

export default ManageUserPage;
