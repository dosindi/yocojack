package com.nyakundid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.appreciated.material.MaterialTheme;
import com.nyakundid.model.ScoreTable;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Duncan Nyakundi
 * @version 1.0
 * @date 13/05/2019
 *
 * This Yocajack UI is the application entry point. A UI may either represent a
 * browser window (or tab) or some part of an HTML page where a Vaadin
 * application is embedded.
 */
@Title("YOCOJACK")
@Theme("demo")
@SpringUI(path = "/")
public class MyUI extends UI {

    @Value("${konstant.base_url}")
    public String BASE_URL;

    private Logger log = Logger.getLogger(MyUI.class.getName());

    public Grid<ScoreTable> scoreGrid = new Grid<>();
    public Button button = new Button("Parse TestCases", VaadinIcons.DOWNLOAD);

    public TextField data = new TextField();
    public VerticalLayout layout = new VerticalLayout();
    public List<ScoreTable> scoreData = new ArrayList<>();
    public Label title = new Label("YOCOJACK");
    private OkHttpClient client;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        buildLayout();

        initializeData();

        setContent(layout);
    }

    private void buildLayout() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_LIGHT + " " + ValoTheme.LABEL_COLORED);
        labels.addComponent(title);

        Label by = new Label(" by ");
        by.setSizeUndefined();
        by.addStyleName(ValoTheme.LABEL_SMALL);
        by.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        labels.addComponent(by);

        Label name = new Label("Duncan Nyakundi");
        name.setSizeUndefined();
        name.addStyleName(ValoTheme.LABEL_H4);
        name.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(name);

        button.addClickListener(e -> {
            //TODO parseTestCase from url
//            String urlData = requestURL();

//            scoreData = requestURL();
            layout.addComponent(new Label("url: " + BASE_URL));
            scoreData = populateScoreData();
            scoreGrid.setItems(scoreData);
        });

        scoreGrid.addColumn(ScoreTable::getGame).setCaption("Game");
        scoreGrid.addComponentColumn((scoreTable) -> {

            Label lbl1 = new Label();
            lbl1.addStyleName(MaterialTheme.BUTTON_ROUND+" "+MaterialTheme.LABEL_SUCCESS);
            lbl1.setWidth(400, Unit.PIXELS);
            lbl1.setContentMode(ContentMode.HTML);
            //Call Rule 1
            lbl1.setValue(scoreTable.getPlayerA() + "<br/>(Over 21)");
            return lbl1;
        }).setCaption("Player A");

        scoreGrid.addComponentColumn((scoreTable) -> {
            Label lbl = new Label();
            lbl.addStyleName(MaterialTheme.BUTTON_ROUND+" "+MaterialTheme.LABEL_SUCCESS);
            lbl.setWidth(400, Unit.PIXELS);
            lbl.setContentMode(ContentMode.HTML);
            
            //Call Rule 2
            lbl.setValue(scoreTable.getPlayerB() + "<br/>(Score)");
            return lbl;
        }).setCaption("Player B");

        scoreGrid.addComponentColumn((scoreTable) -> {

            Label lbl = new Label();
            lbl.setContentMode(ContentMode.HTML);
            if (scoreTable.isPlayerAWins()) {
                lbl.setValue("Player A <br/>(Higher value hand)");
            } else if (!scoreTable.isPlayerAWins()) {
                lbl.setValue("Player B <br/>(Player A  over 21)");
            }
            return lbl;
        }).setCaption("Winner");

        scoreGrid.setSizeFull();

        layout.addComponents(labels, data, button, scoreGrid);
        layout.setComponentAlignment(labels, Alignment.TOP_CENTER);
    }

    private void initializeData() {

    }

    private List<ScoreTable> populateScoreData() {

        List<ScoreTable> sd = Arrays.asList(
                new ScoreTable(1, "5H 5D 7C 9S", "2S 4H 8D", true),
                new ScoreTable(2, "AH JC ", "10H 6C", false),
                new ScoreTable(3, "AH JC", "6H 5C 10D", true));

        return sd;
    }

    private List<ScoreTable> requestURL() {
        List<ScoreTable> score = new ArrayList<>();
        try {
            String result = "["
                    + "	{"
                    + "		\"playerA\": ["
                    + "			\"8D\","
                    + "			\"JS\""
                    + "		],"
                    + "		\"playerAWins\": false,"
                    + "		\"playerB\": ["
                    + "			\"5D\","
                    + "			\"QS\","
                    + "			\"3H\""
                    + "		]"
                    + "	},"
                    + "	{"
                    + "		\"playerA\": ["
                    + "			\"10D\","
                    + "			\"JH\""
                    + "		],"
                    + "		\"playerAWins\": true,"
                    + "		\"playerB\": ["
                    + "			\"QS\","
                    + "			\"4C\","
                    + "			\"JC\""
                    + "		]"
                    + "	}"
                    + "]";
            try {
                Request request = new Request.Builder()
                        .url(BASE_URL)
                        .build();
                client = new OkHttpClient();
                Call call = client.newCall(request);
                Response response = call.execute();

                if (response.code() == 200) {
                    Notification.show("Successfully retrieved testcase", Notification.Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show("Error occured on retriving data", Notification.Type.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                log.info(ex.toString());
            }

            ObjectMapper objectMapper = new ObjectMapper();

            score = objectMapper.readValue(result, new TypeReference<List<ScoreTable>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(MyUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return score;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
