package com.hotel.HotelManage.service.interfac;

import com.hotel.HotelManage.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(Long roomId);
    Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description);
    Response getRoomById(Long roomId);
    Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
    Response getAllAvailableRooms();
}
