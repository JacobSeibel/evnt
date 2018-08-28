package com.evnt.ui.views;

import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.GoToMainViewLink;
import com.evnt.ui.events.NavigationEvent;
import com.evnt.util.BCrypt;
import com.google.common.eventbus.EventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringView(name = LoginView.NAME)
public class LoginView extends AbstractView {

    public final static String NAME = "login";

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    private String forwardTo;
    private TextField nameTF;
    private PasswordField passwordTF;

    public LoginView() {
        addComponent(new Label(
                "Please enter your credentials:"));
        nameTF = new TextField();
        nameTF.setRequiredIndicatorVisible(true);
        nameTF.focus();

        passwordTF = new PasswordField();
        passwordTF.setRequiredIndicatorVisible(true);

        addComponent(nameTF);
        addComponent(passwordTF);

        Button loginButton = new Button("Login");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(click -> {
            if (nameTF.getValue() != null && !nameTF.getValue().isEmpty() && passwordTF.getValue() != null && !passwordTF.isEmpty()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(nameTF.getValue(), passwordTF.getValue());
                if (userAuthenticationService.loginUser(authentication)) {
                    EventBus eventbus = EvntWebappUI.getCurrent().getEventbus();
                    eventbus.post(new NavigationEvent(this, forwardTo));
                } else {
                    passwordTF.setValue("");
                }
            } else {
                if (nameTF.isEmpty()) {
                    nameTF.setErrorHandler(error -> Notification.show("Username required!", Notification.Type.ERROR_MESSAGE));
                }
                if (passwordTF.isEmpty()) {
                    passwordTF.setErrorHandler(error -> Notification.show("Password required!", Notification.Type.ERROR_MESSAGE));
                }
            }
        });
        loginButton.setIcon(VaadinIcons.SIGN_IN);
        addComponent(loginButton);

        Link signUpLink = new Link("Don't have an account? Sign up now!", new ExternalResource("#!" + CreateAccountView.NAME));
        addComponent(signUpLink);

        addComponent(new GoToMainViewLink());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        forwardTo = event.getParameters();
    }

    public static String loginPathForRequestedView(String requestedViewName) {
        return NAME + "/" + requestedViewName;
    }
}
