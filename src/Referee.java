//person child class
public class Referee extends Person{

    public Referee(String fName){
        super(fName);
    }

    public String refRuling(boolean xWins, boolean oWins){
        if(xWins){
            return this + ": X is the winner!";
        }else if(oWins){
            return this + ": O is the winner!";
        }else{
            return this + ": Its a draw!!!";
        }
    }

    public String refRuling(Player player, boolean xWins, boolean oWins){
        if(xWins){
            return this + ": " + player.getFirstName() + " is the winner";
        }else if(oWins){
            return this + ": " + player.getFirstName() + " is the winner";
        }else{
            return this + ": Its a draw!!!";
        }
    }

    public String toString(){
        return super.toString();
    }

}
