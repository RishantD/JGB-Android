package com.jesusgandhiandbebe.models;

/**
 * Created by rishant_dwivedi on 11/12/15.
 */
public class Picture {

    public String author;
    public String timestamp;
    public byte[] picture;

    /**
     * Creates default Picture
     * @param author
     * @param timestamp
     * @param picture takes in a byte array to represent the picture
     */
    public Picture(String author, String timestamp, byte[] picture) {

        this.author = author;
        this.timestamp = timestamp;
        this.picture = picture;
    }
}
