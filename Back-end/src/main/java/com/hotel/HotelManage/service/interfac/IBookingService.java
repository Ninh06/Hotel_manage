package com.hotel.HotelManage.service.interfac;

import com.hotel.HotelManage.dto.Response;
import com.hotel.HotelManage.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
