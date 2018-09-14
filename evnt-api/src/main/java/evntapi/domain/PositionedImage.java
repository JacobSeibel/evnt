package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PositionedImage {
    private Integer pk;
    private byte[] image;
    private int scrollTop;
    private int scrollLeft;
}
