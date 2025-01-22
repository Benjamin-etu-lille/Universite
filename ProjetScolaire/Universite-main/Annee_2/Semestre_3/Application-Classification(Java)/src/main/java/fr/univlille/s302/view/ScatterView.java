package fr.univlille.s302.view;

import java.util.ArrayList;
import java.util.List;

import fr.univlille.s302.controller.Controller;
import fr.univlille.s302.listener.Observer;
import fr.univlille.s302.model.Data;
import fr.univlille.s302.model.DataSet;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Classe {@code ScatterView} qui représente une vue de graphique en nuage de points.
 *
 * Cette classe étend {@link Stage} et implémente l'interface {@link Observer}.
 * Elle permet à l'utilisateur de visualiser un graphique en nuage de points basé sur un
 * ensemble de données, d'ajouter des points, et de classifier les données à l'aide
 * de l'algorithme KNN. Elle fournit également des contrôles pour interagir avec le graphique.
 *
 * @author Thibault Croisier & Benjamin Sere & Louis Bedu
 * @version 2.2
 */
public class ScatterView extends Stage implements Observer {

    private Scene scene;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private ScatterChart<Number, Number> chart;
    private DataSet model;
    private BorderPane root;
    private HBox controlsBox;
    private Button fileChooserButton;
    private Button newGraphButton;
    private Button newPointButton;
    private Button helpButton;
    private ComboBox<String> xAxisBox;
    private ComboBox<String> yAxisBox;
    private ComboBox<String> distancesBox;
    private Controller myController;
    private Spinner<Integer> kValueSpinner;
    private Button classifyData;
    private Label knnRobustnessLabel;
    private VBox classifyBox;


    /**
     * Constructeur qui initialise la vue du graphique en nuage de points.
     *
     * @param model l'ensemble de données à afficher
     * @param myController le contrôleur associé à la vue
     */
    public ScatterView(DataSet model, Controller myController) {
        this.model = model;
        this.myController = myController;
        this.model.attach(this);

        // Initialisation des ComboBox pour les axes
        this.xAxisBox = new ComboBox<>(FXCollections.observableArrayList(this.model.getAttributes()));
        this.yAxisBox = new ComboBox<>(FXCollections.observableArrayList(this.model.getAttributes()));
        this.xAxisBox.getSelectionModel().select(2);
        this.yAxisBox.getSelectionModel().select(3);

        // Initialise le ComboBox pour les distances
        this.distancesBox = new ComboBox<>(FXCollections.observableArrayList(this.model.getDistances()));
        this.distancesBox.getSelectionModel().select(1);

        // Axes du graphique
        this.xAxis = new NumberAxis();
        this.yAxis = new NumberAxis();
        this.xAxis.setLabel(this.xAxisBox.getSelectionModel().getSelectedItem());
        this.yAxis.setLabel(this.yAxisBox.getSelectionModel().getSelectedItem());
        this.chart = new ScatterChart<>(xAxis, yAxis);

        // Ajout des boutons
        this.fileChooserButton = createStyledButton("📁 Load Data", "Choisir un fichier de données.");
        this.newGraphButton = createStyledButton("📊 New Graph", "Créer un nouveau graphique.");
        this.newPointButton = createStyledButton("➕ Add Point", "Ajouter un point au graphique.");
        this.helpButton = createHelpButton("Help", "Afficher l'aide et les fonctionnalités.");

        // Regrouper le label et le spinner dans une HBox (déjà fait dans votre code)
        Label knnLabel = new Label("Knn value");
        this.kValueSpinner = new Spinner<>();
        this.kValueSpinner.setPrefSize(90, 25);
        this.kValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
        this.classifyData = createStyledButton("🔍 Classify Data", "Classer les données avec KNN.");

        // Regrouper le label et le spinner dans une VBox
        HBox spinnerBox = new HBox(knnLabel, kValueSpinner);
        spinnerBox.setSpacing(10);
        spinnerBox.setAlignment(Pos.CENTER);
        spinnerBox.setStyle("-fx-padding: 10;");

        Label classifyLabel = new Label("Classify Settings");
        classifyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px 0;");

        classifyBox = new VBox(20, classifyLabel, spinnerBox, distancesBox); // Utiliser un espacement de 20
        classifyBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 10; -fx-padding: 20;");
        for (String attribute : this.model.getAttributes()) {
            CheckBox tmp = new CheckBox();
            tmp.setOnMouseClicked(e -> {
                this.myController.setDistance(this.distancesBox.getValue(), getChoosenAttriubutes());
                this.myController.robustessPreview(this.kValueSpinner.getValue(), getChoosenAttriubutes());
                updateKnnRobustness(model.getRobustness());
            });
            tmp.setText(attribute);
            tmp.setStyle("-fx-border-radius: 5; -fx-padding: 10;");
            classifyBox.getChildren().add(tmp);
        }
        classifyBox.getChildren().add(classifyData); // Utiliser un espacement de 20

        // Boîte des contrôles
        this.controlsBox = new HBox(fileChooserButton, newGraphButton, newPointButton, xAxisBox, yAxisBox, helpButton);
        this.controlsBox.setAlignment(Pos.CENTER);
        this.controlsBox.setSpacing(20);
        this.controlsBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-padding: 20px");

        // Ajout d'une VBox pour afficher la robustesse du KNN
        this.knnRobustnessLabel = new Label("KNN Robustness: 0 %");
        this.knnRobustnessLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-padding: 10;");
        VBox knnInfoBox = new VBox(knnRobustnessLabel);
        knnInfoBox.setAlignment(Pos.CENTER);
        knnInfoBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 10; -fx-padding: 10;");

        // Disposition principale
        this.root = new BorderPane();
        this.root.setRight(classifyBox);
        this.root.setCenter(chart);
        this.root.setBottom(controlsBox);
        this.root.setTop(knnInfoBox);

        this.scene = new Scene(root, 1300, 800);
        this.setScene(scene);
        this.setTitle("Scatter Plot Viewer");
        this.show();

        attachEventHandlers();
        this.myController.setDistance(this.distancesBox.getValue(), getChoosenAttriubutes());
        this.drawGraph();
    }

    private void attachEventHandlers() {
        this.fileChooserButton.setOnAction(e -> this.myController.loadNewData());
        this.newPointButton.setOnAction(e -> this.myController.newPointWindow());
        this.newGraphButton.setOnAction(e -> this.myController.newGraphWindow());
        this.distancesBox.setOnAction(e -> {
            this.myController.setDistance(this.distancesBox.getValue(), getChoosenAttriubutes());
            this.myController.robustessPreview(this.kValueSpinner.getValue(), getChoosenAttriubutes());
            updateKnnRobustness(model.getRobustness());
        });
        this.classifyData.setOnAction(e -> {
            this.myController.classifyData(this.kValueSpinner.getValue(), this.getChoosenAttriubutes());
        });

        this.kValueSpinner.setOnMouseClicked(e -> {
            this.myController.robustessPreview(this.kValueSpinner.getValue(), this.getChoosenAttriubutes());
            updateKnnRobustness(model.getRobustness());
        });

        this.xAxisBox.setOnAction(e -> {
            this.changeAxisNames();
            this.drawGraph();
        });
        this.yAxisBox.setOnAction(e -> {
            this.changeAxisNames();
            this.drawGraph();
        });
        this.helpButton.setOnAction(e -> showHelpDialog());
    }

    /**
     * Affiche une boîte de dialogue d'aide avec les fonctionnalités de l'application.
     */
    private void showHelpDialog() {
        Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
        helpAlert.setTitle("Help");
        helpAlert.setHeaderText("Résumé des fonctionnalités");
        helpAlert.setContentText("Voici les principales fonctionnalités de l'application :\n\n" +
                "- 📁 Load Data : Charger des données depuis un fichier.\n" +
                "- 📊 New Graph : Créer un nouveau graphique.\n" +
                "- ➕ Add Point : Ajouter un nouveau point au graphique.\n" +
                "- 🔍 Classify Data : Classifier les données à l'aide du KNN.\n" +
                "- Sélection des axes X et Y à partir des ComboBox.\n\n" +
                "Les infobulles vous guideront également à chaque étape.");
        helpAlert.showAndWait();
    }

    @Override
    public void update() {
        this.drawGraph();
    }

    private void refreshChartSeries() {
        for (String t : this.model.getTypes()) {
            if (!this.serieAlreadyExist(t)) {
                this.chart.getData().add(this.createSerie(t));
            }
        }
    }

    private XYChart.Series<Number, Number> createSerie(String type) {
        XYChart.Series<Number, Number> res = new XYChart.Series<>();
        res.setName(type);
        res.getData().add(new XYChart.Data<>(0, 0));
        return res;
    }


    /**
     * Met à jour le graphique en nuage de points avec les données actuelles.
     */
    public void drawGraph() {
        this.updateKnnRobustness(model.getRobustness());
        refreshChartSeries();
        for (XYChart.Series<Number, Number> series : this.chart.getData()) {
            series.getData().clear();
            for (Data d : this.model) {
                if (d.getType().equals(series.getName())) {
                    XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(d.getAttributeByName(this.xAxis.getLabel()),
                            d.getAttributeByName(this.yAxis.getLabel()));
                    series.getData().add(dataPoint);
                }
            }
        }
        drawTooltip();
    }

    /**
     * Ajoute des infobulles aux points de données dans le graphique.
     *
     * Cette méthode parcourt chaque série de données dans le graphique et crée une
     * infobulle pour chaque point de données. L'infobulle affiche le nom de la série
     * ainsi que les coordonnées X et Y du point. Les infobulles apparaissent lorsque
     * l'utilisateur survole un point de données avec la souris et disparaissent lorsqu'il
     * s'en éloigne.
     */
    private void drawTooltip() {
        for (XYChart.Series<Number, Number> series : this.chart.getData()) {
            for (XYChart.Data<Number, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip(series.getName() + " (" + data.getXValue() + ", " + data.getYValue() + ")");
                Tooltip.install(data.getNode(), tooltip);
                data.getNode().setOnMouseEntered(e -> tooltip.show(data.getNode(), e.getScreenX(), e.getScreenY() + 10));
                data.getNode().setOnMouseExited(e -> tooltip.hide());
            }
        }
    }

    private void changeAxisNames() {
        this.xAxis.setLabel(this.xAxisBox.getSelectionModel().getSelectedItem());
        this.yAxis.setLabel(this.yAxisBox.getSelectionModel().getSelectedItem());
    }

    private boolean serieAlreadyExist(String type) {
        return this.chart.getData().stream().anyMatch(series -> series.getName().equals(type));
    }

    private Button createStyledButton(String text, String tooltipText) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(button, tooltip);
        return button;
    }

    private Button createHelpButton(String text, String tooltipText) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(button, tooltip);
        return button;
    }


    public String updateKnnRobustness(Double robustness) {
        if (robustness == 0 || robustness == null || NoAttributeChoosen()) {
            this.knnRobustnessLabel.setText("KNN Robustness: N/A");
            return "N/A";
        }
        String res = String.valueOf(Math.round(robustness * 100) +" %");
        this.knnRobustnessLabel.setText("KNN Robustness: " + res);
        return res;
    }

    // Fonction pour récupérer les données des champs de texte
    public List<String> getChoosenAttriubutes() {
        List<String> res = new ArrayList<>();
        for (Node n : this.classifyBox.getChildren()) {
            if (n instanceof CheckBox) {
                CheckBox tmp = (CheckBox) n;
                if(tmp.isSelected()) res.add(tmp.getText());
            }
        }
        return res;
    }

    private boolean NoAttributeChoosen(){
        return this.getChoosenAttriubutes().isEmpty();
    }
}
