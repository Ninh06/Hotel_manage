package com.hotel.HotelManage.service.impl;

import com.hotel.HotelManage.dto.LoginRequest;
import com.hotel.HotelManage.dto.Response;
import com.hotel.HotelManage.dto.UserDTO;
import com.hotel.HotelManage.entity.User;
import com.hotel.HotelManage.exception.OurException;
import com.hotel.HotelManage.repository.BookingRepository;
import com.hotel.HotelManage.repository.UserRepository;
import com.hotel.HotelManage.utils.JWTUtils;
import com.hotel.HotelManage.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService implements com.hotel.HotelManage.service.interfac.IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if(Objects.equals(user.getRole(), "ADMIN")) {
                throw new OurException("Bạn không thể đăng ký với vai trò này");
            }
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            if(userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saveUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(saveUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("User not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful!");

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUser() {
        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(Long userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);


        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user booking history " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateUser(Long userId, User updateUser) {
        Response response = new Response();
        try {
            User existsUser = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            if(updateUser.getName() != null) {
                existsUser.setName(updateUser.getName());
            }
            if(updateUser.getPhoneNumber() != null && !updateUser.getPhoneNumber().isEmpty()) {
                existsUser.setPhoneNumber(updateUser.getPhoneNumber());
            }
            if(updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()){
                String newPassword = updateUser.getPassword();
                existsUser.setPassword(passwordEncoder.encode(newPassword));
            }

            User saveUser = userRepository.save(existsUser);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(saveUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();

        try {
            userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));


            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting user " + e.getMessage());
        }
        return response;

    }

    @Override
    public Response getUserById(Long userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OurException("User not found"));
    }
}
