package com.nyakundid;

import com.nyakundid.model.ScoreTable;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("demo")
@SpringUI(path = "/")
public class MyUI extends UI {

    public Grid<ScoreTable> scoreGrid = new Grid<>();
    public Button button = new Button("Parse TestCases", VaadinIcons.DOWNLOAD);

    public TextField data = new TextField();
    public VerticalLayout layout = new VerticalLayout();
    public List<ScoreTable> scoreData = new ArrayList<>();
    public Label labelHeader = new Label("YOCOJACK");

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        buildLayout();

        initializeData();

        setContent(layout);
    }

    private void buildLayout() {

        button.addClickListener(e -> {
            //TODO parseTestCase from url
            
            layout.addComponent(new Label("Data: " + data.getValue()));
            scoreData = populateScoreData();
            scoreGrid.setItems(scoreData);
        });

        scoreGrid.addColumn(ScoreTable::getGame).setCaption("Game");
        scoreGrid.addColumn(ScoreTable::getPlayerA).setCaption("Player A");
        scoreGrid.addColumn(ScoreTable::getPlayerB).setCaption("Player B");
        scoreGrid.addColumn(ScoreTable::getWinner).setCaption("Winner");
        scoreGrid.setSizeFull();

        layout.addComponents(labelHeader,data, button, scoreGrid);
    }

    private void initializeData() {

    }

    private List<ScoreTable> populateScoreData() {

        List<ScoreTable> sd = Arrays.asList(new ScoreTable(1, "5H 5D 7C 9S", "2S 4H 8D", "Player B"));

        return sd;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
