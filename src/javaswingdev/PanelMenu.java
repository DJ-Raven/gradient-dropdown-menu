package javaswingdev;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PanelMenu extends javax.swing.JComponent {

    private final Animator animator;
    private final GradientDropdownMenu gradientMenu;
    private ButtonItem mainMenu;
    private boolean menuShow;
    private float animate;

    public PanelMenu(GradientDropdownMenu gradientMenu, MigLayout layout, Font font, int index, String... menus) {
        initComponents();
        this.gradientMenu = gradientMenu;
        int menuHeight = gradientMenu.getMenuHeight() - 10;
        setLayout(new MigLayout("wrap, inset 0, fillx", "[fill]", "[fill, " + menuHeight + "!]0[fill, " + menuHeight + "!]"));
        int subIndex = 0;
        for (String menu : menus) {
            ButtonItem item = new ButtonItem(subIndex++);
            item.setText(menu);
            item.setFont(font);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    gradientMenu.grabFocus();
                    if (!menuShow || !item.isMainMenu()) {
                        gradientMenu.runEvent(index, item.getIndex(), !item.isMainMenu() || menus.length == 1);
                        if (menuShow) {
                            gradientMenu.hideShowMenu();
                        }
                    }
                }
            });
            add(item);
        }
        mainMenu = (ButtonItem) getComponent(0);
        mainMenu.setMainMenu(true);
        if (menus.length > 1) {
            mainMenu.setPaintIcon(true);
            mainMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    gradientMenu.hideShowMenu(PanelMenu.this);
                    stopAnimation();
                    animator.start();
                }
            });
        }
        animator = new Animator(200, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                int menuHeight = gradientMenu.getMenuHeight() - 10;
                int height = getPreferredSize().height - menuHeight;
                int size;
                if (menuShow) {
                    animate = 1f - fraction;
                    size = (int) (height * (1f - fraction) + menuHeight);
                } else {
                    animate = fraction;
                    size = (int) (height * fraction + menuHeight);
                }
                mainMenu.setButtonAngle((int) (animate * 180));
                layout.setComponentConstraints(PanelMenu.this, "h " + size + "!");
                revalidate();
                repaint();
            }

            @Override
            public void end() {
                menuShow = !menuShow;
            }
        });
        animator.setResolution(0);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
    }

    private void stopAnimation() {
        if (animator.isRunning()) {
            float f = animator.getTimingFraction();
            animator.stop();
            animator.setStartFraction(1f - f);
        } else {
            animator.setStartFraction(0f);
        }
    }

    public void hideShowingMenu() {
        if (menuShow) {
            stopAnimation();
            animator.start();
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        int size = (int) (animate * width);
        int x = (width - size) / 2;
        int y = 0;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animate));
        g2.setPaint(new GradientPaint(0, 0, gradientMenu.getGradientColor()[0], 0, height, gradientMenu.getGradientColor()[1]));
        g2.fillRoundRect(x, y, size, height, 5, 5);
        g2.dispose();
        super.paintComponent(grphcs);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
