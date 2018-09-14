package com.evnt.ui;

import com.evnt.domain.User;
import com.evnt.persistence.UserDelegateService;
import com.evnt.persistence.VaadinUIService;
import com.evnt.persistence.VaadinUIServiceImpl;
import com.evnt.ui.events.LogoutEvent;
import com.evnt.ui.events.NavigationEvent;
import com.evnt.ui.security.SecurityErrorHandler;
import com.evnt.ui.security.ViewAccessDecisionManager;
import com.evnt.ui.views.ErrorView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

@SpringUI(path = "")
@Theme("EvntWebapp")
@PreserveOnRefresh
public class EvntWebappUI extends UI {

	private static final Logger LOG = LoggerFactory.getLogger(EvntWebappUI.class);
	private static final VaadinUIService uiService = new VaadinUIServiceImpl();    @Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private ViewAccessDecisionManager viewAccessDecisionManager;

	@Autowired
	private UserDelegateService userService;

	private EventBus eventbus;

	public static EvntWebappUI getCurrent() {
		return (EvntWebappUI) UI.getCurrent();
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		buildNavigator();
		VaadinSession.getCurrent().setErrorHandler(new SecurityErrorHandler(eventbus, getNavigator()));

		viewAccessDecisionManager.checkAccessRestrictionForRequestedView(getNavigator());

		Page.getCurrent().setTitle("evnt");
	}

	private void buildNavigator() {
		Navigator navigator = new Navigator(this, this);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(ErrorView.class);
		setNavigator(navigator);
	}

	public EventBus getEventbus() {
		return eventbus;
	}

	public User getCurrentUser() {
		if (isUserAnonymous()) {
			return null;
		} else {
			return userService.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
	}

	@PostConstruct
	private void initEventbus() {
		eventbus = new EventBus("main");
		eventbus.register(this);
	}

	public boolean isUserAnonymous() {
		return SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
	}

	@Subscribe
	public void userLoggedOut(LogoutEvent event) throws ServletException {
		((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest().logout();
		VaadinSession.getCurrent().close();
		Page.getCurrent().setLocation("/");
	}

	@Subscribe
	public void handleNavigation(NavigationEvent event) {
		getNavigator().navigateTo(event.getTarget());
	}

	public static VaadinUIService getUiService() {
		return uiService;
	}
}
