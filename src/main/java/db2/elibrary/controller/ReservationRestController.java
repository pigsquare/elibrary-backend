package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.entity.Holding;
import db2.elibrary.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/library/reserved")
    public List<HoldingInfoResponseDto> getReservedBookInLibrary(){
        List<HoldingInfoResponseDto> holdingInfoResponseDtoList = new ArrayList<>();
        List<Holding> holdingList = reservationService.getReservedBookInLibrary();
        for(Holding holding:holdingList){
            holdingInfoResponseDtoList.add(new HoldingInfoResponseDto(holding));
        }
        return holdingInfoResponseDtoList;
    }

}
