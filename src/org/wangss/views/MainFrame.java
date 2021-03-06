package org.wangss.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.Color.lightGray;

/**
 * Created by wangss on 2016/12/13.
 */
public class MainFrame {
    private static JTextField ponidText = new JTextField(15);
    private static JTextField tenRs = new JTextField(20);
    private static JTextField rs = new JTextField(20);
    private static JTextField hexRs = new JTextField(20);
    private static boolean isPon = true;
    private static JPanel hexResult = new JPanel();
    private static JPanel tenResult = new JPanel();
    private static JPanel result = new JPanel();
    private static Button reverseBtn = new Button();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(320, 400);
        frame.setLocation(300, 300);
        frame.setTitle("二进制ifIndex");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FlowLayout flow = new FlowLayout();
        frame.setLayout(flow);
        JPanel choicePanel = new JPanel();
        JComboBox<String> typeChoices = new JComboBox<>();
        typeChoices.addItem("GPON");
        typeChoices.addItem("EPON");
        /*typeChoices.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectItem = typeChoices.getSelectedItem().toString();
                String ponid = ponidText.getText();
                String ponid = ponidText.getText();
                calcIndex(ponid, selectItem);
            }
        });*/

        JLabel jLabel = new JLabel("类型");

        JLabel companyLabel = new JLabel("厂商");
        JComboBox<String> companyComboBox = new JComboBox<>();
        companyComboBox.addItem("HW");
        companyComboBox.addItem("ZTE");
        companyComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeChoices.removeAllItems();
                String company = companyComboBox.getSelectedItem().toString();
                if (company.equals("HW")) {
                    typeChoices.addItem("GPON");
                    typeChoices.addItem("EPON");
                    hexResult.setVisible(false);
                    tenResult.setVisible(true);
                    result.setVisible(true);
                    reverseBtn.setVisible(true);
                } else if (company.equals("ZTE")) {
                    typeChoices.addItem("平台类型1");
                    typeChoices.addItem("平台类型3");
                    typeChoices.addItem("平台类型4");
                    typeChoices.addItem("平台类型9");
                    typeChoices.addItem("平台类型10");
                    typeChoices.addItem("PON类型1");
                    typeChoices.addItem("PON类型3");
                    typeChoices.addItem("PON类型6");
                    typeChoices.addItem("PON类型7");
                    typeChoices.addItem("PON类型9");
                    hexResult.setVisible(true);
                    tenResult.setVisible(false);
                    result.setVisible(false);
                    reverseBtn.setVisible(false);
                }
            }
        });


        JTextField oltText = new JTextField(15);

        oltText.setSize(100, 10);
        oltText.setVisible(true);
        oltText.setBackground(Color.gray);

        ponidText.setBackground(Color.lightGray);
        ponidText.setText("NA-");
        ponidText.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPon) {
                    ponidText.setBackground(Color.gray);
                    ponidText.setText("NA-");
                    isPon = true;
                }
            }
        });
        ponidText.setLayout(new GridLayout(2, 1));

        choicePanel.add(companyLabel);
        choicePanel.add(companyComboBox);
        choicePanel.add(jLabel);
        choicePanel.add(typeChoices);
        JPanel input = new JPanel();
        jLabel = new JLabel("OLTID");
        input.add(jLabel);
        input.add(oltText);
        JPanel input_ponid = new JPanel();
        jLabel = new JLabel("PONID");
        input_ponid.add(jLabel);
        input_ponid.add(ponidText);
//        JPanel result = new JPanel();

        rs.setEditable(false);
        rs.setSize(200, 10);
        jLabel = new JLabel("二进制");
        result.add(jLabel);
        result.add(rs);

//        JPanel tenResult = new JPanel();
        tenRs.setSize(200, 10);
        tenRs.setEditable(true);
        tenRs.setBackground(Color.lightGray);
        jLabel = new JLabel("十进制");
        tenResult.add(jLabel);
        tenResult.add(tenRs);

//        JPanel  hexResult = new JPanel();
        hexRs.setSize(200, 10);
        hexRs.setEditable(false);
        hexRs.setBackground(Color.gray);
        jLabel = new JLabel("十六进制");

        hexResult.add(jLabel);
        hexResult.add(hexRs);
        hexResult.setVisible(false);

        Panel btnPanel = new Panel();
        Button yesBtn = new Button();
        yesBtn.addActionListener(e -> {
            {
                String selectItem = typeChoices.getSelectedItem().toString();
                String ponid = ponidText.getText();
                String company = companyComboBox.getSelectedItem().toString();
                calcIndex(ponid, selectItem, company);
            }
        });
        yesBtn.setLabel("计算");
        btnPanel.add(yesBtn);

//        Button reverseBtn = new Button();
        reverseBtn.addActionListener(e -> {
            {
                String rs = tenRs.getText().trim();
                String selectItem = typeChoices.getSelectedItem().toString();
                reverseCalcIndex(selectItem, rs);
            }
        });
        reverseBtn.setLabel("反向计算");
        btnPanel.add(reverseBtn);
        frame.add(choicePanel);
        frame.add(input);
        frame.add(input_ponid);
        frame.add(result);
        frame.add(tenResult);
        frame.add(hexResult);
        frame.add(btnPanel);
        frame.setVisible(true);

    }

    private static void calcIndex(String ponid, String selectItem, String company) {
        if (company.equals("HW")) {
            if (("NA-").equals(ponid)) {
                return;
            }
            if (ponid == null || ponid.trim().equals("") || !isRegRight(ponid)) {
                ponidText.setBackground(Color.ORANGE);
                ponidText.setText("格式错误!");
                isPon = false;
                tenRs.setText("");
                rs.setText("");
                return;
            }
            if ("GPON".equals(selectItem)) {
                tenRs.setText(getGPONIfIndex(ponid));
                rs.setText(Long.toBinaryString(Long.valueOf(getGPONIfIndex(ponid))));
            } else if ("EPON".equals(selectItem)) {
                tenRs.setText(getEPONIfIndex(ponid));
                rs.setText(Long.toBinaryString(Long.valueOf(getEPONIfIndex(ponid))));
            }
            tenRs.setBackground(Color.lightGray);
            ponidText.setBackground(Color.lightGray);
        } else if (company.equals("ZTE")) {


        }
    }

    private static void reverseCalcIndex(String selectItem, String rsStr) {
        if (rsStr == null || rsStr.trim().equals("")) {
            tenRs.setText("不能为空!");
            tenRs.setBackground(Color.magenta);
            return;
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher m = pattern.matcher(rsStr);
        if (!m.matches()) {
            tenRs.setText("必须为数字!");
            tenRs.setBackground(Color.pink);
            return;
        }
        String bin = Long.toBinaryString(Long.valueOf(rsStr));
        rs.setText(bin);
        ponidText.setText("NA-" + Long.valueOf(bin.substring(7, 13), 2) + "-" + Long.valueOf(bin.substring(13, 19), 2) + "-" + Long.valueOf(bin.substring(19, 24), 2));
        ponidText.setBackground(Color.lightGray);
        tenRs.setBackground(Color.lightGray);
    }

    private static boolean isRegRight(String ponid) {
        Pattern pattern = Pattern.compile("^(NA|0|[1-9]?\\d)-(0|[1-9]?\\d)-(0|[1-9]?\\d)-(0|[1-9]?\\d)$");
        Matcher m = pattern.matcher(ponid);
        return m.matches();
    }

    private static String getGPONIfIndex(String ponid) {
        String d = "1111101" + getBinary(ponid.split("-")[1], 6) + getBinary(ponid.split("-")[2], 6) + getBinary(ponid.split("-")[3], 5) + "00000000";
        return Long.valueOf(d, 2).toString();
    }

    private static String getEPONIfIndex(String ponid) {
        String d = "1111110" + getBinary(ponid.split("-")[1], 6) + getBinary(ponid.split("-")[2], 6) + getBinary(ponid.split("-")[3], 5) + "00000000";
        return Long.valueOf(d, 2).toString();
    }

    private static String getBinary(String num, int cap) {
        String bin = Integer.toBinaryString(Integer.valueOf(num));
        while (bin.length() < cap) {
            bin = "0" + bin;
        }
        return bin;
    }
}