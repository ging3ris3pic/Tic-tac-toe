//Custom exception per project requirements
public class MoreThan2PlayersException extends Exception{

    public MoreThan2PlayersException(){
    }

    @Override
    public String getMessage(){
        return "User attempted to add more than 2 players";
    }
}
