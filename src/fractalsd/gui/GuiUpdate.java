/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fractalsd.gui;

import java.awt.*;

/**
 * @author IPT
 */
public interface GuiUpdate {
    void onStart();

    void onStop();

    void onException(String message, Exception e);

    void onDisplay(Color color, String message);
}
