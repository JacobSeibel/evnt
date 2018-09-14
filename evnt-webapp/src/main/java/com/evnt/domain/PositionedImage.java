package com.evnt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class PositionedImage {
    private Integer pk;
    private byte[] image;
    private int scrollTop;
    private int scrollLeft;

    public PositionedImage(byte[] image, int scrollTop, int scrollLeft){
        this.image = image;
        this.scrollTop = scrollTop;
        this.scrollLeft = scrollLeft;
    }
}
