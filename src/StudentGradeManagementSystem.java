import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class StudentGradeManagementSystem extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField englishField;
    private JTextField mathField;
    private JTextField cLanguageField;
    private JTextField javaField;
    private JTextField searchField;

    private JButton addButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton calculateAverageButton;
    private JButton calculateStandardDeviationButton;
    private JButton calculateIndividualAverageButton;
    private JButton calculateIndividualStandardDeviationButton;
    private JButton modifyButton;
    private JButton searchButton;
    private JButton calculateCoefficientOfVariationButton;

    private JLabel resultLabel;
    private JLabel individualResultLabel;
    private JLabel individualStandardDeviationLabel;


    public StudentGradeManagementSystem() throws IOException {
        initializeUI();
        loadData();
    }


    private void initializeUI() throws IOException {
        // 窗口设计
        setTitle("学生成绩管理系统");
        setSize(1200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建表格模型和表格
        String[] columnNames = {"学号", "姓名", "英语", "高数", "C语言", "Java程序设计"};
        tableModel = new DefaultTableModel(null, columnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        // 输入信息部分
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        add(formPanel, BorderLayout.EAST);
        formPanel.add(new JLabel("学号 : "));
        studentIdField = new JTextField();
        formPanel.add(studentIdField);
        formPanel.add(new JLabel("姓名 : "));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("英语 : "));
        englishField = new JTextField();
        formPanel.add(englishField);
        formPanel.add(new JLabel("高数 : "));
        mathField = new JTextField();
        formPanel.add(mathField);
        formPanel.add(new JLabel("C语言 : "));
        cLanguageField = new JTextField();
        formPanel.add(cLanguageField);
        formPanel.add(new JLabel("Java程序设计 : "));
        javaField = new JTextField();
        formPanel.add(javaField);
        formPanel.add(new JLabel("查找 : "));
        searchField = new JTextField();
        formPanel.add(searchField);
        // Buttons
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        addButton = new JButton("添加");
        addButton.addActionListener(new AddButtonListener());
        buttonPanel.add(addButton);
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(new DeleteButtonListener());
        buttonPanel.add(deleteButton);
        saveButton = new JButton("保存");
        saveButton.addActionListener(new SaveButtonListener());
        buttonPanel.add(saveButton);
        calculateAverageButton = new JButton("计算平均成绩");
        calculateAverageButton.addActionListener(new CalculateAverageListener());
        buttonPanel.add(calculateAverageButton);
        calculateStandardDeviationButton = new JButton("计算标准差");
        calculateStandardDeviationButton.addActionListener(new CalculateStandardDeviationListener());
        buttonPanel.add(calculateStandardDeviationButton);
        calculateIndividualAverageButton = new JButton("计算单个学生平均成绩");
        calculateIndividualAverageButton.addActionListener(new CalculateIndividualAverageListener());
        buttonPanel.add(calculateIndividualAverageButton);
        calculateIndividualStandardDeviationButton = new JButton("计算单个学生标准差");
        calculateIndividualStandardDeviationButton.addActionListener(new CalculateIndividualStandardDeviationListener());
        buttonPanel.add(calculateIndividualStandardDeviationButton);
        modifyButton = new JButton("修改");
        modifyButton.addActionListener(new ModifyButtonListener());
        buttonPanel.add(modifyButton);
        searchButton = new JButton("查找");
        searchButton.addActionListener(new SearchButtonListener());
        buttonPanel.add(searchButton);
        calculateCoefficientOfVariationButton = new JButton("计算变异系数");
        calculateCoefficientOfVariationButton.addActionListener(new CalculateCoefficientOfVariationListener());
        buttonPanel.add(calculateCoefficientOfVariationButton);
        // 显示计算结果
        individualStandardDeviationLabel = new JLabel();
        buttonPanel.add(individualStandardDeviationLabel);


        individualResultLabel = new JLabel();
        buttonPanel.add(individualResultLabel);

        resultLabel = new JLabel();
        buttonPanel.add(resultLabel);
    }


    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String studentId = studentIdField.getText();
            String name = nameField.getText();
            String english = englishField.getText();
            String math = mathField.getText();
            String cLanguage = cLanguageField.getText();
            String javaProg = javaField.getText();
            if (!studentId.isEmpty() &&!name.isEmpty() &&!english.isEmpty() &&!math.isEmpty() &&!cLanguage.isEmpty() &&!javaProg.isEmpty()) {
                Object[] row = {studentId, name, english, math, cLanguage, javaProg};
                tableModel.addRow(row);

                studentIdField.setText("");
                nameField.setText("");
                englishField.setText("");
                mathField.setText("");
                cLanguageField.setText("");
                javaField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "请填写所有字段");
            }
        }
    }


    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(null, "确定要删除这些行数据吗？", "删除确认", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        tableModel.removeRow(selectedRows[i]);
                    }
                }
            }
        }
    }


    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine())!= null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Object[] row = new Object[6];
                    for (int i = 0; i < parts.length; i++) {
                        row[i] = parts[i];
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            // 文件不存在或读取错误，不做任何处理，继续程序
        }
    }


    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 更新选中行的数据
                tableModel.setValueAt(studentIdField.getText(), selectedRow, 0);
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(englishField.getText(), selectedRow, 2);
                tableModel.setValueAt(mathField.getText(), selectedRow, 3);
                tableModel.setValueAt(cLanguageField.getText(), selectedRow, 4);
                tableModel.setValueAt(javaField.getText(), selectedRow, 5);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt"))) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        sb.append(tableModel.getValueAt(i, j));
                        if (j < tableModel.getColumnCount() - 1) {
                            sb.append(",");
                        }
                    }
                    writer.write(sb.toString());
                    writer.newLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CalculateAverageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double average = calculateAverage();
            resultLabel.setText("平均成绩: " + average);
        }
    }


    private double calculateAverage() {
        double sum = 0.0;
        int count = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double[] grades = getGradesFromRow(i);
            if (grades!= null) {
                sum += (grades[0] + grades[1] + grades[2] + grades[3]);
                count++;
            }
        }
        return count > 0? sum / count : 0.0;
    }


    private class CalculateStandardDeviationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double stdDev = calculateStandardDeviation();
            resultLabel.setText("标准差: " + String.format("%.2f", stdDev));
        }
    }


    private double calculateStandardDeviation() {
        double sum = 0.0;
        int count = 0;
        List<Double> averages = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double[] grades = getGradesFromRow(i);
            if (grades!= null) {
                double average = (grades[0] + grades[1] + grades[2] + grades[3]) ;
                sum += (grades[0] + grades[1] + grades[2] + grades[3]);
                averages.add(average);
                count++;
            }
        }
        double average = count > 0? sum / (count) : 0.0;
        double varianceSum = 0.0;
        for (int i = 0; i < averages.size(); i++) {
            varianceSum += Math.pow(averages.get(i) - average, 2);
        }
        double variance = count > 0? varianceSum / count : 0.0;
        return Math.sqrt(variance);
    }


    private class CalculateIndividualAverageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                double average = calculateIndividualAverage(selectedRow);
                //individualResultLabel.setText("单个学生平均成绩: " + average);
                resultLabel.setText("单个学生平均成绩: " + average);
            } else {
                individualResultLabel.setText("请先选择一个学生");
            }
        }
    }


    private double calculateIndividualAverage(int rowIndex) {
        double[] grades = getGradesFromRow(rowIndex);
        if (grades!= null) {
            return (grades[0] + grades[1] + grades[2] + grades[3]) / 4;
        }
        return 0.0;
    }


    private class CalculateIndividualStandardDeviationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                double standardDeviation = calculateIndividualStandardDeviation(selectedRow);
                //individualStandardDeviationLabel.setText("单个学生标准差: " +  String.format("%.2f", standardDeviation));
                resultLabel.setText("单个学生标准差: " +  String.format("%.2f", standardDeviation));
            } else {
                individualStandardDeviationLabel.setText("请先选择一个学生");
            }
        }
    }


    private double calculateIndividualStandardDeviation(int rowIndex) {
        double[] grades = getGradesFromRow(rowIndex);
        if (grades!= null) {
            double individualAverage = (grades[0] + grades[1] + grades[2] + grades[3]) / 4;
            double varianceSum = Math.pow(grades[0] - individualAverage, 2) +
                    Math.pow(grades[1] - individualAverage, 2) +
                    Math.pow(grades[2] - individualAverage, 2) +
                    Math.pow(grades[3] - individualAverage, 2);
            double variance = varianceSum / 4;
            return Math.sqrt(variance);
        }
        return 0.0;
    }


    private double[] getGradesFromRow(int rowIndex) {
        try {
            double english = Double.parseDouble(tableModel.getValueAt(rowIndex, 2).toString());
            double math = Double.parseDouble(tableModel.getValueAt(rowIndex, 3).toString());
            double cLanguage = Double.parseDouble(tableModel.getValueAt(rowIndex, 4).toString());
            double javaProg = Double.parseDouble(tableModel.getValueAt(rowIndex, 5).toString());
            return new double[]{english, math, cLanguage, javaProg};
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "成绩数据格式错误，请确保所有成绩都是数字");
            return null;
        }
    }

    private class ModifyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 将选中行的数据填充到输入框中
                studentIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                englishField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                mathField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                cLanguageField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                javaField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            } else {
                JOptionPane.showMessageDialog(null, "请先选择要修改的行");
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyword = searchField.getText().toLowerCase();
            if (!keyword.isEmpty()) {
                int foundRow = -1;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String studentId = tableModel.getValueAt(i, 0).toString().toLowerCase();
                    String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
                    if (studentId.contains(keyword) || name.contains(keyword)) {
                        foundRow = i;
                        break;
                    }
                }
                table.clearSelection();
                if (foundRow!= -1) {
                    table.setRowSelectionInterval(foundRow, foundRow);
                } else {
                    JOptionPane.showMessageDialog(null, "未找到匹配的行");
                }
            } else {
                JOptionPane.showMessageDialog(null, "请输入查找关键字");
            }
        }
    }

    private class CalculateCoefficientOfVariationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double cv = calculateCoefficientOfVariation();
            resultLabel.setText("变异系数: " +String.format("%.2f", cv));
        }
    }


    private double calculateCoefficientOfVariation() {
        double average = calculateAverage();
        double stdDev = calculateStandardDeviation();
        if (average == 0) {
            JOptionPane.showMessageDialog(null, "平均成绩为 0，无法计算变异系数");
            return 0;
        }
        return stdDev / average;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            StudentGradeManagementSystem frame = null;
            try {
                frame = new StudentGradeManagementSystem();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.setVisible(true);
        });
    }


}