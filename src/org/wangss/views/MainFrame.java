package org.wangss.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangss on 2016/12/13.
 */
public class MainFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(300, 400);
        frame.setLocation(300, 300);
        frame.setTitle("二进制ifIndex");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FlowLayout flow = new FlowLayout();
        GridLayout borderLayout = new GridLayout();

        frame.setLayout(flow);


        JPanel choicePanel = new JPanel();
        JComboBox<String> typeChoices = new JComboBox<String>();
        typeChoices.addItem("GPON");
        typeChoices.addItem("EPON");
        /*typeChoices.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                calcRes();
            }

            private void calcRes() {

            }
        });*/

        JLabel jLabel = new JLabel("类型");

        JTextField oltText = new JTextField(15);

        oltText.setSize(100, 10);
        oltText.setVisible(true);
        oltText.setBackground(Color.gray);

        JTextField ponidText = new JTextField(15);
        ponidText.setBackground(Color.gray);
        ponidText.setText("NA-");
        ponidText.setLayout(new GridLayout(2, 1));

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
        JPanel result = new JPanel();
        JTextField rs = new JTextField(20);
        rs.setEditable(false);
        rs.setSize(200, 10);
        jLabel = new JLabel("二进制");
        result.add(jLabel);
        result.add(rs);

        JPanel tenResult = new JPanel();
        JTextField tenRs = new JTextField(20);
        tenRs.setSize(200, 10);
        tenRs.setEditable(false);
        jLabel = new JLabel("十进制");
        tenResult.add(jLabel);
        tenResult.add(tenRs);


        Panel btnPanel = new Panel();
        Button yesBtn = new Button();
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectItem = typeChoices.getSelectedItem().toString();
                String ponid = ponidText.getText();
                if (ponid == null || ponid.trim().equals("") || !isRegRight(ponid)) {
                    ponidText.setBackground(Color.red);
                    ponidText.setText("格式错误!");
                    return;
                }
                if ("GPON".equals(selectItem)) {
                    tenRs.setText(getGPONIfIndex(ponid));
                    rs.setText(Long.toBinaryString(Long.valueOf(getGPONIfIndex(ponid))));
                } else if ("EPON".equals(selectItem)) {
                    tenRs.setText(getEPONIfIndex(ponid));
                    rs.setText(Long.toBinaryString(Long.valueOf(getEPONIfIndex(ponid))));
                }
                ponidText.setBackground(Color.gray);

            }
            private boolean isRegRight(String ponid) {
                Pattern pattern = Pattern.compile("^(NA|0|[1-9]?\\d)-(0|[1-9]?\\d)-(0|[1-9]?\\d)-(0|[1-9]?\\d)$");
                Matcher m = pattern.matcher(ponid);
                return m.matches();
            }
        });
        yesBtn.setLabel("计算");
        btnPanel.add(yesBtn);
        frame.add(choicePanel);
        frame.add(input);
        frame.add(input_ponid);
        frame.add(result);
        frame.add(tenResult);
        frame.add(btnPanel);
        frame.setVisible(true);

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

