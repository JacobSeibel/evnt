package com.evnt.ui.components;

import com.evnt.ui.Theme;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RepositionableImage extends Panel{

    private Image image;
    private String imageCaption;
    private boolean isEditable;

    public RepositionableImage(){
        this(null);
    }

    public RepositionableImage(String imageCaption) {
        this(imageCaption, false);
    }

    public RepositionableImage(boolean isEditable){
        this(null, isEditable);
    }

    public RepositionableImage(String imageCaption, boolean isEditable){
        this.imageCaption = imageCaption;
        this.isEditable = isEditable;

        build();
    }

    private void build(){
        image = imageCaption != null ? new Image(imageCaption) : new Image();
        image.setWidth("100%");
        setContent(image);
        setHeight("300px");
        setWidth("100%");
        addStyleName(Theme.NO_SCROLL);
        if(isEditable) {
            setDescription("Click to reposition");
            addStyleName(Theme.REPOSITIONABLE);
            image.addClickListener(click -> {
                if (getStyleName().contains(Theme.NO_SCROLL)) {
                    removeStyleName(Theme.NO_SCROLL);
                    setDescription("Click to save position");
                } else {
                    addStyleName(Theme.NO_SCROLL);
                    setDescription("Click to reposition");
                }
            });
        }
    }
}
