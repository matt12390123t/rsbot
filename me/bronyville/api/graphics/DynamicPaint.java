package me.bronyville.api.graphics;

import me.bronyville.api.impl.Script;

import java.awt.*;
import java.util.Enumeration;

public class DynamicPaint {

    private Point location = new Point(5, 50);
    private Rectangle backgroundRectangle = new Rectangle(location.x, location.y, 1, 1);

    public static Font FONT_TITLE = new Font("Calibri", Font.BOLD, 14);
    public static Font FONT_SMALL = new Font("Calibri", Font.BOLD, 12);

    private static final Color COLOUR_BACKGROUND = new Color(0, 0, 0, 64);
    private static final Color COLOUR_BORDER = new Color(255, 255, 255, 192);
    private final BasicStroke mouseStroke = new BasicStroke(2f);
    private final Script script;
    private LinkedProperties properties;

    public DynamicPaint(final Script script) {
        this.script = script;
    }

    public DynamicPaint properties(final LinkedProperties properties) {
        this.properties = properties;
        return this;
    }

    public void draw(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);


        int x = location.x + 10;
        int y = location.y + 20;
        int width = 1;

        g.setColor(COLOUR_BACKGROUND);
        g2d.fill(backgroundRectangle);
        g.setColor(COLOUR_BORDER);
        g2d.draw(backgroundRectangle);

        g.setFont(FONT_TITLE);
        FontMetrics fontMetrics = g.getFontMetrics();
        width = Math.max(width, fontMetrics.stringWidth(script.getName()));
        g.drawString(script.getName(), x, y);
        y += 25;

        final Enumeration<Object> keys = properties.keys();
        while(keys.hasMoreElements()) {
            final Object element = keys.nextElement();
            final String string = String.format("%s: %s", element, properties.get(element));
            width = Math.max(width, fontMetrics.stringWidth(string));
            g.drawString(string, x, y);
            y += 15;
        }

        drawMouse(g2d);

        backgroundRectangle.width = width + 20;
        backgroundRectangle.height = y - location.y;
    }

    private void drawMouse(Graphics2D g2d) {
        g2d.setStroke(mouseStroke);
        g2d.setColor(System.currentTimeMillis()- script.ctx.mouse.getPressTime() < 500 ? Color.RED : COLOUR_BACKGROUND.WHITE);

        final Point mouse = script.ctx.mouse.getLocation();
        g2d.drawLine(mouse.x - 5, mouse.y - 5, mouse.x + 5, mouse.y + 5);
        g2d.drawLine(mouse.x - 5, mouse.y + 5, mouse.x + 5, mouse.y - 5);
    }
}