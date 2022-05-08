package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML   ImageView takePhoto;
    @FXML   AnchorPane anchorPane;
    @FXML   Pane optionPanel;
    @FXML   TextField SendersEmail;
    @FXML   PasswordField pass;
    @FXML   TextField emailSubject;
    @FXML   TextArea emailText;
    @FXML   TextField recipientsEmail;
    @FXML   TextField filePath;
    @FXML   ImageView pdfIcon;
    @FXML   ImageView ball;
    @FXML   Pane ballPanel;
    @FXML   Slider ballSize;
    @FXML   Slider ballSpeed;
    @FXML   Slider ballOpacity;
    @FXML   Button runBall;
    @FXML   ImageView logo;
    @FXML   ImageView arrows;
    @FXML   ImageView closeButton;
    @FXML   ImageView emailBtn;
    @FXML   ImageView photo;
    @FXML   VBox capturedPane;

    static int dx = 1;
    static int dy = 1;
    File file;

    private Stage stage = new Stage();
    private Scene scene;
    private boolean isOptionPaneShowed = false;
    private TranslateTransition translateTransition;
    private TranslateTransition translateTransitionBallPanel;
    private Mail mail = new Mail();

    private boolean isBallRun = false;
    private boolean isBallPanelShowed = false;
    private Timeline tl = new Timeline();

    private Duration duration = Duration.millis(10);
    private Double sliderValue;
    private boolean isFirst = true;
    private String gmail= "@gmail.com";

    //Sizing
    private double sizeTakePhoto;
    private double logosize;
    private double arrowSize;

        @FXML
    public void closeButton() {
        stage= (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.out.println("Closed");
    }


    public void captured() throws InterruptedException, AWTException, IOException {
        scene = takePhoto.getScene();
        scene.getWindow().hide();

        FXMLLoader  loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/sample.fxml"));
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        BufferedImage capture = new Robot().createScreenCapture(screenRect);

        Rectangle crop = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);

        capture = capture.getSubimage(0, 30, crop.width-80, crop.height-80);

        File outputfile = new File("SELFIE.png");
        ImageIO.write(capture, "png", outputfile);
        mail.setPhoto(outputfile);
        System.out.println("Captured");
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        capturedPane.setVisible(true);
        capturedPane.setStyle("-fx-background-color: rgba(0,0,0,0.75)");
        logo.setVisible(false);
        takePhoto.setVisible(false);
        arrows.setVisible(false);

        photo.setFitHeight(800);
        photo.setFitWidth(800);
        photo.setImage(SwingFXUtils.toFXImage(capture, null));
        photo.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(206,206,206,0.75), 50, 0.1, 0.0, 0.0);");
    }

    public void hideCapturedPhoto(){
        capturedPane.setVisible(false);
        logo.setVisible(true);
        takePhoto.setVisible(true);
        arrows.setVisible(true);
    }

    @FXML
    public void openOptionPanel(){
        if (!isOptionPaneShowed){
            emailBtn.setImage(new Image(new File("src/img/email-opened-w.png").toURI().toString()));

            translateTransition.setDuration(Duration.millis(500));
            translateTransition.setToX(200);
            isOptionPaneShowed = true;

        } else{
            emailBtn.setImage(new Image(new File("src/img/email.png").toURI().toString()));
            translateTransition.setToX(-200);
            isOptionPaneShowed = false;
            emailBtn.setVisible(true);

        }
        translateTransition.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            optionPanel.setStyle("-fx-background-color: rgba(26,22,26,0.65)");
            ballPanel.setStyle("-fx-background-color: rgba(255,255,255,0.77)");
            translateTransition = new TranslateTransition();
            translateTransitionBallPanel = new TranslateTransition();
            translateTransitionBallPanel.setDuration(Duration.millis(500));
            translateTransition.setNode(optionPanel);
            translateTransitionBallPanel.setNode(ballPanel);
            ball.setVisible(false);
            ChangeListener changeListener = new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    double size = ballSize.getValue() *5 +1;
                    ball.setFitWidth(size);
                    ball.setFitHeight(size);
                    ball.setOpacity(ballOpacity.getValue()/100+0.01);
                }
            };

             ballSize.valueProperty().addListener(changeListener);
             ballOpacity.valueProperty().addListener(changeListener);

             ballSpeed.valueProperty().addListener(new ChangeListener<Number>() {
                 @Override
                 public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                     calcDuration(ballSpeed.getValue());
                     addBouncyBall(1);
                 }
             });
    }



    public Duration calcDuration(Double sl){
        duration = new Duration((50 * (1 - (sl/100)))+1);
        return duration;
    }

    public void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        file = fileChooser.showOpenDialog(null);
        filePath.setText(file.getName());
        pdfIcon.setImage(new Image(new File("/img/pdf-ico-ok.png").toURI().toString()));
        System.out.println("file was choosed");
        Image image = new Image(new File("src/img/pdf-ico.png").toURI().toString());
        pdfIcon.setImage(image);
    }

    public void addBouncyBall(int i) {
        if (isBallRun){
            toMoveBall();
        }
    }

    public void addBouncyBall() {
        if (isBallRun){
            runBall.setText("Запустити");
            isBallRun = false;
            ball.setVisible(false);
        }else if (!isBallRun) {
            isBallRun = true;
            ball.setVisible(true);
            runBall.setText("Зупинити");
            toMoveBall();
        }
    }

    public void toMoveBall(){
        tl.getKeyFrames().clear();
        tl.stop();
        tl.setCycleCount(Animation.INDEFINITE);
        scene = takePhoto.getScene();
        KeyFrame moveBall = new KeyFrame(duration,
                (ActionEvent event) -> {
                    double xMin = ball.getBoundsInParent().getMinX();
                    double yMin = ball.getBoundsInParent().getMinY();
                    double xMax = ball.getBoundsInParent().getMaxX();
                    double yMax = ball.getBoundsInParent().getMaxY();
                    // Collision - boundaries
                    if (xMin < 0 || xMax > scene.getWidth()) {
                        dx = dx * -1;
                    }
                    if (yMin < 0 || yMax > scene.getHeight()) {
                        dy = dy * -1;
                    }
                    ball.setTranslateX(ball.getTranslateX() + dx);
                    ball.setTranslateY(ball.getTranslateY() + dy);
                });

        tl.getKeyFrames().add(moveBall);
        tl.play();
    }

    public void openBallPanel(){
        if (!isBallPanelShowed){
            ballPanel.setVisible(true);
            translateTransitionBallPanel.setToY(-250);
            //ball.setVisible(true);
            isBallPanelShowed = true;
        } else  {
            translateTransitionBallPanel.setToY(250);
            isBallPanelShowed = false;
        }
        translateTransitionBallPanel.play();
    }


    public void saveMailParameters(){

            if (!SendersEmail.getText().toLowerCase().contains(gmail.toLowerCase())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "допустимо використовувати лише пошту GMAIL");
                alert.show();
            }else{
                if (isValidEmailAddress(SendersEmail.getText())) {
                    mail.setSender(SendersEmail.getText());
                    mail.setPassword(pass.getText());
                    mail.setSubject(emailSubject.getText());
                    mail.setText(emailText.getText());
                    mail.setFile(file);
                    openOptionPanel();
                    System.out.println(mail.toString());
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "введена адреса не дійсна, перевірте ще раз");
                    alert.show();
                }
            }

        }



    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void sendEmail() throws Exception {
        saveMailParameters();
        mail.setRecipient(recipientsEmail.getText());
        mail.send();
        System.out.println("sent");
    }
}
