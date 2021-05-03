package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationRestController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/new/{isbn}")
    public CommonResponseDto makeReservation(@PathVariable String isbn){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage(reservationService.makeReservation(isbn));
        return responseDto;
    }

    @DeleteMapping("/del/{id}")
    public CommonResponseDto cancelReservation(@PathVariable Integer id){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(reservationService.cancelReservation(id));
        return responseDto;
    }

    // TODO: 管理员功能，获取当前所有处于RESERVED状态的在馆图书列表，需要定义一个返回类
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/library/reserved")
    public CommonResponseDto getReservedBookInLibrary(){
        return null;
    }

}
