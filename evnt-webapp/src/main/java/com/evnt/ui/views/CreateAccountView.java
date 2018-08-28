package com.evnt.ui.views;

import com.evnt.domain.SecurityRole;
import com.evnt.domain.User;
import com.evnt.persistence.SecurityRoleDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.GoToMainViewLink;
import com.evnt.ui.events.NavigationEvent;
import com.google.common.eventbus.EventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringView(name = CreateAccountView.NAME)
public class CreateAccountView extends AbstractView {

    public final static String NAME = "create-account";

    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private UserDelegateService userService;
    @Autowired
    private SecurityRoleDelegateService securityService;

    private String forwardTo;

    public CreateAccountView() {
        Binder<User> binder = new Binder<>();

        TextField usernameField = new TextField("Username");
        binder.forField(usernameField)
                .asRequired("Username is required!")
                .withValidator(str -> userService.findByUsername(str) == null, "Username is already in use!")
                .bind(User::getUsername, User::setUsername);
        PasswordField passwordField = new PasswordField("Password");
        binder.forField(passwordField)
                .asRequired("Password is required!");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        binder.forField(confirmPasswordField)
                .asRequired("Please confirm password!")
                .withValidator(str -> str.equals(passwordField.getValue()), "Passwords do not match!")
                .bind(User::getPassword, User::setUnencryptedPassword);
        TextField emailField = new TextField("Email Address");
        binder.forField(emailField)
                .asRequired("Email is required!")
                .withValidator(new EmailValidator("Invalid email address!"))
                .withValidator(str -> userService.findByEmail(str) == null, "An account is already registered for this email!")
                .bind(User::getEmail, User::setEmail);
        //TODO: Phone number validation and hint to help user type number correctly
        TextField cellNumberField = new TextField("Cell Phone Number");
        binder.forField(cellNumberField)
                .bind(User::getCellNumber, User::setCellNumber);
        TextField firstNameField = new TextField("First Name");
        binder.forField(firstNameField)
                .bind(User::getFirstName, User::setFirstName);
        TextField lastNameField = new TextField("Last Name");
        binder.forField(lastNameField)
                .bind(User::getLastName, User::setLastName);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setEnabled(false);
        signUpButton.addClickListener(click -> {
            User user = new User();
            binder.writeBeanIfValid(user);
            createAccount(user, passwordField.getValue());
        });
        binder.addValueChangeListener(change ->{
            signUpButton.setEnabled(binder.isValid());
        });

        addComponent(usernameField);
        addComponent(passwordField);
        addComponent(confirmPasswordField);
        addComponent(emailField);
        addComponent(cellNumberField);
        addComponent(firstNameField);
        addComponent(lastNameField);
        addComponent(signUpButton);
    }

    private void createAccount(User user, String unencryptedPassword){
        user.addSecurityRole(SecurityRole.ROLE_USER, securityService);
        userService.insert(user);
        Notification success = new Notification("Successfully created user "+user.getUsername());
        success.setDelayMsec(3000);
        success.show(EvntWebappUI.getCurrent().getPage());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), unencryptedPassword);
        if (userAuthenticationService.loginUser(authentication)) {
            EventBus eventbus = EvntWebappUI.getCurrent().getEventbus();
            eventbus.post(new NavigationEvent(this, forwardTo));
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        forwardTo = event.getParameters();
    }
}
