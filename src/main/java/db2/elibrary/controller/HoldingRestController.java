package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.HoldingAddRequestDto;
import db2.elibrary.dto.HoldingInfoResponseDto;
import db2.elibrary.dto.HoldingUpdateRequestDto;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public CommonResponseDto addHolding(@RequestBody @Valid HoldingAddRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(holdingService.addHolding(requestDto));
        return responseDto;
    }

    @GetMapping("/info/{isbn}")
    public List<HoldingInfoResponseDto> getInfoByIsbn(@PathVariable String isbn){
        return holdingService.getHoldingsByIsbn(isbn);
    }

    // TODO: update book status
    @PostMapping("/update")
    public CommonResponseDto updateHolding(@RequestBody @Valid HoldingUpdateRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(null);
        return responseDto;
    }
}
