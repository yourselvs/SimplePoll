package me.yourselvs;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import resources.Poll;

public class MainClass extends JavaPlugin {
	
	String prefix = "[" + ChatColor.GREEN + ChatColor.BOLD + "SP" + ChatColor.RESET + "]";
	String[] info = {prefix + "SimplePoll plugin v1.0", prefix + "Created by yourselvs", prefix + "Type \"/poll help\" to view Simple Poll commands."};
	
	Poll poll;
	Poll previousPoll;

	@Override
	public void onEnable() {
		getLogger().info("SimplePoll successfully enabled.");
	}
	
	@Override
	public void onDisable() {
		poll = null;
		getLogger().info("SimplePoll successfully disbled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(label.equalsIgnoreCase("poll") && sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0)
				player.sendMessage(info);
			else {
				String subcmd = args[0];
				if(subcmd.equalsIgnoreCase("create")) 
					processCreate(args, player);
				else if(subcmd.equalsIgnoreCase("add"))
					processAdd(args, player);
				else if(subcmd.equalsIgnoreCase("view"))
					processView(player);
				else if(subcmd.equalsIgnoreCase("post"))
					processPost(player);
				else if(subcmd.equalsIgnoreCase("end"))
					processEnd(player);
				else if(subcmd.equalsIgnoreCase("vote"))
					processVote(args, player);
				else if(subcmd.equalsIgnoreCase("help"))
					processHelp(player);
				else if(subcmd.equalsIgnoreCase("result"))
					processResult(player);
				else
					processError(player);
			}
			
			return true;
		}
		
		return false;	
		
	}
	
	private void processError(Player player) {
		sendMessage(player, "Unknown command. Type \"/poll help\" to see a command list.");
	}

	private void processResult(Player player) {
		boolean hasVotes = false;
		if(previousPoll.getVotes() > 0)
			hasVotes = true;
		sendMessage(player, "Poll created by " + this.previousPoll.getCreator().getDisplayName());
		if(!hasVotes)
			sendMessage(player, "NOTE: Nobody voted in this poll.");
		sendMessage(player, "Q: " + this.previousPoll.getQuestion());
		for(int i = 0; i < this.previousPoll.getAnswers().size(); i++){
			switch(i){
			case 0: sendMessage(player, "A: " + this.previousPoll.getAnswers().get(i).getDescription());
					if(hasVotes) sendMessage(player, "     " + this.previousPoll.getAnswers().get(i).getVotes() + " votes, " + getPercent(this.previousPoll.getAnswers().get(i).getVotes(),this.previousPoll.getVotes()) + "%");
					break;
			case 1: sendMessage(player, "B: " + this.previousPoll.getAnswers().get(i).getDescription()); 
					if(hasVotes) sendMessage(player, "     " + this.previousPoll.getAnswers().get(i).getVotes() + " votes, " + getPercent(this.previousPoll.getAnswers().get(i).getVotes(),this.previousPoll.getVotes()) + "%");
					break;
			case 2: sendMessage(player, "C: " + this.previousPoll.getAnswers().get(i).getDescription()); 
					if(hasVotes) sendMessage(player, "     " + this.previousPoll.getAnswers().get(i).getVotes() + " votes, " + getPercent(this.previousPoll.getAnswers().get(i).getVotes(),this.previousPoll.getVotes()) + "%");
					break;	
			case 3: sendMessage(player, "D: " + this.previousPoll.getAnswers().get(i).getDescription()); 
					if(hasVotes) sendMessage(player, "     " + this.previousPoll.getAnswers().get(i).getVotes() + " votes, " + getPercent(this.previousPoll.getAnswers().get(i).getVotes(),this.previousPoll.getVotes()) + "%");
					break;
			case 4: sendMessage(player, "E: " + this.previousPoll.getAnswers().get(i).getDescription()); 
					if(hasVotes) sendMessage(player, "     " + this.previousPoll.getAnswers().get(i).getVotes() + " votes, " + getPercent(this.previousPoll.getAnswers().get(i).getVotes(),this.previousPoll.getVotes()) + "%");
					break;
			}
		}
	}

	private void processHelp(Player player) {
		sendMessage(player, ChatColor.AQUA + "/poll" + ChatColor.RESET + " Views information about the Simple Poll plugin");
		if(player.isOp()){
			sendMessage(player, ChatColor.AQUA + "/poll create <question>" + ChatColor.RESET + " Begins creation of a new poll");
			sendMessage(player, ChatColor.AQUA + "/poll add <answer>" + ChatColor.RESET + " Adds an answer to a poll being created");
			sendMessage(player, ChatColor.AQUA + "/poll post" + ChatColor.RESET + " Posts the created poll to all members");
			sendMessage(player, ChatColor.AQUA + "/poll end" + ChatColor.RESET + " Deletes the current poll and gives results to all online OPs");
		}
		sendMessage(player, ChatColor.AQUA + "/poll view" + ChatColor.RESET + " Views the current poll");
		sendMessage(player, ChatColor.AQUA + "/poll vote  <answer>" + ChatColor.RESET + " Answer the current poll with the letter of the response");
		sendMessage(player, ChatColor.AQUA + "/poll help" + ChatColor.RESET + " Views poll plugin commands");
	}

	private void processVote(String[] args, Player player) {
		if(poll == null)
			sendMessage(player, "No poll exists to answer.");
		else if(!poll.isPosted()){
			if(player.isOp())
				sendMessage(player, "You cannot vote in the poll until it is posted.");
			else
				sendMessage(player, "No poll exists to answer.");
		}
		else if(containsPlayer(player))
			sendMessage(player, "You have already voted in this poll.");
		else if(args.length > 2 || args[1].length() > 1)
			sendMessage(player, "Please only include the letter of your answer. (A, B, C, D , E)");
		else{
			boolean valid = false;
			for(int i = 0; i < this.poll.getAnswers().size(); i++){
				switch(i){
				case 0: if(args[1].equalsIgnoreCase("a")) {poll.vote(i, player); valid = true;} break;
				case 1: if(args[1].equalsIgnoreCase("b")) {poll.vote(i, player); valid = true;} break;
				case 2: if(args[1].equalsIgnoreCase("c")) {poll.vote(i, player); valid = true;} break;	
				case 3: if(args[1].equalsIgnoreCase("d")) {poll.vote(i, player); valid = true;} break;
				case 4: if(args[1].equalsIgnoreCase("e")) {poll.vote(i, player); valid = true;} break;
				}
			}
			if(valid)
				sendMessage(player, "Vote cast: \"" + args[1].toUpperCase() + "\"");
			else
				sendMessage(player, "Invalid vote: \"" + args[1].toUpperCase() + "\"");
		}
	}

	private void processEnd(Player player) {
		if(poll.isPosted())
			for(Player notifyPlayer : Bukkit.getServer().getOnlinePlayers())
				if(notifyPlayer.isOp() && !notifyPlayer.getName().equals(poll.getCreator().getName()))
					sendMessage(notifyPlayer, "A poll created by " + poll.getCreator().getDisplayName() + " has been ended. Type \"/poll result\" to see results.");
		previousPoll = poll;
		poll = null;
		sendMessage(player, "Poll successfully ended.");
	}

	private void processPost(Player player) {
		if(!player.isOp())
			sendMessage(player, "You don't have permission to do this.");
		else if(poll == null)
			sendMessage(player, "A poll does not exist to post.");
		else if(poll.getAnswers().size() < 2)
			sendMessage(player, "You may not post a poll with less than two possible answers.");
		else if(!player.getName().equals(poll.getCreator().getName()))
			sendMessage(player, "You may not post the poll because you are not the creator. The poll is being created by " + poll.getCreator().getDisplayName() + ".");
		else{
			poll.post();
			sendMessage(player, "Poll succesfully posted.");
			for(Player notifyPlayer : Bukkit.getServer().getOnlinePlayers())
				sendMessage(notifyPlayer, "A new poll has been posted. Type \"/poll view\" to view.");
		}
	}

	private void processView(Player player) {
		if(poll == null || (!poll.isPosted() && !player.isOp()))
			sendMessage(player, "No poll exists to view.");
		else{
			sendMessage(player, "Poll created by " + this.poll.getCreator().getDisplayName());
			if(!this.poll.isPosted())
				sendMessage(player, "NOTE: This poll is not public yet");
			sendMessage(player, "Q: " + this.poll.getQuestion());
			for(int i = 0; i < this.poll.getAnswers().size(); i++){
				switch(i){
				case 0: sendMessage(player, "A: " + this.poll.getAnswers().get(i).getDescription()); break;
				case 1: sendMessage(player, "B: " + this.poll.getAnswers().get(i).getDescription()); break;
				case 2: sendMessage(player, "C: " + this.poll.getAnswers().get(i).getDescription()); break;
				case 3: sendMessage(player, "D: " + this.poll.getAnswers().get(i).getDescription()); break;
				case 4: sendMessage(player, "E: " + this.poll.getAnswers().get(i).getDescription()); break;
				}
			}
			if(this.poll.getAnswers().size() > 0)
			for(Player players : this.poll.getPlayers()){
				if(player.getName().equalsIgnoreCase(players.getName()))
					sendMessage(player, "You have already voted on this poll.");
			}
		}
	}

	private void processAdd(String[] args, Player player) {
		if(!player.isOp())
			sendMessage(player, "You don't have permission to do this.");
		else if(poll == null)
			sendMessage(player, "A poll does not exist to add an answer to.");
		else if(poll.isPosted())
			sendMessage(player, "You may not add answers to a poll that has been posted.");
		else if(!player.getName().equals(poll.getCreator().getName()))
			sendMessage(player, "You may not add an answer to the poll because you are not the creator. The poll is being created by " + poll.getCreator().getDisplayName() + ".");
		else if(poll.getAnswers().size() >= 5)
			sendMessage(player, "You have reached the maximum of 5 answers.");
		else if(args.length == 1)
			sendMessage(player, "You must include an answer.");
		else{
			String answer = "";
			for(int i = 1; i < args.length; i++){
				answer = answer + args[i] + " ";
			}
			poll.addAnswer(answer);
			sendMessage(player, "Answer added: \"" + answer.trim() + "\"");
		}
	}

	private void processCreate(String[] args, Player player) {
		if(!player.isOp())
			sendMessage(player, "You don't have permission to do this.");
		else if(poll != null){
			if(player.getName().equalsIgnoreCase(this.poll.getCreator().getName()))
				sendMessage(player, "You are already creating a poll.");
			else
				sendMessage(player, "A poll already exists or is being created by " + poll.getCreator().getDisplayName() + ". You cannot create a poll at this time.");
		}
		else if(args.length == 1)
			sendMessage(player, "You must include a question.");
		else{
			String question = "";
			for(int i = 1; i < args.length; i++){
				question = question + args[i] + " ";
			}
			poll = new Poll(question, player);
			sendMessage(player, "Poll created: \"" + question.trim() + "\"");
		}
	}
	
	private boolean containsPlayer(Player player){
		for(Player voter : poll.getPlayers())
			if(voter.getName().equals(player.getName()))
				return true;
		return false;
	}
	
	private String getPercent(int num1, int num2){
		double d = (num1/num2) * 100;
        DecimalFormat df = new DecimalFormat("###.#");
       return df.format(d);
	}
	
	private void sendMessage(Player player, String line){
		player.sendMessage(prefix + " " + line);
	}
}