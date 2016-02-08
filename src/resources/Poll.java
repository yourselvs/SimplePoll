package resources;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Poll{
	String question;
	ArrayList<Answer> answers = new ArrayList<Answer>();
	ArrayList<Player> players = new ArrayList<Player>();
	int numAnswers = 0;
	int votes = 0;
	Player creator;
	boolean isPosted = false;
	
	public Poll(String question, Player creator) {
		this.question = question; 
		this.creator = creator;
	}
	
	public void addAnswer(String answer){
		if(answers.size() < 5){
			answers.add(new Answer(answer));
			numAnswers++;
		}
	}
	
	public void post(){isPosted = true;}
	public void vote(int vote, Player player){
		answers.get(vote).vote();
		players.add(player);
		votes++;
	}
	
	public ArrayList<Answer> getAnswers(){return answers;}
	public ArrayList<Player> getPlayers(){return players;}
	public String getQuestion(){return question;}
	public Player getCreator(){return creator;}
	public int getVotes(){return votes;}
	
	public boolean isPosted(){return isPosted;}
}