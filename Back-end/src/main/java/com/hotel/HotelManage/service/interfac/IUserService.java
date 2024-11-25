package com.hotel.HotelManage.service.interfac;

import com.hotel.HotelManage.dto.LoginRequest;
import com.hotel.HotelManage.dto.Response;
import com.hotel.HotelManage.entity.User;

public interface IUserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUser();
    Response getUserBookingHistory(Long userId);
    Response updateUser(Long userId,User user);
    Response deleteUser(Long userId);
    Response getUserById(Long userId);
    Response getMyInfo(String email);
    public User findUserByEmail(String email);


}
