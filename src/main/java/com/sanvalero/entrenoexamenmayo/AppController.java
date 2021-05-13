package com.sanvalero.entrenoexamenmayo;

import com.sanvalero.entrenoexamenmayo.domain.SwPeople;
import com.sanvalero.entrenoexamenmayo.domain.SwResults;
import com.sanvalero.entrenoexamenmayo.service.SwService;
import com.sanvalero.entrenoexamenmayo.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Creado por @ author: Pedro Orós
 * el 11/05/2021
 */
public class AppController implements Initializable {

    public Label lName, lGender, lHeight, lMass, lBirthYear;
    public TextField tfFilter, tfCharacter, tfPage;
    public TableView<SwPeople> tvData;
    public ProgressIndicator piLoading;
    public ComboBox<String> cbChoose;

    private SwService service;

    private ObservableList<SwPeople> peopleList;
    private List<SwPeople> listCharacters;

    int page = 1;
    int countPage = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        putTableColumnsSw();

        String[] options = new String[]{"<<All>>", "Height", "Mass"};
        cbChoose.setValue("<<All>>");
        cbChoose.setItems(FXCollections.observableArrayList(options));

        service = new SwService();

        peopleList = FXCollections.observableArrayList();

        listCharacters = new ArrayList<>();

        completeLoad();
    }

    @FXML
    public void show(Event event) {

        if(tfFilter.getText().equals("")) {
            String option = cbChoose.getValue();

            switch (option) {
                case "<<All>>":
                    completeLoad();
                    break;

                case "Height":
                    orderByHeight();
                    break;

                case "Mass":
                    orderByMass();
                    break;
            }
        } else {
            orderByGender(tfFilter.getText());
        }
    }

    @FXML
    public void showData(Event event) {
        SwPeople character = tvData.getSelectionModel().getSelectedItem();

        lName.setText(character.getName());
        lGender.setText(character.getGender());
        lHeight.setText(character.getHeight());
        lMass.setText(character.getMass());
        lBirthYear.setText(character.getBirth_year());
    }

    @FXML
    public void character(Event event) {
        int idCharacter = Integer.parseInt(tfCharacter.getText());
        getCharacter(idCharacter);
    }

    @FXML
    public void page(Event event) {
        if (tfPage.getText().equals("") || tfPage.getText().equals("0")) {
            AlertUtils.mostrarError("Debes rellenar el campo Página con un número mayor que cero");
            return;
        }
        page = Integer.parseInt(tfPage.getText());
        completeLoadPage(page);
    }

    @FXML
    public void next(Event event) {
        countPage = page;
        countPage++;
        completeLoadPage(countPage);
    }

    @FXML
    public void prev(Event event) {
        countPage = page;
        countPage--;
        completeLoadPage(countPage);
    }

    public void putTableColumnsSw() {
        Field[] fields = SwPeople.class.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("name")) continue;

            TableColumn<SwPeople, String> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tvData.getColumns().add(column);
        }
        tvData.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void loadingSwPeople() {
        peopleList.clear();
        tvData.setItems(peopleList);

        service.getSwPeople()
                .map(SwResults::getResults)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de personajes de Star Wars descargado"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(sw -> peopleList.add(sw));
    }

    public void loadingSwPeoplePage(int page) {
        peopleList.clear();
        tvData.setItems(peopleList);

        service.getSwPeoplePage(page)
                .map(SwResults::getResults)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de personajes de Star Wars descargado"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(sw -> peopleList.add(sw));
    }

    public void getCharacter(int id) {
        listCharacters.clear();

        SwPeople swPeople = service.getCharacter(id);

        listCharacters.add(swPeople);

        tvData.setItems(FXCollections.observableArrayList(listCharacters));
    }

    public void completeLoad() {
        piLoading.setVisible(true);
        CompletableFuture.runAsync(() -> {
            piLoading.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingSwPeople();})
                .whenComplete((string, throwable) -> piLoading.setVisible(false));
    }

    public void completeLoadPage(int page) {
        piLoading.setVisible(true);
        CompletableFuture.runAsync(() -> {
            piLoading.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingSwPeoplePage(page);})
                .whenComplete((string, throwable) -> piLoading.setVisible(false));
    }

    public void orderByHeight() {
        peopleList.clear();
        service.getSwPeople()
                .map(SwResults::getResults)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de personajes de Star Wars descargado"))
                .subscribe(sw -> peopleList.add(sw));

        tvData.setItems(peopleList.stream()
                .sorted(Comparator.comparing(sw -> Integer.parseInt(sw.getHeight())))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void orderByMass() {
        peopleList.clear();
        service.getSwPeople()
                .map(SwResults::getResults)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de personajes de Star Wars descargado"))
                .subscribe(sw -> peopleList.add(sw));

        tvData.setItems(peopleList.stream()
                .sorted(Comparator.comparing(sw -> Integer.parseInt(sw.getMass())))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void orderByGender(String gender) {
        //peopleList.clear();
        List<SwPeople> genderList = peopleList.stream()
                .filter(people -> people.getGender().equalsIgnoreCase(gender))
                //.distinct()
                .collect(Collectors.toList());
        tvData.setItems(FXCollections.observableArrayList(genderList));
    }
}
