///****************************************************************************/
///****************************************************************************/
///****     Copyright (C) 2013                                             ****/
///****     Antonio Manuel Rodrigues Manso                                 ****/
///****     e-mail: manso@ipt.pt                                           ****/
///****     url   : http://orion.ipt.pt/~manso                             ****/
///****     Instituto Politecnico de Tomar                                 ****/
///****     Escola Superior de Tecnologia de Tomar                         ****/
///****************************************************************************/
///****************************************************************************/
///****     This software was built with the purpose of investigating      ****/
///****     and learning. Its use is free and is not provided any          ****/
///****     guarantee or support.                                          ****/
///****     If you met bugs, please, report them to the author             ****/
///****                                                                    ****/
///****************************************************************************/
///****************************************************************************/
package fractalsd.utils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Zulu
 */
public class GuiUtils {

    /**
     * adiciona uma mensagem no      *
     *
     * @param txt area de texto para apresentacao da mensagem
     * @param msg mensagem
     */
    public static void addText(final JTextPane txt, final String msg, final Color color) {
        SwingUtilities.invokeLater(() -> {
            try {
                Date now = new Date();
                String strDate = sdfDate.format(now);
                StyleContext sc = new StyleContext();
                Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
                Document doc = txt.getDocument();
                StyleConstants.setForeground(style, Color.DARK_GRAY);
                doc.insertString(doc.getLength(), "\n" + strDate + " ", style);
                StyleConstants.setForeground(style, color);
                doc.insertString(doc.getLength(), msg, style);
            } catch (BadLocationException ex) {
                Logger.getLogger(GuiUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * adiciona uma mensagem no formato "HH:mm:ss <Titulo> mensagem" no final da
     * caixa de texto com o titulo a Azul
     *
     * @param txt   area de texto para apresentacao da mensagem
     * @param title titulo da mensagem
     * @param ex    exception
     */
    public static void addException(final JTextPane txt, String title, final Exception ex) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyleContext sc = new StyleContext();
                Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
                Date now = new Date();
                String strDate = sdfDate.format(now);
                Document doc = txt.getDocument();
                StyleConstants.setForeground(style, Color.DARK_GRAY);
                doc.insertString(doc.getLength(), "\n" + strDate + " ", style);
                StyleConstants.setForeground(style, Color.PINK);
                doc.insertString(doc.getLength(), title + " ", style);
                StyleConstants.setForeground(style, Color.RED);
                doc.insertString(doc.getLength(), ex.getLocalizedMessage(), style);
            } catch (BadLocationException ex1) {
                Logger.getLogger(GuiUtils.class.getName()).log(Level.SEVERE, null, ex1);
            }
        });
    }

    static SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");

    /**
     * adiciona uma imagem no formato "HH:mm:ss <Titulo> imagem" no final da
     * caixa de texto com o titulo a Azul
     *
     * @param txt   area de texto para apresentacao da mensagem
     * @param title titulo da mensagem
     * @param img   imagem para ser apresentada
     */
    public static void addImage(final JTextPane txt, final String title, final ImageIcon img) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyleContext sc = new StyleContext();
                Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
                SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();
                String strDate = sdfDate.format(now);
                Document doc = txt.getDocument();
                StyleConstants.setForeground(style, Color.RED);
                doc.insertString(doc.getLength(), "\n" + strDate, style);
                StyleConstants.setForeground(style, Color.BLUE);
                doc.insertString(doc.getLength(), " " + title + "\n", style);
                StyleConstants.setIcon(style, img);
                doc.insertString(doc.getLength(), " ", style);
            } catch (BadLocationException ex) {
                Logger.getLogger(GuiUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

}
