package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DKPRulesPanel extends JPanel {

    private static final String RULES = "1. 第一星期沒有LOOT權 但可以積分(若裝備沒人要可以出分)\n"
            + "2. 集合分 10 解散分 10 （中途离开过则没有解散分） Boss 普通10 英雄15 传奇20 开荒成功皆双倍积分\n"
            + "3. DKP 每周衰減10% 避免分霸\n"
            + "4. 項鍊, 戒子, 披風, 鞋子, 手套, 腰帶 ,護腕，单手武器 底價30(每次+10) 頭盔, 胸鎧, 腿 底價40(每次+15) 双手武器, 飾品, 套装Token 底價50(每次+20)\n"
            + "5. DKP 一律明拍 。 禁止恶意飙分，一经判定将有重罚。 (比如：竞分装备不如你身上的好，你却故意抬分，最后又并不loot，则可以判定为恶意飙分，惩罚扣除两倍于最终定价的dkp)\n"
            + "6. 两人竞分，若两人皆为正分，则正常飙分，剩余dkp不能为负； 若其中有一人负分，则负分者无权出分； 若两人皆负分，则正常飙分，剩余dkp不能低于 -100； 任何低于 -100者，无loot权。\n"
            + "7. DKP需要转换到另一个角色的，每次扣除当前DKP的50%作为惩罚。\n";

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
