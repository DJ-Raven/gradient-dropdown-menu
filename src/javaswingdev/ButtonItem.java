package javaswingdev;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class ButtonItem extends JButton {

    public int getIndex() {
        return index;
    }

    public boolean isMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(boolean mainMenu) {
        this.mainMenu = mainMenu;
    }

    public int getButtonAngle() {
        return buttonAngle;
    }

    public void setButtonAngle(int buttonAngle) {
        this.buttonAngle = buttonAngle;
    }

    public boolean isPaintIcon() {
        return paintIcon;
    }

    public void setPaintIcon(boolean paintIcon) {
        this.paintIcon = paintIcon;
        if (paintIcon) {
            setBorder(new EmptyBorder(0, 20, 0, 40));
        } else {
            setBorder(new EmptyBorder(0, 20, 0, 20));
        }
    }

    private Animator animator;
    private boolean mouseOver;
    private float animate;
    private float animateText;
    private boolean paintIcon;
    private int buttonAngle;
    private boolean mainMenu;
    private int index;

    public ButtonItem(int index) {
        this.index = index;
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(0, 20, 0, 20));
        setContentAreaFilled(false);
        setFocusable(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!mainMenu) {
                    stopAnimation();
                    animate = 0;
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
                stopAnimation();
                animator.start();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                mouseOver = false;
                stopAnimation();
                animator.start();
            }
        });
        animator = new Animator(300, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (mouseOver) {
                    animateText = fraction;
                    fraction *= 0.4;
                    animate = fraction;
                } else {
                    animateText = 1f - fraction;
                    fraction *= 0.4;
                    animate = 0.4f - fraction;
                }
                repaint();
            }
        });
        animator.setResolution(0);
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

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        if (paintIcon) {
            g2.setColor(getForeground());
            int x = getLocationIcon(g2);
            int y = height / 2 - 2;
            Path2D p2 = new Path2D.Double();
            p2.moveTo(x, y);
            p2.lineTo(x + 4, y + 4);
            p2.lineTo(x + 8, y);
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(buttonAngle), x + 4, y + 2);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(at.createTransformedShape(p2));
        }
        if (mainMenu) {
            int textWidth = getTextWidth(g2);
            int size = (int) (textWidth * animateText);
            int x = (width - size) / 2;
            int y = getLocationLine(g2);
            g2.setColor(getForeground());
            g2.fillRoundRect(x, y, size, 2, 2, 2);
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animate));
            g2.setColor(new Color(50, 50, 50));
            g2.fillRoundRect(0, 0, width, height, 5, 5);
        }
        g2.dispose();
        super.paintComponent(grphcs);
    }

    private int getLocationIcon(Graphics2D g2) {
        double fontw = g2.getFontMetrics().getStringBounds(getText(), g2).getWidth();
        double x = (getWidth() - fontw) / 2;
        return (int) (x + fontw);
    }

    private int getTextWidth(Graphics2D g2) {
        return (int) g2.getFontMetrics().getStringBounds(getText(), g2).getWidth() + (paintIcon ? 20 : 0);
    }

    private int getLocationLine(Graphics2D g2) {
        double fontH = g2.getFontMetrics().getStringBounds(getText(), g2).getHeight();
        double x = (getHeight() - fontH) / 2;
        return (int) (x + fontH + 3);
    }
}
