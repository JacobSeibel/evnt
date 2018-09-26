package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Email {
    private Integer pk;
    private String name;
    private String description;
    private String subjectLine;
    private String freemarkerTemplate;
    private boolean isActive;
}
