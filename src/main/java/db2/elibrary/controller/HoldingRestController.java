package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.HoldingAddRequestDto;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holding")
public class HoldingRestController {
    private HoldingService holdingService;

    @Autowired
    public HoldingRestController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @RequestMapping("/barcode/{isbn}")
    public CommonResponseDto getBarcodeByIsbn(@PathVariable String isbn){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage(holdingService.getBarcode(isbn));
        return responseDto;
    }

    @PostMapping("/add")
    public CommonResponseDto addHolding(@RequestBody HoldingAddRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(holdingService.addHolding(requestDto));
        return responseDto;
    }
}
