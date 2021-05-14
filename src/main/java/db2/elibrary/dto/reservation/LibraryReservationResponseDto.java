package db2.elibrary.dto.reservation;

import db2.elibrary.entity.Reservation;
import lombok.Data;

@Data
public class LibraryReservationResponseDto {
    // 返回详细信息，包括用户、图书、条码
    private Integer id;
    private String readerName;
    private String bookName;
    private String lastDate;
    private String isbn;
    private String barcode;

    public LibraryReservationResponseDto(Reservation reservation){
        this.id = reservation.getId();
        this.bookName = reservation.getBookInfo().getName();
        this.isbn = reservation.getBookInfo().getIsbn();
        this.readerName = reservation.getUser().getName();
        if(reservation.getBook() != null){
            this.barcode = reservation.getBook().getBarcode();
            this.lastDate = reservation.getLastDate().toString();
        } else{
            this.barcode = "---";
            this.lastDate = "---";
        }
    }
}
