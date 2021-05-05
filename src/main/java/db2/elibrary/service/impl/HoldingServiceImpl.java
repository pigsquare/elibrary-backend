package db2.elibrary.service.impl;

import db2.elibrary.dto.holding.HoldingAddRequestDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.entity.Admin;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BookRepository;
import db2.elibrary.repository.HoldingRepository;
import db2.elibrary.service.HoldingService;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HoldingServiceImpl implements HoldingService {
    private final BookRepository bookRepository;
    private final AdminRepository adminRepository;
    private final HoldingRepository holdingRepository;

    @Autowired
    public HoldingServiceImpl(BookRepository bookRepository, AdminRepository adminRepository, HoldingRepository holdingRepository) {
        this.bookRepository = bookRepository;
        this.adminRepository = adminRepository;
        this.holdingRepository = holdingRepository;
    }

    @Override
    public String getBarcode(String isbn) {
        if (bookRepository.findById(isbn).isEmpty()) {
            throw new AuthException("该书不存在");
        }
        String isbnWithTimestamp = isbn + System.currentTimeMillis();
        int barcode = Math.abs(isbnWithTimestamp.hashCode() % 89999999) + 10000000;
        while (holdingRepository.findByBarcode(String.valueOf(barcode)).isPresent()) {
            barcode = (int) ((barcode + Math.round(Math.random() * 1000) % 89999999) + 10000000);
        }
        return String.valueOf(barcode);
    }

    @Override
    public Boolean addHolding(HoldingAddRequestDto requestDto) {
        Holding holding = new Holding();
        String uuid = UserUtil.getCurrentUserAccount();
        Optional<Admin> optionalAdmin = adminRepository.findByUserId(uuid);
        if (optionalAdmin.isEmpty()) {
            throw new AuthException("");
        }
        optionalAdmin.ifPresent(holding::setAdmin);
        holding.setBarcode(requestDto.getBarcode());
        var optionalBook = bookRepository.findById(requestDto.getIsbn());
        if (optionalBook.isEmpty()) {
            throw new AuthException("");
        }
        holding.setBook(optionalBook.get());
        holding.setStatus(BookStatusEnum.valueOf(requestDto.getStatus()));
        holdingRepository.save(holding);
        return true;
    }

    @Override
    public List<HoldingInfoResponseDto> getHoldingsByIsbn(String isbn) {
        var optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty()) {
            throw new NotFoundException("");
        }
        var holdings = holdingRepository.findByBook(optionalBook.get());
        List<HoldingInfoResponseDto> res = new ArrayList<>();
        for (Holding holding : holdings) {
            res.add(new HoldingInfoResponseDto(holding));
        }
        return res;
    }

    @Override
    public Holding getHoldingByBarcode(String barcode) {
        Optional<Holding> optionalHolding = holdingRepository.findByBarcode(barcode);
        if(optionalHolding.isEmpty()){
            throw new NotFoundException("");
        }
        return optionalHolding.get();
    }

    @Override
    public Boolean updateHolding(String barcode, String status) {
        Optional<Holding> holdingOptional = holdingRepository.findByBarcode(barcode);
        if(holdingOptional.isEmpty()){
            throw new NotFoundException("该藏书号不存在");
        }
        Holding holding = holdingOptional.get();
        holding.setStatus(BookStatusEnum.valueOf(status));
        holdingRepository.save(holding);
        return true;
    }
}
