package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = "")
public class RedirectToMainView extends Navigator.EmptyView {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        EvntWebappUI.getCurrent().getNavigator().navigateTo(MainView.NAME);
    }
}
