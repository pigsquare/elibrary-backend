package db2.elibrary.service.impl;

import db2.elibrary.dto.HoldingAddRequestDto;
import db2.elibrary.entity.Admin;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BookRepository;
import db2.elibrary.repository.HoldingRepository;
import db2.elibrary.service.HoldingService;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class HoldingServiceImpl implements HoldingService {
    private BookRepository bookRepository;
    private AdminRepository adminRepository;
    private HoldingRepository holdingRepository;

    @Autowired
    public HoldingServiceImpl(BookRepository bookRepository, AdminRepository adminRepository, HoldingRepository holdingRepository) {
        this.bookRepository = bookRepository;
        this.adminRepository = adminRepository;
        this.holdingRepository = holdingRepository;
    }

    @Override
    public String getBarcode(String isbn) {
        if(bookRepository.findById(isbn).isEmpty()){
            throw new AuthException("该书不存在");
        }
        String isbnWithTimestamp = isbn + System.currentTimeMillis();
        int barcode = Math.abs(isbnWithTimestamp.hashCode() % 89999999) + 10000000;
        while (holdingRepository.findByBarcode(String.valueOf(barcode)).isPresent()){
            barcode = (int) ((barcode + Math.round(Math.random()*1000) % 89999999) + 10000000);
        }
        return String.valueOf(barcode);
    }

    @Override
    public Boolean addHolding(HoldingAddRequestDto requestDto) {
        Holding holding = new Holding();
        String uuid = UserUtil.getCurrentUserAccount();
        Optional<Admin> optionalAdmin = adminRepository.findByUserId(uuid);
        if(optionalAdmin.isEmpty()){
            throw new AuthException("");
        }
        optionalAdmin.ifPresent(holding::setAdmin);
        holding.setBarcode(requestDto.getBarcode());
        var optionalBook = bookRepository.findById(requestDto.getIsbn());
        if(optionalBook.isEmpty()){
            throw new AuthException("");
        }
        holding.setBook(optionalBook.get());
        holding.setStatus(BookStatusEnum.AVAILABLE);
        holdingRepository.save(holding);
        return true;
    }
}
