package com.hotel.HotelManage.service.impl;

import com.hotel.HotelManage.dto.BookingDTO;
import com.hotel.HotelManage.dto.Response;
import com.hotel.HotelManage.entity.Booking;
import com.hotel.HotelManage.entity.Room;
import com.hotel.HotelManage.entity.User;
import com.hotel.HotelManage.exception.OurException;
import com.hotel.HotelManage.repository.BookingRepository;
import com.hotel.HotelManage.repository.RoomRepository;
import com.hotel.HotelManage.repository.UserRepository;
import com.hotel.HotelManage.service.interfac.IBookingService;
import com.hotel.HotelManage.service.interfac.IRoomService;
import com.hotel.HotelManage.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existBookings) {
        return existBookings.stream()
                .noneMatch(existsBookings ->
                        bookingRequest.getCheckInDate().equals(existsBookings.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existsBookings.getCheckOutDate())

                                || (bookingRequest.getCheckInDate().isAfter(existsBookings.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existsBookings.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().isBefore(existsBookings.getCheckInDate()))
                                && bookingRequest.getCheckOutDate().equals(existsBookings.getCheckOutDate())

                                || (bookingRequest.getCheckInDate().isBefore(existsBookings.getCheckInDate()))
                                && bookingRequest.getCheckOutDate().isAfter(existsBookings.getCheckOutDate())

                                ||(bookingRequest.getCheckInDate().equals(existsBookings.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existsBookings.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existsBookings.getCheckOutDate()))
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())
                );
    }

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try {
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must  come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));

            List<Booking> existsBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest, existsBookings)) {
                throw new OurException("Room not available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(Utils.generateRandomConfirmationCode(10));
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(Utils.generateRandomConfirmationCode(10));

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving booking " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking, true);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking(bookingDTO);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error find booking by confirmation code " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingList(bookingDTOList);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error find all booking" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking not found"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error cancel booking" + e.getMessage());
        }
        return response;
    }
}
