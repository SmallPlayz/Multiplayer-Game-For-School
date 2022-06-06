package com.smallplayz.clientzero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client0 extends Thread{

    private static Socket clientSocket = null;

    public static PrintStream os = null;

    private static DataInputStream is = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    public static void main(String[] args) {

        Game game = new Game();

        int portNumber = 12345;
        String host = "localhost";

        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
            os.println("Player0");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host " + host);
        }
        if (clientSocket != null && os != null && is != null) {
            try {
                new Thread(new Client0()).start();
                while (!closed) {
                    os.println(inputLine.readLine().trim());
                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
    public void run() {
        String responseLine;
        try {
            while ((responseLine = is.readLine()) != null) {
                System.out.println(responseLine);
                if(responseLine.startsWith("[Player0]")){

                }else{
                    if(responseLine.startsWith("[Player1] : #move")) {
                        Game.player1.setLocation(findMovementCords(responseLine, 0), findMovementCords(responseLine, 1));
                        if(responseLine.charAt(18) == 'W')
                            Game.player1.setIcon(new ImageIcon("images/player1.png"));
                        else if(responseLine.charAt(18) == 'A')
                            Game.player1.setIcon(new ImageIcon("images/player1Left.png"));
                        else if(responseLine.charAt(18) == 'S')
                            Game.player1.setIcon(new ImageIcon("images/player1Down.png"));
                        else if(responseLine.charAt(18) == 'D')
                            Game.player1.setIcon(new ImageIcon("images/player1Right.png"));
                    }
                    else if(responseLine.startsWith("[Player2] : #move")) {
                        Game.player2.setLocation(findMovementCords(responseLine, 0), findMovementCords(responseLine, 1));
                        if(responseLine.charAt(18) == 'W')
                            Game.player2.setIcon(new ImageIcon("images/player2.png"));
                        else if(responseLine.charAt(18) == 'A')
                            Game.player2.setIcon(new ImageIcon("images/player2Left.png"));
                        else if(responseLine.charAt(18) == 'S')
                            Game.player2.setIcon(new ImageIcon("images/player2Down.png"));
                        else if(responseLine.charAt(18) == 'D')
                            Game.player2.setIcon(new ImageIcon("images/player2Right.png"));
                    }
                    else if(responseLine.startsWith("[Player3] : #move")) {
                        Game.player3.setLocation(findMovementCords(responseLine, 0), findMovementCords(responseLine, 1));
                        if(responseLine.charAt(18) == 'W')
                            Game.player3.setIcon(new ImageIcon("images/player3.png"));
                        else if(responseLine.charAt(18) == 'A')
                            Game.player3.setIcon(new ImageIcon("images/player3Left.png"));
                        else if(responseLine.charAt(18) == 'S')
                            Game.player3.setIcon(new ImageIcon("images/player3Down.png"));
                        else if(responseLine.charAt(18) == 'D')
                            Game.player3.setIcon(new ImageIcon("images/player3Right.png"));
                    }
                    else if(responseLine.substring(12, 19).equals("#bullet")){
                        MultiplayerGun thread1 = new MultiplayerGun(responseLine.substring(1, 8), responseLine.charAt(20));
                        thread1.start();
                    }
                }
            }
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
    public static int findMovementCords(String line, int c){
        String result = "";
        int resultInt;
        if(c == 0)
            result = line.substring(19, 35).trim();
        else if(c == 1)
            result = line.substring(35).trim();
        resultInt = Integer.parseInt(result);
        return resultInt;
    }
}

class Game extends Thread{

    static JFrame frame;

    static JLabel player;
    static JLabel player1;
    static JLabel player2;
    static JLabel player3;

    Action upAction;
    Action downAction;
    Action leftAction;
    Action rightAction;
    Action shootAction;
    Action fullscreenAction;

    public static int width = 1280, height = 720;

    public static char playerDir = 'W';
    public static boolean fullScreen = false;

    Game() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.getContentPane().setBackground(new Color(107, 156, 88));
        frame.setLayout(null);

        player = new JLabel(new ImageIcon("images/player.png"));
        player.setBounds(100, 100, 100, 100);
        player.setOpaque(false);

        player1 = new JLabel(new ImageIcon("images/player1.png"));
        player1.setBounds(1100, 100, 100, 100);
        player1.setOpaque(false);

        player2 = new JLabel(new ImageIcon("images/player2.png"));
        player2.setBounds(100, 500, 100, 100);
        player2.setOpaque(false);

        player3 = new JLabel(new ImageIcon("images/player3.png"));
        player3.setBounds(1100, 500, 100, 100);
        player3.setOpaque(false);

        upAction = new UpAction();
        downAction = new DownAction();
        leftAction = new LeftAction();
        rightAction = new RightAction();
        shootAction = new ShootAction();
        fullscreenAction = new FullScreenAction();

        player.getInputMap().put(KeyStroke.getKeyStroke('w'), "upAction");
        player.getActionMap().put("upAction", upAction);
        player.getInputMap().put(KeyStroke.getKeyStroke('s'), "downAction");
        player.getActionMap().put("downAction", downAction);
        player.getInputMap().put(KeyStroke.getKeyStroke('a'), "leftAction");
        player.getActionMap().put("leftAction", leftAction);
        player.getInputMap().put(KeyStroke.getKeyStroke('d'), "rightAction");
        player.getActionMap().put("rightAction", rightAction);
        player.getInputMap().put(KeyStroke.getKeyStroke('e'), "shootAction");
        player.getActionMap().put("shootAction", shootAction);
        player.getInputMap().put(KeyStroke.getKeyStroke('f'), "fullscreenAction");
        player.getActionMap().put("fullscreenAction", fullscreenAction);

        frame.add(player);
        frame.add(player1);
        frame.add(player2);
        frame.add(player3);

        //Monster monsterThread = new Monster();
        //monsterThread.start();

        frame.setVisible(true);
    }
    public static void wait(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static class UpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX(), player.getY() - 10);
            player.setIcon(new ImageIcon("images/player.png"));
            playerDir = 'W';
            Client0.os.println("#move " + playerDir + "       " + player.getX() + "       " + player.getY());
        }
    }

    public static class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX(), player.getY() + 10);
            player.setIcon(new ImageIcon("images/playerDown.png"));
            playerDir = 'S';
            Client0.os.println("#move " + playerDir + "       " + player.getX() + "       " + player.getY());
        }
    }

    public static class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX() - 10, player.getY());
            player.setIcon(new ImageIcon("images/playerLeft.png"));
            playerDir = 'A';
            Client0.os.println("#move " + playerDir + "       " + player.getX() + "       " + player.getY());
        }
    }

    public static class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX() + 10, player.getY());
            player.setIcon(new ImageIcon("images/playerRight.png"));
            playerDir = 'D';
            Client0.os.println("#move " + playerDir + "       " + player.getX() + "       " + player.getY());
        }
    }

    public static class ShootAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            Gun gunthread = new Gun();
            gunthread.start();
        }
    }

    public static class FullScreenAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!fullScreen) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.repaint();
                fullScreen = true;
            } else if(fullScreen) {
                frame.setSize(width, height);
                frame.repaint();
                fullScreen = false;
            }
        }
    }
}

class Gun extends Thread{

    JLabel bullet;

    public void run() {
        
        bullet = new JLabel(new ImageIcon("images/bullet.png"));
        bullet.setBounds(50, 50, 15, 15);
        bullet.setLocation(Game.player.getX()+40, Game.player.getY()+40);
        Game.frame.add(bullet);
        Client0.os.println("#bullet " + Game.playerDir + "       " + bullet.getX() + "       " + bullet.getY());

        if(Game.playerDir == 'W') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX(), bullet.getY() - 1);
                Game.wait(2);
            }
        }
        else if(Game.playerDir == 'A') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX() - 1, bullet.getY());
                Game.wait(2);
            }
        }
        else if(Game.playerDir == 'S') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX(), bullet.getY() + 1);
                Game.wait(2);
            }
        }
        else if(Game.playerDir == 'D') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX() + 1, bullet.getY());
                Game.wait(2);
            }
        }
    }
}
class Monster extends Thread{

    JLabel monster;

    public void run() {
        monster = new JLabel(new ImageIcon("images/monster.png"));
        monster.setBounds(500, 500, 100, 100);
        monster.setOpaque(false);
        Game.frame.add(monster);

        try{
            while(true) {
                if(monster.getX() < Game.player.getX())
                    monster.setLocation(monster.getX()+1, monster.getY());
                else if(monster.getX() > Game.player.getX())
                    monster.setLocation(monster.getX()-1, monster.getY());
                if(monster.getY() < Game.player.getY())
                    monster.setLocation(monster.getX(), monster.getY()+1);
                else if(monster.getY() > Game.player.getY())
                    monster.setLocation(monster.getX(), monster.getY()-1);
                Game.wait(8);
                if((int) (Math.random() * 4) == 0)
                    Game.wait((int) (Math.random() * 250));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MultiplayerGun extends Thread{

    JLabel bullet;
    private static String MultiplayerPlayer = "";
    private static char MultiplayerDir = ' ';

    MultiplayerGun(String player, char dir){
        MultiplayerPlayer = player;
        MultiplayerDir = dir;
    }

    public void run() {

        bullet = new JLabel(new ImageIcon("images/bullet.png"));
        bullet.setBounds(50, 50, 15, 15);
        if(MultiplayerPlayer.equals("Player1"))
            bullet.setLocation(Game.player1.getX()+40, Game.player1.getY()+40);
        else if(MultiplayerPlayer.equals("Player2"))
            bullet.setLocation(Game.player2.getX()+40, Game.player2.getY()+40);
        else if(MultiplayerPlayer.equals("Player3"))
            bullet.setLocation(Game.player3.getX()+40, Game.player3.getY()+40);
        Game.frame.add(bullet);

        if(MultiplayerDir == 'W') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX(), bullet.getY() - 1);
                Game.wait(2);
            }
        }
        else if(MultiplayerDir == 'A') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX() - 1, bullet.getY());
                Game.wait(2);
            }
        }
        else if(MultiplayerDir == 'S') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX(), bullet.getY() + 1);
                Game.wait(2);
            }
        }
        else if(MultiplayerDir == 'D') {
            for (int i = 0; i < 3000; i++) {
                bullet.setLocation(bullet.getX() + 1, bullet.getY());
                Game.wait(2);
            }
        }
    }
}