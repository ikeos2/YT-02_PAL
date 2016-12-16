package note;

public class Note {
	String owner; //owner's username
	String collab; //String of collaborators UIDs
	String data; //whatever data needs to be stored
	String title; //title of note
	int UID; //noteUID
	
	public Note(String gtitle, String gOwner, String collabs, String gData, int id){
		title = gtitle;
		owner = gOwner;
		collab = collabs;
		data = gData;
		UID = id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCollab() {
		return collab;
	}
	public void setCollab(String collab) {
		this.collab = collab;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void appendData(String data){
		this.data += data;
	}
	public int getUID() {
		return UID;
	}
}
