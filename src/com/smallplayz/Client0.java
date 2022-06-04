package com.smallplayz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Client0 {

    public static void main(String[] args) {
        Game game = new Game();
    }
}

class Game extends Thread{

    static JFrame frame;

    static JLabel player;

    Action upAction;
    Action downAction;
    Action leftAction;
    Action rightAction;
    Action shootAction;
    Action fullscreenAction;

    public static int width = 600, height = 600;

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

        Monster monsterThread = new Monster();
        monsterThread.start();

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
        }
    }

    public static class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX(), player.getY() + 10);
            player.setIcon(new ImageIcon("images/playerDown.png"));
            playerDir = 'S';
        }
    }

    public static class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX() - 10, player.getY());
            player.setIcon(new ImageIcon("images/playerLeft.png"));
            playerDir = 'A';
        }
    }

    public static class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setLocation(player.getX() + 10, player.getY());
            player.setIcon(new ImageIcon("images/playerRight.png"));
            playerDir = 'D';
        }
    }

    public static class ShootAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            Gun thread = new Gun();
            thread.start();
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

        if(Game.playerDir == 'W')
            for(int i = 0; i<3000; i++){
                bullet.setLocation(bullet.getX(), bullet.getY()-1);
                Game.wait(2);
            }
        else if(Game.playerDir == 'A')
            for(int i = 0; i<3000; i++){
                bullet.setLocation(bullet.getX()-1, bullet.getY());
                Game.wait(2);
            }
        else if(Game.playerDir == 'S')
            for(int i = 0; i<3000; i++){
                bullet.setLocation(bullet.getX(), bullet.getY()+1);
                Game.wait(2);
            }
        else if(Game.playerDir == 'D')
            for(int i = 0; i<3000; i++){
                bullet.setLocation(bullet.getX()+1, bullet.getY());
                Game.wait(2);
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