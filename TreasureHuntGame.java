import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TreasureHuntGame extends JFrame {
    private static final int GRID_SIZE = 5; // Example: 5x5 grid
    private JButton[][] buttons;
    private JLabel levelLabel;
    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JPanel gridPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private int currentLevel;
    private int score;
    private int highScore;
    private boolean[][] treasureMap;
    private boolean gameOver;
    private int treasureX;
    private int treasureY;

    public TreasureHuntGame() {
        super("Treasure Hunt Game");
        currentLevel = 1;
        score = 0;
        highScore = 0;
        initializeUI();
        startLevel(currentLevel);

        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top Panel
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        levelLabel = new JLabel("Level: " + currentLevel);
        scoreLabel = new JLabel("Score: " + score);
        topPanel.add(levelLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Grid)
        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        add(gridPanel, BorderLayout.CENTER);

        // Create grid buttons
        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50, 50)); // Adjust button size
                buttons[i][j].setBackground(Color.LIGHT_GRAY); // Default color
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                gridPanel.add(buttons[i][j]);
            }
        }

        // Bottom Panel
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("Find the treasure!");
        bottomPanel.add(statusLabel);
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        bottomPanel.add(restartButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startLevel(int level) {
        // Initialize game state for the current level
        Random random = new Random();
        treasureMap = new boolean[GRID_SIZE][GRID_SIZE];

        // Place treasure randomly ensuring it's not in the same place as last time
        do {
            treasureX = random.nextInt(GRID_SIZE);
            treasureY = random.nextInt(GRID_SIZE);
        } while (treasureMap[treasureX][treasureY]);

        treasureMap[treasureX][treasureY] = true;
    }

    private void restartGame() {
        // Reset game variables and UI for a new game
        currentLevel = 1;
        score = 0;
        gameOver = false;
        updateUI();
        startLevel(currentLevel);
    }

    private void updateUI() {
        levelLabel.setText("Level: " + currentLevel);
        scoreLabel.setText("Score: " + score);
        statusLabel.setText("Find the treasure!");
        // Reset button colors and text
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int x;
        private int y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                if (treasureMap[x][y]) {
                    buttons[x][y].setBackground(Color.GREEN);
                    buttons[x][y].setText("TREASURE"); // Display text inside button
                    buttons[x][y].setEnabled(false);
                    statusLabel.setText("Congratulations! You found the treasure!");
                    score += 100;
                    currentLevel++;
                    updateHighScore();
                    if (currentLevel <= 12) {
                        updateUI();
                        startLevel(currentLevel);
                    } else {
                        statusLabel.setText("You have completed all levels!");
                        gameOver = true;
                    }
                } else {
                    int distance = Math.abs(x - treasureX) + Math.abs(y - treasureY);
                    buttons[x][y].setBackground(Color.RED);
                    buttons[x][y].setText("Distance: " + distance);
                    buttons[x][y].setEnabled(false);
                    statusLabel.setText("Oops! Try again.");
                    score -= 10; // Penalty for hitting a wrong cell
                    score = Math.max(score, 0); // Score cannot be negative
                    scoreLabel.setText("Score: " + score);
                }
            }
        }
    }

    private void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            JOptionPane.showMessageDialog(this, "New High Score: " + highScore);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TreasureHuntGame();
            }
        });
    }
}
