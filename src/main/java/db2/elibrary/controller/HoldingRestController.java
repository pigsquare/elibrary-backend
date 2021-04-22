package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
