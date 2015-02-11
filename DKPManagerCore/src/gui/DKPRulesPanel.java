package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DKPRulesPanel extends JPanel {

    private static final String RULES = "1. ��һ���ڛ]��LOOT�� �����Էe��(���b��]��Ҫ���Գ���)\n"
            + "2. ���Ϸ� 10 ��ɢ�� 10 ����;�뿪����û�н�ɢ�֣� Boss ��ͨ10 Ӣ��15 ����20 ���ĳɹ���˫������\n"
            + "3. DKP ÿ��˥�p10% ����ְ�\n"
            + "4. ��, ����, ���L, Ь��, ����, ���� ,�o�󣬵������� �׃r30(ÿ��+10) �^��, ���z, �� �׃r40(ÿ��+15) ˫������, �Ʒ, ��װToken �׃r50(ÿ��+20)\n"
            + "5. DKP һ������ �� ��ֹ����쭷֣�һ���ж������ط��� (���磺����װ�����������ϵĺã���ȴ����̧�֣�����ֲ���loot��������ж�Ϊ����쭷֣��ͷ��۳����������ն��۵�dkp)\n"
            + "6. ���˾��֣������˽�Ϊ���֣�������쭷֣�ʣ��dkp����Ϊ���� ��������һ�˸��֣��򸺷�����Ȩ���֣� �����˽Ը��֣�������쭷֣�ʣ��dkp���ܵ��� -100�� �κε��� -100�ߣ���lootȨ��\n"
            + "7. DKP��Ҫת������һ����ɫ�ģ�ÿ�ο۳���ǰDKP��50%��Ϊ�ͷ���\n";

    public DKPRulesPanel() {
        this.setLayout(new GridLayout(0, 1));
        JTextArea textArea = new JTextArea(RULES);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(textArea);
        this.add(sp);
    }
}
