package javaswingdev;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import net.miginfocom.swing.MigLayout;

public class GradientDropdownMenu extends javax.swing.JComponent {

    public boolean isHeaderGradient() {
        return headerGradient;
    }

    public void setHeaderGradient(boolean headerGradient) {
        this.headerGradient = headerGradient;
        repaint();
    }
    private List<MenuEvent> events;
    private List<ModelMenuItem> menuItem;
    private MigLayout layout;
    private Color gradientColors[] = {new Color(252, 70, 107), new Color(63, 94, 251)};
    private boolean headerGradient = true;
    private int menuHeight = 40;

    public GradientDropdownMenu() {
        initComponents();
        menuItem = new ArrayList<>();
        events = new ArrayList<>();
        layout = new MigLayout("inset 5", "[fill]0[fill]", "[top, fill]");
        setFocusable(true);
        setBackground(new Color(32, 32, 32));
        setLayout(layout);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent fe) {
                hideShowMenu();
            }
        });
    }

    public void addItem(String... menus) {
        if (menus.length > 0) {
            PanelMenu menu = new PanelMenu(this, layout, getFont(), getComponentCount(), menus);
            add(menu, "h " + menuHeight + "!");
        }
    }

    protected void hideShowMenu(Component... coms) {
        for (Component com : getComponents()) {
            if (!check(com, coms)) {
                ((PanelMenu) com).hideShowingMenu();
            }
        }
    }

    public void hideShowingMenu() {
        hideShowMenu();
    }

    public void addEvent(MenuEvent event) {
        events.add(event);
    }

    private boolean check(Component com, Component... coms) {
        for (Component c : coms) {
            if (c == com) {
                return true;
            }
        }
        return false;
    }

    public void setGradientColor(Color color1, Color color2) {
        gradientColors = new Color[]{color1, color2};
        repaint();
    }

    public Color[] getGradientColor() {
        return gradientColors;
    }

    public void applay(JFrame fram) {
        Container com = fram.getContentPane();
        JLayeredPane layer = new JLayeredPane();
        layer.setLayout(new MigLayout("fill, inset 0", "[fill]", "[fill]"));
        layer.setLayer(this, JLayeredPane.POPUP_LAYER);
        layer.add(this, "pos 0 0 100% n");
        layer.add(com);
        fram.setContentPane(layer);
    }

    public String getMenuNameAt(int index, int subMenu) {
        PanelMenu menu = (PanelMenu) getComponent(index);
        ButtonItem item = (ButtonItem) menu.getComponent(subMenu);
        return item.getText();
    }

    protected void runEvent(int index, int subIndex, boolean menuItem) {
        for (MenuEvent event : events) {
            event.selected(index, subIndex, menuItem);
        }
    }

    public int getMenuHeight() {
        return menuHeight + 10;
    }

    public void setMenuHeight(int menuHeight) {
        this.menuHeight = menuHeight - 10;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = menuHeight + 10;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, width, height);
        if (headerGradient) {
            g2.setPaint(new GradientPaint(0, 0, gradientColors[0], width, 0, gradientColors[1]));
            g2.fillRect(0, 0, width, 3);
        }
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
            .addGap(0, 639, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
