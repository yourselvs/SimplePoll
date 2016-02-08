package resources;

public class Answer{
	String description;
	int votes = 0;
	
	public Answer(String description){this.description = description;}
	
	public void vote(){votes++;}
	
	public String getDescription(){return description;}
	public int getVotes(){return votes;}
}