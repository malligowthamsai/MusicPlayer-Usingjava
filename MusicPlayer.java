import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MusicPlayer {

    static java.util.List<File> songs = new ArrayList<>();
    static int currentIndex = 0;

    static Player player;
    static Thread playThread;

    static boolean isPaused = false;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Music Player");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> songList = new JList<>(listModel);

        JScrollPane scroll = new JScrollPane(songList);

        JPanel controls = new JPanel();

        JButton loadBtn = new JButton("Load Folder");
        JButton playBtn = new JButton("Play");
        JButton nextBtn = new JButton("Next");
        JButton prevBtn = new JButton("Prev");

        controls.add(loadBtn);
        controls.add(playBtn);
        controls.add(prevBtn);
        controls.add(nextBtn);

        frame.add(scroll, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        // Load Songs
        loadBtn.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                File folder = chooser.getSelectedFile();
                songs.clear();
                listModel.clear();

                for (File file : folder.listFiles()) {
                    if (file.getName().endsWith(".mp3")) {
                        songs.add(file);
                        listModel.addElement(file.getName());
                    }
                }
            }
        });

        // Play Selected
        playBtn.addActionListener(e -> {
            currentIndex = songList.getSelectedIndex();
            playSong(songs.get(currentIndex));
        });

        // Next
        nextBtn.addActionListener(e -> {
            currentIndex++;
            if (currentIndex >= songs.size()) currentIndex = 0;
            playSong(songs.get(currentIndex));
        });

        // Previous
        prevBtn.addActionListener(e -> {
            currentIndex--;
            if (currentIndex < 0) currentIndex = songs.size() - 1;
            playSong(songs.get(currentIndex));
        });

        frame.setVisible(true);
    }

    static void playSong(File file) {
        try {
            if (player != null) {
                player.close();
            }

            FileInputStream fis = new FileInputStream(file);
            player = new Player(fis);

            playThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            playThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}