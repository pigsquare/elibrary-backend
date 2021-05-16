package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.dto.reservation.LibraryReservationResponseDto;
import db2.elibrary.dto.reservation.PersonalReservationResponseDto;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.Reservation;
import db2.elibrary.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationRestController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    // 增加订阅记录
    @PostMapping("/books/{isbn}")
    public CommonResponseDto makeReservation(@PathVariable String isbn){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage(reservationService.makeReservation(isbn));
        return responseDto;
    }
    // 删除订阅记录
    @DeleteMapping("/{id}")
    public CommonResponseDto cancelReservation(@PathVariable Integer id){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(reservationService.cancelReservation(id));
        return responseDto;
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/reserved-holdings")
    public List<LibraryReservationResponseDto> getReservedBookInLibrary(){
        List<LibraryReservationResponseDto> libraryReservationResponseDtoList = new ArrayList<>();
        List<Reservation> reservationList= reservationService.getReservedBookInLibrary();
        for(Reservation reservation:reservationList){
            libraryReservationResponseDtoList.add(new LibraryReservationResponseDto(reservation));
        }
        return libraryReservationResponseDtoList;
    }


    @GetMapping("/user/reserved")
    public List<PersonalReservationResponseDto> getUserUncompletedReservation(){
        List<PersonalReservationResponseDto> personalReservationResponseDtoList = new ArrayList<>();
        List<Reservation> reservationList = reservationService.getUserUncompletedReservation();
        for(Reservation reservation:reservationList){
            personalReservationResponseDtoList.add(new PersonalReservationResponseDto(reservation));
        }
        return personalReservationResponseDtoList;
    }

    @GetMapping("/user/all")
    public List<PersonalReservationResponseDto> getUserReservation(){
        List<PersonalReservationResponseDto> personalReservationResponseDtoList = new ArrayList<>();
        List<Reservation> reservationList = reservationService.getUserReservation();
        for(Reservation reservation:reservationList){
            personalReservationResponseDtoList.add(new PersonalReservationResponseDto(reservation));
        }
        return personalReservationResponseDtoList;
    }
}
