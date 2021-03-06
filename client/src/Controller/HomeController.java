package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import Interfaces.Controller;
import Models.Appointment;
import Models.Invitation;
import Models.Notification;
import Models.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController implements Initializable, Controller {
	@FXML
	private AnchorPane root;
	@FXML
	private Label dateDisplay;
	@FXML
	private VBox notifications;
	@FXML
	private ListView<String> appointments;
	@FXML
	private VBox requests;
	@FXML
	private Button createEvent;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setTime();
		update();

		
		
	}

	@FXML
	private void onCreate(ActionEvent event) throws Exception{
		this.showEditAppointmentDialog();
	}

	private Controller showEditAppointmentDialog(  )throws Exception{

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/EditAppointment.fxml"));
		Parent root = fxmlLoader.load();
		EditAppointmentController editAppointmentController = fxmlLoader.getController();
		editAppointmentController.setMainController(this);
		editAppointmentController.setData(new Appointment());
		Stage stage = new Stage();
		stage.setTitle("New Appointment");
		stage.setScene(new Scene(root));
		stage.show();
		return editAppointmentController;
	}
	
	private void setTime(){
		//set date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		dateDisplay.setText(sdf.format(date));
	}

	private void fillNotifications(ArrayList<Notification> notificationsList) {
		//notifications.setOrientation(Orientation.VERTICAL);
		
		// list for controllers
				ArrayList<NotificationController> notificationControllers = new ArrayList<NotificationController>();

				for (Notification notification : notificationsList) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
								"../View/Notification.fxml"));
						Parent root = fxmlLoader.load();
						NotificationController notificationController = fxmlLoader
								.getController();
						notificationController.setData(notification, this);

						notificationControllers.add(notificationController);

						notifications.getChildren().addAll(root);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}


	}

	private void fillAppointments(ArrayList<String> appointmentList) {
		// fill the appointment of the day list
		ObservableList obListTime = FXCollections.observableList(appointmentList);
		appointments.getItems().clear();
		appointments.setItems(obListTime);
	}



	private void fillRequests(ArrayList<Invitation> requestList) {
		
		// list for controllers
		ArrayList<RequestController> requestControllers = new ArrayList<RequestController>();
		requests.getChildren().removeAll(requests.getChildren());
		for (Invitation request : requestList) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
						"../View/Request.fxml"));
				Parent root = fxmlLoader.load();
				RequestController requestController = fxmlLoader
						.getController();
				requestController.setData(request);
				requestController.setParentController(this);

				requestControllers.add(requestController);
				requests.getChildren().addAll(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update() {

		// ask server for appointments
		ArrayList<String> appointmentList = new ArrayList<String>();
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		appointments = Appointment.getAppointmentsByCalendar(new GregorianCalendar());
		// list with appointments
		for(Appointment appointment : appointments){
			appointmentList.add(appointment.toString());
		}
		fillAppointments(appointmentList);

		ArrayList<Invitation> invitations = Invitation.getInvitationsByUserId(MainController.getCurrentUser().getId());

		// list with requests
		ArrayList<Invitation> invitationList = new ArrayList<Invitation>();
		for(Invitation invitation : invitations){
			invitationList.add(invitation);
			
		}

		fillRequests(invitationList);

		/*notifications.getChildren().removeAll(notifications.getChildren());

		ArrayList<Notification> notificationsList = new ArrayList<Notification>();
		notificationsList = Notification.getNotificationsByUserId(MainController.getCurrentUser().getId());
		fillNotifications(notificationsList);*/
	}
}
