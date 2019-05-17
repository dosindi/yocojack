package com.nyakundid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.appreciated.material.MaterialTheme;
import com.nyakundid.controller.Rule1;
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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.json.JSONArray;
import org.json.JSONObject;
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

    private Logger log = Logger.getLogger(MyUI.class.getName());

    @Value("${konstant.base_url}")
    public String BASE_URL;

    public String PROXY_USR, PROXY_PWD, PROXY_HOST, PROXY_PORT;

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
            //parseTestCase from url

            layout.addComponent(new Label("url: " + BASE_URL));
            scoreData = requestURL();//populateScoreData();
            scoreGrid.setItems(scoreData);
        });

        scoreGrid.addColumn(ScoreTable::getGame).setCaption("Game");
        scoreGrid.addComponentColumn((scoreTable) -> {

            Label lbl1 = new Label();

            if (scoreTable.isPlayerAWins()) {
                lbl1.addStyleName(MaterialTheme.BUTTON_ROUND + " " + MaterialTheme.LABEL_SUCCESS);
            }
            lbl1.setWidth(400, Unit.PIXELS);
            lbl1.setContentMode(ContentMode.HTML);
            //Call Rule 1
            Rule1 rule1 = new Rule1(scoreTable.getPlayerA());
            String result = rule1.resultLabel(rule1.results());
            lbl1.setValue(scoreTable.getPlayerA() + "<br/>" + result);
            return lbl1;
        }).setCaption("Player A");

        scoreGrid.addComponentColumn((scoreTable) -> {
            Label lbl = new Label();

            if (!scoreTable.isPlayerAWins()) {
                lbl.addStyleName(MaterialTheme.BUTTON_ROUND + " " + MaterialTheme.LABEL_SUCCESS);
            }

            lbl.setWidth(400, Unit.PIXELS);
            lbl.setContentMode(ContentMode.HTML);
            //Rule 1
            Rule1 rule1 = new Rule1(scoreTable.getPlayerB());
            String result = rule1.resultLabel(rule1.results());
            lbl.setValue(scoreTable.getPlayerB() + "<br/>" + result);
            return lbl;
        }).setCaption("Player B");

        scoreGrid.addComponentColumn((scoreTable) -> {

            Label lbl = new Label();
            lbl.setContentMode(ContentMode.HTML);

            if (scoreTable.isPlayerAWins()) {
                lbl.setValue(VaadinIcons.TROPHY.getHtml() + " Player A <br/>" + scoreTable.winnerMsg());
            } else if (!scoreTable.isPlayerAWins()) {
                lbl.setValue(VaadinIcons.TROPHY.getHtml() + " Player B <br/>" + scoreTable.winnerMsg());
            }
            return lbl;
        }).setCaption("Winner");

        scoreGrid.setItems(populateScoreData());
        scoreGrid.setSizeFull();
        VerticalLayout header = new VerticalLayout(labels, data, button);
        layout.addComponents(header, scoreGrid);
        layout.setExpandRatio(scoreGrid, 2);
        layout.setExpandRatio(header, 2);
        header.setComponentAlignment(labels, Alignment.TOP_CENTER);
    }

    private void initializeData() {

    }

    private List<ScoreTable> populateScoreData() {

        List<ScoreTable> sd = Arrays.asList(
                new ScoreTable(1, "5H 5D 7C 9S", "2S 4H 8D", true),
                new ScoreTable(2, "AH JC", "10H 6C", false),
                new ScoreTable(3, "AH JC", "6H 5C 10D", true),
                new ScoreTable(4, "10D 6C", "10S 6H", false));

        return sd;
    }

    private List<ScoreTable> requestURL() {

        List<ScoreTable> score = new ArrayList<>();

        try {

//Run behind a Proxy 
            loadConfig();
            client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.MINUTES)
                    .writeTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, Integer.parseInt(PROXY_PORT))))
                    .authenticator((Route route, Response rspns) -> {
                        String credential = Credentials.basic(PROXY_USR, PROXY_PWD);
                        return rspns.request().newBuilder().header("Proxy-Authorization", credential).build();
                    }).build();

            Response response = null;

            Request request = new Request.Builder().url(BASE_URL).get().build();
            response = client.newCall(request).execute();
            String payload = response.body().string();
            log.log(Level.INFO, "Res::{0}", payload);
            if (response.code() == 200) {
                Notification.show("Successfully retrieved testcase", Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show("Error occured on retriving data", Notification.Type.ERROR_MESSAGE);
            }

            JSONArray jsonArray = new JSONArray(payload);
            log.log(Level.INFO, "{0} size::", jsonArray.length());

            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject objects = (JSONObject) jsonArray.get(x);
                ScoreTable s = new ScoreTable();
                int id = x + 1;
                s.setGame(id);
                s.setPlayerA(sanitize(objects.getJSONArray("playerA").toString()));
                s.setPlayerB(sanitize(objects.getJSONArray("playerB").toString()));
                s.setPlayerAWins(objects.getBoolean("playerAWins"));
                score.add(s);
            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            score = objectMapper.readValue(result, new TypeReference<List<ScoreTable>>() {
//            });
        } catch (IOException ex) {
            log.info(ex.toString());
        }

        return score;
    }

    public void loadConfig() {
        PROXY_HOST = "10.1.0.1";
        PROXY_PORT = "8383";
        PROXY_USR = System.getProperty("proxy.authentication.username");
        PROXY_PWD = System.getProperty("proxy.authentication.password");
    }

    public String sanitize(String s) {
        String sanitized = s.replace("[", "").replace("]", "").replace("\",\"", " ").replace("\"", "");
        return sanitized;

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
