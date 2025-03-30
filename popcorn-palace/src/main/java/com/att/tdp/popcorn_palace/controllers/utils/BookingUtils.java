package com.att.tdp.popcorn_palace.controllers.utils;

import java.nio.ByteBuffer;
import java.util.UUID;


public class BookingUtils {
    /*gets the id of the ticket and transforms him to a booking id */
    public static String generateBookingId(Long id) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(id);
        return UUID.nameUUIDFromBytes(buffer.array()).toString();
    }
}

