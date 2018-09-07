package com.evnt.ui.components;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.function.Consumer;

public class ConfirmDialog extends Window{
    private final String confirmMessage;
    private final Consumer<Object> onYes;
    private final Consumer<Object> onNo;
    public ConfirmDialog(String confirmMessage, Consumer<Object> onYes, Consumer<Object> onNo){
        this.confirmMessage = confirmMessage;
        this.onYes = onYes;
        this.onNo = onNo;

        build();
        center();
        setClosable(true);
        setModal(true);
        setHeight("20%");
        setWidth("20%");
    }

    private void build(){
        Label header = new Label("<h2>Confirm</h2>", ContentMode.HTML);
        Label areYouSureLabel = new Label(confirmMessage);
        areYouSureLabel.setWidth("90%");
        Button yesBtn = new Button("Yes");
        yesBtn.addClickListener(onYes::accept);
        yesBtn.addClickListener(click -> close());
        Button noBtn = new Button("No");
        noBtn.addClickListener(onNo::accept);
        noBtn.addClickListener(click -> close());
        HorizontalLayout buttonLayout = new HorizontalLayout(yesBtn, noBtn);
        buttonLayout.setSpacing(true);
        VerticalLayout content = new VerticalLayout(header, areYouSureLabel, buttonLayout);
        content.setSpacing(true);
        content.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
        content.setMargin(new MarginInfo(false, true, true, true));
        content.setSizeFull();
        setContent(content);
    }
}
