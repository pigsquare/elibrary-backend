package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.holding.HoldingAddRequestDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.dto.holding.HoldingUpdateRequestDto;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/holdings")
public class HoldingRestController {
    private HoldingService holdingService;

    @Autowired
    public HoldingRestController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    // 获取该isbn图书的条形码号
    @GetMapping("/barcode/{isbn}")
    public CommonResponseDto getBarcodeByIsbn(@PathVariable String isbn){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage(holdingService.getBarcode(isbn));
        return responseDto;
    }
    // 添加藏书
    @PostMapping("/")
    public CommonResponseDto addHolding(@RequestBody @Valid HoldingAddRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(holdingService.addHolding(requestDto));
        return responseDto;
    }
    // 获取指定图书对应的所有藏书信息
    @GetMapping("/books/{isbn}")
    public List<HoldingInfoResponseDto> getInfoByIsbn(@PathVariable String isbn){
        return holdingService.getHoldingsByIsbn(isbn);
    }
    // 更新藏书状态
    @PatchMapping("/{barcode}")
    public CommonResponseDto updateHolding(@PathVariable String barcode, @RequestBody @Valid HoldingUpdateRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(holdingService.updateHolding(barcode,requestDto.getStatus()));
        return responseDto;
    }
}
