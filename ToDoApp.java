import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXDatePicker;

public class ToDoApp extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField taskField;
    private JXDatePicker datePicker;
    private JComboBox<String> priorityCombo, categoryCombo;
    private JList<String> taskList;
    private DefaultListModel<String> taskModel;
    private ArrayList<Task> tasks;

    private class Task {
        String description, category, dueDate;
        String priority;

        Task(String description, String category, String dueDate, String priority) {
            this.description = description;
            this.category = category;
            this.dueDate = dueDate;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return String.format("%s [Priority: %s, Due: %s, Category: %s]", description, priority, dueDate, category);
        }
    }

    public ToDoApp() {
        tasks = new ArrayList<>();
        taskModel = new DefaultListModel<>();

        setTitle("Pro To-Do List App");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("todo-icon.png").getImage());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        taskField = new JTextField(20);
        taskField.setFont(new Font("Arial", Font.PLAIN, 14));
        taskField.setToolTipText("Enter your task here");
        inputPanel.add(new JLabel("Task:"), gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 4;
        inputPanel.add(taskField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        datePicker = new JXDatePicker();
        datePicker.setFont(new Font("Arial", Font.PLAIN, 14));
        datePicker.setToolTipText("Select due date");
        datePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
        inputPanel.add(new JLabel("Due Date:"), gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 4;
        inputPanel.add(datePicker, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        inputPanel.add(new JLabel("Priority:"), gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(priorityCombo, gbc);

        gbc.gridx = 4; gbc.gridy = 2; gbc.gridwidth = 2;
        categoryCombo = new JComboBox<>(new String[]{"Work", "Personal", "Study"});
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        inputPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 6; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(categoryCombo, gbc);

        gbc.gridx = 8; gbc.gridy = 0; gbc.gridwidth = 2; gbc.gridy = 3; gbc.gridwidth = 1;
        JButton addButton = new JButton("Add Task");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setBackground(new Color(46, 139, 87));
        addButton.setForeground(Color.WHITE);
        inputPanel.add(addButton, gbc);

        taskList = new JList<>(taskModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 14));
        taskList.setBackground(new Color(255, 250, 240));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(450, 400));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        JButton completeButton = new JButton("Mark Complete");
        completeButton.setFont(new Font("Arial", Font.BOLD, 12));
        completeButton.setBackground(new Color(255, 165, 0));
        completeButton.setForeground(Color.WHITE);
        JButton clearButton = new JButton("Clear All");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setBackground(new Color(105, 105, 105));
        clearButton.setForeground(Color.WHITE);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(clearButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskField.getText().trim();
                String dueDate = datePicker.getDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(datePicker.getDate()) : "";
                String priority = (String) priorityCombo.getSelectedItem();
                String category = (String) categoryCombo.getSelectedItem();
                if (!task.isEmpty() && !dueDate.isEmpty()) {
                    tasks.add(new Task(task, category, dueDate, priority));
                    updateTaskList();
                    taskField.setText("");
                    datePicker.setDate(null);
                } else {
                    JOptionPane.showMessageDialog(ToDoApp.this, "Please enter a valid task and select a date!", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    tasks.remove(selectedIndex);
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(ToDoApp.this, "Please select a task to delete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    tasks.get(selectedIndex).description += " (Completed)";
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(ToDoApp.this, "Please select a task to mark complete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tasks.clear();
                updateTaskList();
            }
        });

        // Add main panel to frame
        add(mainPanel);
    }

    private void updateTaskList() {
        taskModel.clear();
        for (Task task : tasks) {
            taskModel.addElement(task.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDoApp app = new ToDoApp();
            app.setVisible(true);
        });
    }
}