package com.api.model;


public class BookingDTO {

    private BookingDetailsDTO bookingDetailsDTO;
    private String bookingid;

    public BookingDetailsDTO getBooking() {
        return bookingDetailsDTO;
    }

    public void setBooking(BookingDetailsDTO bookingDetailsDTO) {
        this.bookingDetailsDTO = bookingDetailsDTO;
    }

    public String getBookingid() {
        return bookingid;
    }

    public void setBookingid(String bookingid) {
        this.bookingid = bookingid;
    }

    @Override
    public String toString() {
        return "ClassPojo [booking = " + bookingDetailsDTO + ", bookingid = "+bookingid + "]";
    }


}
