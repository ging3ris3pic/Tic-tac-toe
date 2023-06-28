//class implements the award interface to update points
public class Points implements Award{

    public int updatePoints(Player player, Boolean wonGame){
        int winAmount = 50;
        int pointsWon = player.getPoints() + winAmount;
        player.setPoints(pointsWon);
        return winAmount;
    }
}
