import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class TicTacToe implements ActionListener{
    Random rand = new Random();
    JFrame frame = new JFrame();//JFrame for project requirements
    JPanel winner = new JPanel();//JPanel for project requirements
    JPanel buttonPanel = new JPanel();
    JPanel playerPanel = new JPanel();
    JLabel players = new JLabel();//JLabel for project requirements
    JLabel textField = new JLabel();
    JButton[] buttons = new JButton[9];//JButton for project requirements
    JButton playAgain = new JButton("New game");
    JMenuBar mainMenu = new JMenuBar();
    JMenuItem addPlayer = new JMenuItem("Add Player");
    JMenuItem startGame = new JMenuItem("Start Game");
    LinkedList<Player> currentPlayers = new LinkedList<>();//LinkedList for project requirements
    String name;
    Player person;//child class extends parents class
    Referee ref = new Referee("Jim");//child class extends parents class
    Points points = new Points();//implements the interface award
    ConsolationPrize prize = new ConsolationPrize();//implements the interface award
    File file;
    boolean playerTurn;
    boolean xWinner = false;
    boolean oWinner = false;
    int turnCount = 0;
    int xCount = 0;
    int oCount = 0;

    //method to call all the Swing component settings
    public TicTacToe(){
        frameSettings();
        textFieldSettings();
        playersSettings();
        winnerSettings();
        playerPanelSettings();
        buttonPanelSettings();
        setStartGame();
        playAgainSettings();
        mainMenuSettings();
        setAddPlayer();
    }

    @Override//Override for project requirements
    public void actionPerformed(ActionEvent e){
        for(int i = 0; i < 9; i++){
            if(e.getSource() == buttons[i]){
                if(playerTurn){
                    if(Objects.equals(buttons[i].getText(), "")){
                        buttons[i].setText("X");
                        playerTurn = false;
                        if(currentPlayers.size() < 2){
                            textField.setText("O Turn");
                        } else {
                            textField.setText(currentPlayers.get(1).getFirstName() + "'s Turn (O)");
                        }
                        turnCount++;
                        checkWinner();
                    }
                } else {
                    if(Objects.equals(buttons[i].getText(), "")){
                        buttons[i].setText("O");
                        playerTurn = true;
                        if(currentPlayers.size() < 2){
                            textField.setText("X Turn");
                        } else {
                            textField.setText(currentPlayers.get(0).getFirstName() + "'s Turn (X)");
                        }
                        turnCount++;
                        checkWinner();
                        gameDraw();
                    }
                }
            }
        }
    }

    //method to set JFrame settings
    public void frameSettings(){
        frame.addWindowListener(new WindowAdapter(){//creates score.txt if this file does not exist
            @Override
            public void windowOpened(WindowEvent e){
                try{
                    file = new File("src//Score//score.txt");
                    if(! file.exists()){
                        file.createNewFile();
                    }
                } catch(IOException io){
                    System.err.println("New file created");
                }
            }

            @Override
            public void windowClosing(WindowEvent e){//writes to the file per project requirements
                if(currentPlayers.size() == 2){
                    try{
                        FileOutputStream out = new FileOutputStream(file);
                        String user = currentPlayers.get(0) + "\n" + currentPlayers.get(1);
                        char[] ch = user.toCharArray();
                        for(char c : ch){
                            out.write(c);
                        }
                        out.close();
                    } catch(IOException io){
                        System.err.println("Mistakes were made");
                    }
                }
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setLayout(new BorderLayout());//layout manager per project requirements
        frame.setVisible(true);
        frame.add(winner, BorderLayout.NORTH);
        frame.add(playerPanel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setJMenuBar(mainMenu);
    }

    public void textFieldSettings(){
        textField.setBackground(Color.darkGray);
        textField.setForeground(Color.pink);
        textField.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic Tac Toe");
        textField.add(playAgain, BorderLayout.WEST);
    }

    public void playersSettings(){
        players.setBackground(Color.darkGray);
        players.setForeground(Color.pink);
        players.setFont(new Font(Font.SERIF, Font.PLAIN, 25));
        players.setHorizontalAlignment(JLabel.CENTER);
        players.setText("Current players");
        players.setOpaque(true);
    }

    public void winnerSettings(){
        winner.setLayout(new BorderLayout());
        winner.setBounds(0, 0, 800, 100);
        winner.setBackground(Color.darkGray);
        winner.add(textField);
    }

    public void playerPanelSettings(){
        playerPanel.setLayout(new BorderLayout());
        playerPanel.setBounds(0, 650, 800, 100);
        playerPanel.add(players);
    }

    public void mainMenuSettings(){
        mainMenu.add(addPlayer);
        mainMenu.add(startGame);
    }

    public void setAddPlayer(){
        addPlayer.addActionListener(e -> {
            try{
                if(currentPlayers.size() < 2){
                    name = JOptionPane.showInputDialog("Enter your first name");
                    person = new Player(name);
                    currentPlayers.add(person);
                    players.setText(String.valueOf(currentPlayers));
                } else {
                    throw new MoreThan2PlayersException();//throws the custom exception per project reqs
                }
            } catch(MoreThan2PlayersException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
    }

    public void setStartGame(){
        startGame.addActionListener(e -> firstTurn());
    }

    public void buttonPanelSettings(){
        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.setBackground(Color.BLACK);
        for(int i = 0; i < 9; i++){
            buttons[i] = new JButton();
            buttons[i].setBackground(Color.pink);
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(new Font(Font.SERIF, Font.BOLD, 80));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }
    }

    public void playAgainSettings(){
        playAgain.setEnabled(true);
        playAgain.setSize(100, 50);
        playAgain.addActionListener(ActionListener -> {
            xWinner = false;
            oWinner = false;
            textField.setText("Tic Tac Toe");
            gameOver();
        });
    }

    //determines first turn based on a 50/50 chance and updates initial text to reflect
    public void firstTurn(){
        if(rand.nextInt(2) == 0){
            playerTurn = true;
            if(currentPlayers.size() < 2){
                textField.setText("X Turn");
            } else {
                textField.setText(currentPlayers.get(0).getFirstName() + "'s Turn (X)");
            }
        } else {
            playerTurn = false;
            if(currentPlayers.size() < 2){
                textField.setText("O Turn");
            } else {
                textField.setText(currentPlayers.get(1).getFirstName() + "'s Turn (O)");
            }
        }
    }

    //check winning combinations for X and O
    public void checkWinner(){
        if((Objects.equals(buttons[0].getText(), "X")) &&
                (Objects.equals(buttons[1].getText(), "X")) &&
                (Objects.equals(buttons[2].getText(), "X"))
        ){
            xWin(0, 1, 2);
        } else if((Objects.equals(buttons[3].getText(), "X")) &&
                (Objects.equals(buttons[4].getText(), "X")) &&
                (Objects.equals(buttons[5].getText(), "X"))
        ){
            xWin(3, 4, 5);
        } else if((Objects.equals(buttons[6].getText(), "X")) &&
                (Objects.equals(buttons[7].getText(), "X")) &&
                (Objects.equals(buttons[8].getText(), "X"))
        ){
            xWin(6, 7, 8);
        } else if((Objects.equals(buttons[0].getText(), "X")) &&
                (Objects.equals(buttons[3].getText(), "X")) &&
                (Objects.equals(buttons[6].getText(), "X"))
        ){
            xWin(0, 3, 6);
        } else if((Objects.equals(buttons[1].getText(), "X")) &&
                (Objects.equals(buttons[4].getText(), "X")) &&
                (Objects.equals(buttons[7].getText(), "X"))
        ){
            xWin(1, 4, 7);
        } else if((Objects.equals(buttons[2].getText(), "X")) &&
                (Objects.equals(buttons[5].getText(), "X")) &&
                (Objects.equals(buttons[8].getText(), "X"))
        ){
            xWin(2, 5, 8);
        } else if((Objects.equals(buttons[0].getText(), "X")) &&
                (Objects.equals(buttons[4].getText(), "X")) &&
                (Objects.equals(buttons[8].getText(), "X"))
        ){
            xWin(0, 4, 8);
        } else if((Objects.equals(buttons[2].getText(), "X")) &&
                (Objects.equals(buttons[4].getText(), "X")) &&
                (Objects.equals(buttons[6].getText(), "X"))
        ){
            xWin(2, 4, 6);
        } else if((Objects.equals(buttons[0].getText(), "O")) &&
                (Objects.equals(buttons[1].getText(), "O")) &&
                (Objects.equals(buttons[2].getText(), "O"))
        ){
            oWin(0, 1, 2);
        } else if((Objects.equals(buttons[3].getText(), "O")) &&
                (Objects.equals(buttons[4].getText(), "O")) &&
                (Objects.equals(buttons[5].getText(), "O"))
        ){
            oWin(3, 4, 5);
        } else if((Objects.equals(buttons[6].getText(), "O")) &&
                (Objects.equals(buttons[7].getText(), "O")) &&
                (Objects.equals(buttons[8].getText(), "O"))
        ){
            oWin(6, 7, 8);
        } else if((Objects.equals(buttons[0].getText(), "O")) &&
                (Objects.equals(buttons[3].getText(), "O")) &&
                (Objects.equals(buttons[6].getText(), "O"))
        ){
            oWin(0, 3, 6);
        } else if((Objects.equals(buttons[1].getText(), "O")) &&
                (Objects.equals(buttons[4].getText(), "O")) &&
                (Objects.equals(buttons[7].getText(), "O"))
        ){
            oWin(1, 4, 7);
        } else if((Objects.equals(buttons[2].getText(), "O")) &&
                (Objects.equals(buttons[5].getText(), "O")) &&
                (Objects.equals(buttons[8].getText(), "O"))
        ){
            oWin(2, 5, 8);
        } else if((Objects.equals(buttons[0].getText(), "O")) &&
                (Objects.equals(buttons[4].getText(), "O")) &&
                (Objects.equals(buttons[8].getText(), "O"))
        ){
            oWin(0, 4, 8);
        } else if((Objects.equals(buttons[2].getText(), "O")) &&
                (Objects.equals(buttons[4].getText(), "O")) &&
                (Objects.equals(buttons[6].getText(), "O"))
        ){
            oWin(2, 4, 6);
        }
    }

    //highlights winning button combination for X, updates points, and changes the text field to reflect winner
    public void xWin(int a, int b, int c){
        buttons[a].setBackground(Color.cyan);
        buttons[b].setBackground(Color.cyan);
        buttons[c].setBackground(Color.cyan);
        xWinner = true;
        if(currentPlayers.size() < 2){
            textField.setText(ref.refRuling(xWinner, oWinner));
        } else {
            int currentPoints = currentPlayers.get(0).getPoints();
            textField.setText(ref.refRuling(currentPlayers.get(0), xWinner, oWinner));
            currentPoints += points.updatePoints(currentPlayers.get(0), true);
            currentPlayers.get(0).setPoints(currentPoints);
            players.setText(String.valueOf(currentPlayers));
            checkPoints();
        }
        for(int i = 0; i < 9; i++){
            buttons[i].setEnabled(false);
        }

    }

    //highlights winning button combination for O, updates points, and changes the text field to reflect winner
    public void oWin(int a, int b, int c){
        buttons[a].setBackground(Color.cyan);
        buttons[b].setBackground(Color.cyan);
        buttons[c].setBackground(Color.cyan);
        oWinner = true;
        if(currentPlayers.size() < 2){
            textField.setText(ref.refRuling(xWinner, oWinner));
        } else {
            int currentPoints = currentPlayers.get(1).getPoints();
            textField.setText(ref.refRuling(currentPlayers.get(1), xWinner, oWinner));
            currentPoints += points.updatePoints(currentPlayers.get(1), true);
            currentPlayers.get(1).setPoints(currentPoints);
            players.setText(String.valueOf(currentPlayers));
            checkPoints();
        }
        for(int i = 0; i < 9; i++){
            buttons[i].setEnabled(false);
        }
    }

    //checks if the game is a draw
    public void gameDraw(){
        if((! xWinner || ! oWinner) && turnCount == 9){
            textField.setText(ref.refRuling(xWinner, oWinner));
        }
    }


    public void gameOver(){
        turnCount = 0;
        resetButtons();
        checkWinner();
        gameDraw();
        for(int i = 0; i < 9; i++){
            buttons[i].setEnabled(true);
            buttons[i].setBackground(Color.pink);
        }
    }

    //resets JButton array text
    public void resetButtons(){
        for(JButton button : buttons){
            button.setText("");
        }
    }

    //checks if the player has reached the required points for the consolation prize
    public void checkPoints(){
        if(currentPlayers.get(0).getPoints() == 100 && xCount < 1){
            prize.updatePoints(currentPlayers.get(0), true);
            xCount++;
        } else if(currentPlayers.get(1).getPoints() == 100 && oCount < 1){
            prize.updatePoints(currentPlayers.get(1), true);
            oCount++;
        }
    }
}
