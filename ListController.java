package view;
/*
 * Created By:
 * Rohan Joshi: rj408
 * Nicholas Cheniara: njc129
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import Models.Song;
import Models.CustomComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.nio.charset.Charset;


public class ListController {
	
	private boolean isEdit = false;
	private boolean isAdd = false;

	@FXML         
	ListView<Song> listView;
	
	@FXML         
	TextArea textA;
	
	@FXML         
	Button detailbutton,addbutton,deletebutton,editbutton,submitbutton,cancelbutton;  
	
	@FXML         
	TextField songfield,artistfield,yearfield,albumfield;  

	private ObservableList<Song> obsList;
	
	Map<String, String> uniquecheck  = new HashMap<String, String>();

	public void start(Stage mainStage){   
		textA.setEditable(false);
		detailbutton.setVisible(false);
		
		obsList = FXCollections.observableArrayList(readSong("testfile.txt"));
		FXCollections.sort(obsList, new CustomComparator());
		refreshmap();
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("testfile.txt"));
					for(Song s : obsList) {
						writer.write(s.getName()+"|"+s.getArtist()+"|"+s.getAlbum()+"|"+s.getYear()+"\n");
					}
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}));
		
		listView.setItems(obsList); 
		showfields(false);
		
		
		
		listView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {
            @Override
            public ListCell<Song> call(ListView<Song> param) {
                ListCell<Song> cell = new ListCell<Song>() {
                    @Override
                    protected void updateItem(Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            setText(item.getName());
                        }
                        else {
                            setText("");
                        }
                    }

                };

                return cell;
            }
        });
		

		// select the first item
		listView.getSelectionModel().select(0);
		if(obsList.size()>0) {
			showItem(mainStage);
		}
		
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				showItem(mainStage));
		
		
		detailbutton.setOnAction((event) -> {
            showDetails(mainStage);
        });
		

		addbutton.setOnAction((event) -> {
			showfields(true);
			isAdd = true;
        });
		
		deletebutton.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Cancel Action");
			alert.setHeaderText("Cancel Action");
			alert.setContentText("Are you sure would like to delete the song?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				deleteSong(mainStage);
			}
        });
		
		editbutton.setOnAction((event) -> {
            showfields(true);
            isEdit = true;
        });
		
		submitbutton.setOnAction((event) -> {
            if((isEdit == true) && (isAdd == false)) {
            	editSong(mainStage);
            }
            if((isEdit == false) && (isAdd == true)) {
            	addSong(mainStage);
            }
        });
		
		cancelbutton.setOnAction((event) -> {
            cancel(mainStage);
        });

	}
	
	private void showItem(Stage mainStage) {
		if(listView.getSelectionModel().getSelectedItem()!=null) {
			String content = "Name: "+listView.getSelectionModel().getSelectedItem().getName() + 
					"\nAlbum: " + listView.getSelectionModel().getSelectedItem().getAlbum()+
					"\nArtist: " + listView.getSelectionModel().getSelectedItem().getArtist()+
					"\nYear: " + listView.getSelectionModel().getSelectedItem().getYear();
			textA.setText(content);
		}
	}

	
	private void cancel(Stage mainStage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Cancel Action");
		alert.setHeaderText("Cancel Action");
		if(isAdd == true) {
			alert.setContentText("Are you sure would like to cancel action: Add? (Press OK to cancel the action, press Cancel to continue the action)");
		}
		if(isEdit == true) {
			alert.setContentText("Are you sure would like to cancel action: Edit? (Press OK to cancel the action, press Cancel to continue the action)");
		}
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			isAdd = false;
			isEdit = false;
			clearfields();
			showfields(false);
		}
	}
	private void showDetails(Stage mainStage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Song Details");
		alert.setHeaderText(
				"Selected song details");
		

		String content = "Name: "+listView.getSelectionModel().getSelectedItem().getName() + 
				"\nAlbum: " + listView.getSelectionModel().getSelectedItem().getAlbum()+
				"\nArtist: " + listView.getSelectionModel().getSelectedItem().getArtist()+
				"\nYear: " + listView.getSelectionModel().getSelectedItem().getYear();
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void clearfields() {
		songfield.clear();
		albumfield.clear();
		artistfield.clear();
		yearfield.clear();
	}
	
	private void addSong(Stage mainStage) {
		String songtext = songfield.getText().trim();
		String albumtext = albumfield.getText().trim();
		String artistext = artistfield.getText().trim();
		String yeartext = yearfield.getText().trim();

		if(songtext.equals("")|artistext.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
			alert.setTitle("Could not add!");
			alert.setContentText("Oops! You left a field blank. Please fix it.");
			alert.setHeaderText("ERROR: EMPTY FIELD");
			alert.showAndWait();
			return;
			
		}
		
		if(songtext.equals("|")|albumtext.equals("|")|artistext.equals("|")|yeartext.equals("|")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
			alert.setTitle("Could not add!");
			alert.setContentText("Oops! You cannot use the '|' character. Please fix it.");
			alert.setHeaderText("ERROR: Illegal Character");
			alert.showAndWait();
			return;
			
		}
		
		 if(!isPrintable(songtext)|!isPrintable(artistext)|!isPrintable(albumtext)|!isPrintable(yeartext)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
			alert.setTitle("Could not add!");
			alert.setContentText("Oops! You cannot use a non printable character!. Please fix it.");
			alert.setHeaderText("ERROR: Illegal Character");
			alert.showAndWait();
			return;
		}
		
		if(uniquecheck.containsKey(songtext+"|"+artistext)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainStage);
			alert.setTitle("Could not add!");
			alert.setContentText("Oops! This song/artist combination already exists! Please try again.");
			alert.setHeaderText("ERROR: DUPLICATE");
			alert.showAndWait();
			clearfields();
			return;
		}
		
		if(!yeartext.equals("")){
			try{
				  int num = Integer.parseInt(yeartext);
				  if(num<0) {
					  Alert alert = new Alert(AlertType.WARNING);
					  alert.initOwner(mainStage);
					  alert.setTitle("Could not add!");
					  alert.setContentText("Oops! That year is negative and does not exist!");
					  alert.setHeaderText("ERROR: Incorrect Year Format");
					  alert.showAndWait();
					  yearfield.clear();
					  return;
				  }
				} catch (NumberFormatException e) {
					  Alert alert = new Alert(AlertType.WARNING);
					  alert.initOwner(mainStage);
					  alert.setTitle("Could not add!");
					  alert.setContentText("Oops! That year is not an integer!");
					  alert.setHeaderText("ERROR: Incorrect Year Format");
					  alert.showAndWait();
					  yearfield.clear();
					  return;
				}
		}
		
		Song newsong = new Song(songtext,artistext,albumtext,yeartext);
		obsList.add(newsong);
		clearfields();
		isAdd = false;
		showfields(false);
		FXCollections.sort(obsList, new CustomComparator());
		refreshmap();
		int index = obsList.indexOf(newsong);
		listView.getSelectionModel().select(index);
	}
	
	 private void deleteSong(Stage mainStage) {
		 int index = listView.getSelectionModel().getSelectedIndex();
		 obsList.remove(index);
		 FXCollections.sort(obsList, new CustomComparator());
		 refreshmap();
		 listView.refresh();
		 
		 if(obsList.size()==0) {
			 textA.clear();
		 }
		 
	 }

	 private void editSong(Stage mainStage) {
		 int index = listView.getSelectionModel().getSelectedIndex();
		 Song cursong = listView.getSelectionModel().getSelectedItem();
		 String songtext = songfield.getText().trim();
		 String albumtext = albumfield.getText().trim();
		 String artistext = artistfield.getText().trim();
		 String yeartext = yearfield.getText().trim();
		 
		 if(songtext.equals("|")|albumtext.equals("|")|artistext.equals("|")|yeartext.equals("|")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Could not add!");
				alert.setContentText("Oops! You cannot use the '|' character. Please fix it.");
				alert.setHeaderText("ERROR: Illegal Character");
				alert.showAndWait();
				return;
				
			}
		 
		 if(uniquecheck.containsKey(songtext+"|"+artistext)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainStage);
				alert.setTitle("Could not add!");
				alert.setContentText("Oops! This song/artist combination already exists! Please try again.");
				alert.setHeaderText("ERROR: DUPLICATE");
				alert.showAndWait();
				clearfields();
				return;
			}
		 
		 if(!isPrintable(songtext)|!isPrintable(artistext)|!isPrintable(albumtext)|!isPrintable(yeartext)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainStage);
				alert.setTitle("Could not add!");
				alert.setContentText("Oops! You cannot use a non printable character!. Please fix it.");
				alert.setHeaderText("ERROR: Illegal Character");
				alert.showAndWait();
				return;
			}
		 
		 if(!yeartext.equals("")){
				try{
					  int num = Integer.parseInt(yeartext);
					  if(num<0) {
						  Alert alert = new Alert(AlertType.WARNING);
						  alert.initOwner(mainStage);
						  alert.setTitle("Could not add!");
						  alert.setContentText("Oops! That year is negative and does not exist!");
						  alert.setHeaderText("ERROR: Incorrect Year Format");
						  alert.showAndWait();
						  yearfield.clear();
						  return;
					  }
					} catch (NumberFormatException e) {
						  Alert alert = new Alert(AlertType.WARNING);
						  alert.initOwner(mainStage);
						  alert.setTitle("Could not add!");
						  alert.setContentText("Oops! That year is not an integer!");
						  alert.setHeaderText("ERROR: Incorrect Year Format");
						  alert.showAndWait();
						  yearfield.clear();
						  return;
					}
			}
		 
		 if(songtext.equals("")) {
			 songtext = cursong.getName();
		 }
		 if(artistext.equals("")) {
			 artistext = cursong.getArtist();
		 }
		 if(albumtext.equals("")) {
			 albumtext = cursong.getAlbum();
		 }
		 if(yeartext.equals("")) {
			 yeartext = cursong.getYear();
		 }
		 
		 Song newsong = new Song(songtext,albumtext,artistext,yeartext);
		 obsList.remove(index);
		 obsList.add(newsong);
		 clearfields();
		 isEdit = false;
		 showfields(false);
		 FXCollections.sort(obsList, new CustomComparator());
		 refreshmap();
		 listView.refresh();
		 int index1 = obsList.indexOf(newsong);
		 listView.getSelectionModel().select(index1);
	 }
	 
	 public void showfields(boolean setter) {
		 songfield.setVisible(setter);
		 albumfield.setVisible(setter);
		 artistfield.setVisible(setter);
		 yearfield.setVisible(setter);
		 submitbutton.setVisible(setter);
		 cancelbutton.setVisible(setter);
	 }

	 private void refreshmap() {
		 uniquecheck.clear();
		 for(Song s : obsList) {
				uniquecheck.put(s.getName().toLowerCase()+"|"+s.getArtist().toLowerCase(),"");
			}
	 }
	 
	 private static ArrayList<Song> readSong(String pathname){
		 ArrayList <Song> songs = new ArrayList<Song>();
		 String[] details =null;
		 try (BufferedReader br = new BufferedReader(new FileReader("testfile.txt"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       details = line.split("[|]");
			       if(details.length==2) {
			    	   songs.add(new Song(details[0],details[1],"",""));
			       }
			       else if(details.length==3) {
			    	   songs.add(new Song(details[0],details[1],details[2],""));
			       }
			       else {
			    	   songs.add(new Song(details[0],details[1],details[2],details[3]));
			       }
			    }
			    
			} catch (Exception e) {
				e.printStackTrace();
			} 
		 
		 
		 return songs;
	 }
	 
	 public static boolean isPrintable(String s) {
		    return Charset.forName("US-ASCII").newEncoder().canEncode(s);
		  }
	 
	 

}
