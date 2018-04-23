package Server;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class SingleQuestionPane extends JPanel {
    private JRadioButton rdbtnOpt0;
    private JLabel lblQuestion;
    private JRadioButton rdbtnOpt1;
    private JRadioButton rdbtnOpt2;
    private JRadioButton rdbtnOpt3;
    private final ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * Create the panel.
     */

    public String getSelectedOptionText() {

        if (rdbtnOpt0.isSelected()) {
            return rdbtnOpt0.getText();
        }
        if (rdbtnOpt1.isSelected()) {
            return rdbtnOpt1.getText();
        }
        if (rdbtnOpt2.isSelected()) {
            return rdbtnOpt2.getText();
        }
        if (rdbtnOpt3.isSelected()) {
            return rdbtnOpt3.getText();
        }

        return "";

    }

    public SingleQuestionPane(Question q) {
        this();

        lblQuestion.setText(q.getQuestionText());
        rdbtnOpt0.setText(q.getOptions().get(0));
        rdbtnOpt1.setText(q.getOptions().get(1));
        rdbtnOpt2.setText(q.getOptions().get(2));
        rdbtnOpt3.setText(q.getOptions().get(3));
    }

    public SingleQuestionPane() {

        lblQuestion = new JLabel("Question 1:");

        rdbtnOpt0 = new JRadioButton("Opt1");
        buttonGroup.add(rdbtnOpt0);

        rdbtnOpt1 = new JRadioButton("Opt2");
        buttonGroup.add(rdbtnOpt1);

        rdbtnOpt2 = new JRadioButton("Opt3");
        buttonGroup.add(rdbtnOpt2);

        rdbtnOpt3 = new JRadioButton("Opt4");
        buttonGroup.add(rdbtnOpt3);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(rdbtnOpt3)
                                .addComponent(rdbtnOpt2).addComponent(rdbtnOpt1).addComponent(rdbtnOpt0)
                                .addComponent(lblQuestion))
                        .addContainerGap(173, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblQuestion)
                        .addPreferredGap(ComponentPlacement.UNRELATED).addComponent(rdbtnOpt0)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(rdbtnOpt1)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(rdbtnOpt2)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(rdbtnOpt3)
                        .addContainerGap(27, Short.MAX_VALUE)));
        setLayout(groupLayout);

    }

}
