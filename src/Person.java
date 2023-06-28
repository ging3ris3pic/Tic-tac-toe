//parents class for Referee and Player
public class Person{
    private String firstName;

    //overloaded constructor
    public Person(String fName){
        firstName = fName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String toString(){
        return firstName;
    }
}
