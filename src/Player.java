//person child class
public class Player extends Person{
    private int points;

    public Player(String fName){
        super(fName);
        points = 0;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return points;
    }

    public String toString(){
        return super.toString() + " Ranking points: " + points;
    }
}
