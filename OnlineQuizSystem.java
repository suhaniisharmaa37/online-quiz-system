import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OnlineQuizSystem extends JFrame implements ActionListener {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int timeLeft = 300; // 5 minutes in seconds
    private Timer timer;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton submitButton;
    private JLabel timerLabel;
    private JLabel scoreLabel;

    public OnlineQuizSystem() {
        setTitle("Online Quiz System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeQuestions();

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout());
        timerLabel = new JLabel("Time Left: 05:00");
        timerPanel.add(timerLabel);
        add(timerPanel, BorderLayout.NORTH);

        // Score panel
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scoreLabel = new JLabel("Score: 0");
        scorePanel.add(scoreLabel);
        add(scorePanel, BorderLayout.SOUTH);

        // Question panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel();
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            buttonGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
        }

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        questionPanel.add(submitButton);

        add(questionPanel, BorderLayout.CENTER);

        loadQuestion();

        // Start timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                updateTimerLabel();
                if (timeLeft <= 0) {
                    timer.stop();
                    showResult();
                }
            }
        });
        timer.start();

        setVisible(true);
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?",
                new String[]{"Paris", "London", "Berlin", "Madrid"}, 0));
        questions.add(new Question("Which planet is known as the Red Planet?",
                new String[]{"Venus", "Mars", "Jupiter", "Saturn"}, 1));
        questions.add(new Question("What is 2 + 2?",
                new String[]{"3", "4", "5", "6"}, 1));
        questions.add(new Question("Who wrote 'To Kill a Mockingbird'?",
                new String[]{"Harper Lee", "J.K. Rowling", "Stephen King", "Mark Twain"}, 0));
        questions.add(new Question("What is the largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("<html><b>Question " + (currentQuestionIndex + 1) + ":</b> " + q.getQuestion() + "</html>");
            String[] options = q.getOptions();
            for (int i = 0; i < options.length; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setSelected(false);
            }
        } else {
            showResult();
        }
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));
    }

    private void showResult() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Quiz Over!\nYour Score: " + score + "/" + questions.size(),
                "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            int selected = -1;
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    selected = i;
                    break;
                }
            }
            if (selected == questions.get(currentQuestionIndex).getCorrectIndex()) {
                score++;
                scoreLabel.setText("Score: " + score);
            }
            currentQuestionIndex++;
            loadQuestion();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineQuizSystem());
    }
}

class Question {
    private String question;
    private String[] options;
    private int correctIndex;

    public Question(String question, String[] options, int correctIndex) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}