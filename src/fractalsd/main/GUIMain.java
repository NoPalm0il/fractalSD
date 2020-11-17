package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.fractal.engine.GenFractal;
import fractalsd.fractal.models.Index;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Classe principal GUI
 */
public class GUIMain {
    private JPanel mainPanel;
    private JPanel fractalPanel;
    private JLabel fractalLabel;
    private JButton genFractalBt;
    private JComboBox fractalsCombo;
    private JLabel cordsLabel;
    private JTextField zoomTextField;
    private JTextField iterTextField;
    private JProgressBar progressBar;
    private JTabbedPane tabbedPane1;
    private JPanel fractalTabbedPanel;
    private JPanel colorTabbedPanel;
    private JComboBox colorComboBox;
    private JPanel mainJPanel;
    private JPanel colorJPanel;
    private JScrollPane fractalScroll;
    private JTextField xTextField;
    private JTextField yTextField;
    private JTextField centerXtextField;
    private JTextField centerYtextField;
    private JButton resetBt;
    private JPanel infoTabbedPanel;
    private JTextArea infoTextArea;
    private JButton smallBt;
    private JButton fullHDBt;
    private JButton hdBt;
    private JButton fourBt;
    private JCheckBox bigDecCheckBox;
    private JSlider brightnessSlider;
    private JSlider saturationSlider;
    private JSlider hueSlider;

    private Point2D center;
    private Object zoomSize;
    private int zoomSizeDecCount;
    private int iteration;
    private int pictureSizeX;
    private int pictureSizeY;
    private BufferedImage fractalBufferedImage;
    private ColorShifter pl;
    private GenFractal fr;

    public GUIMain() {
        // botão para gerar um Fractal com os parâmetros presentes nos JTextFields
        genFractalBt.addActionListener(e -> {
            // verifica se a CheckBox de "BigDecimal" esta selecionada
            // caso esteja, executa os calculos com os valores BigDecimal
            if (bigDecCheckBox.isSelected()) {
                zoomSize = new BigDecimal(zoomTextField.getText());
                zoomSizeDecCount = ((BigDecimal) zoomSize).scale();
            } else
                zoomSize = Double.parseDouble(zoomTextField.getText());

            // fractal iterations
            iteration = Integer.parseInt(iterTextField.getText());
            // altura e largura (height and width) da imagem
            pictureSizeX = Integer.parseInt(xTextField.getText());
            pictureSizeY = Integer.parseInt(yTextField.getText());

            // centro com as informacoes dos textFields relativos ao mesmo
            center = new Point2D.Double(Double.parseDouble(centerXtextField.getText()), Double.parseDouble(centerYtextField.getText()));


            if ((float) pictureSizeX / (float) pictureSizeY > 1.5f)
                center = getRealCoordinates(pictureSizeX / 1.0 / pictureSizeY / 2.0, pictureSizeY / 2.0, pictureSizeY);

            // limpa a area de info para colocar as informacoes atualizadas
            infoTextArea.setText("");
            showInfo();

            // gera o Fratal e executa
            fr = new GenFractal(this);
            fr.execute();
        });

        // botao de RESET para voltar aos parametros iniciais
        resetBt.addActionListener(e -> {
            // escolha dinamica para verificar se a checkBox do BigDecimal esta selecionada
            if (bigDecCheckBox.isSelected())
                zoomSize = new BigDecimal("5.0");
            else
                zoomSize = 5.0;
            // fractal iterations
            iteration = 256;
            // image w and h in pixels resolution
            pictureSizeX = 400;
            pictureSizeY = 400;
            center = new Point2D.Double(0, 0);

            // colocar as informacoes dinamicamente nos textFields para quando for
            // a geracao do Fractal, este poder ler as mesmas
            zoomTextField.setText("5");
            iterTextField.setText("256");
            xTextField.setText("400");
            yTextField.setText("400");
            centerXtextField.setText("0");
            centerYtextField.setText("0");

            // limpar a area de info e colocar as informacoes atualizadas
            infoTextArea.setText("");
            showInfo();

            // gerar o "novo" Fractal
            fr = new GenFractal(this);
            fr.execute();

            centerXtextField.setText("" + center.getX());
            centerYtextField.setText("" + center.getY());
        });

        // botão para a definição padrão "small" (200x200)
        smallBt.addActionListener(e -> {
            xTextField.setText("200");
            yTextField.setText("200");

            genFractalBt.doClick();
        });

        // botão para a definição HD (1280x720)
        hdBt.addActionListener(e -> {
            xTextField.setText("1280");
            yTextField.setText("720");

            genFractalBt.doClick();
        });

        // botão para a definição FullHD (1920x1080)
        fullHDBt.addActionListener(e -> {
            xTextField.setText("1920");
            yTextField.setText("1080");

            genFractalBt.doClick();
        });

        // botão para a definição 4K (3840x2160)
        fourBt.addActionListener(e -> {
            xTextField.setText("3840");
            yTextField.setText("2160");

            genFractalBt.doClick();
        });

        fractalLabel.addMouseListener(new MouseAdapter() {
            @Override
            // evento para a ocorrencia de se ampliar a imagem com um clique na mesma
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // limitador, para nao se clicar fora dos limites
                if (e.getX() < pictureSizeX && e.getY() < pictureSizeY) {
                    center = getRealCoordinates(e.getX(), e.getY(), pictureSizeY);
                    // resultado para o caso de se clicar no botao de ampliar a imagem
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (bigDecCheckBox.isSelected())
                            zoomSize = new BigDecimal(zoomTextField.getText()).divide(new BigDecimal("4.0"), 20, RoundingMode.CEILING);
                        else
                            zoomSize = Double.parseDouble(zoomTextField.getText()) / 4;
                    }
                    // resultado para o caso de se clicar no botao para diminuir a imagem
                    else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (bigDecCheckBox.isSelected())
                            zoomSize = new BigDecimal(zoomTextField.getText())
                                    .multiply(new BigDecimal("4.0")).setScale(20, RoundingMode.CEILING);
                        else
                            zoomSize = Double.parseDouble(zoomTextField.getText()) * 4;
                    }

                    infoTextArea.setText("");
                    showInfo();
                    fr = new GenFractal(GUIMain.this);
                    fr.execute();

                    zoomTextField.setText("" + zoomSize);
                    centerXtextField.setText("" + center.getX());
                    centerYtextField.setText("" + center.getY());
                }
            }
        });

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        // evento para dar output das coordenadas do rato em tempo real
        fractalLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (e.getX() < pictureSizeX && e.getY() < pictureSizeY) {
                    Point2D mouseRealCord = getRealCoordinates(e.getX(), e.getY(), pictureSizeY);
                    cordsLabel.setText("x: " + df.format(mouseRealCord.getX()) + " y: " + df.format(mouseRealCord.getY()));
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });

        // quando se seleciona um fractal do fractalsCombo, sao selecionadas e colocadas estas opcoes nos textFields
        fractalsCombo.addItemListener(e -> {
            zoomTextField.setText("5");
            iterTextField.setText("256");
            xTextField.setText("400");
            yTextField.setText("400");
            centerXtextField.setText("0");
            centerYtextField.setText("0");
        });

        // evento para quando se altera o "slider" do HUE
        hueSlider.addChangeListener(evt -> {
            if (fractalBufferedImage != null && pl != null) {
                pl.setHueShift(hueSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
        // evento para quando se altera o "slider" da SATURATION
        saturationSlider.addChangeListener(evt -> {
            if (fractalBufferedImage != null && pl != null) {
                pl.setSaturationShift(saturationSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
        // evento para quando se altera o "slider" da BRIGHTNESS
        brightnessSlider.addChangeListener(evt -> {
            if (fractalBufferedImage != null && pl != null) {
                pl.setBrightnessShift(brightnessSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
    }

    private void createUIComponents() {
        fractalLabel = new JLabel();
        zoomTextField = new JTextField();
        iterTextField = new JTextField();
        progressBar = new JProgressBar();
        fractalScroll = new JScrollPane();

        // comando para alinhar a fractalLabel ao topo superior esquerdo
        fractalLabel.setVerticalAlignment(SwingConstants.NORTH);

        // declaracao do combobox dos fractais
        fractalsCombo = new JComboBox();
        Index idx = new Index();
        for (Fractal f : idx.fractals)
            fractalsCombo.addItem(f);

        progressBar.setVisible(false);
    }

    // metodo para colocar os outputs das informacoes do Fratal
    private void showInfo() {
        // cada vez que o metodo e executado, limpamos a string "info" para colocar novas informacoes
        String info = "";

        info += "Fractal Name: " + fractalsCombo.getSelectedItem();
        info += "\n\nMax Iterations: " + iterTextField.getText();
        info += "\nZoom: " + zoomTextField.getText();
        info += "\n\nImage Width: " + xTextField.getText();
        info += "\nImage Height: " + yTextField.getText();
        info += "\n\nHue Value: " +hueSlider.getValue();
        info += "\nSaturation Value: " +saturationSlider.getValue();
        info += "\nBrightness Value: " +brightnessSlider.getValue();

        // inserimos o valor da variavel "info" na area de "info"
        infoTextArea.insert(info, 0);
     }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // metodo para receber as coordenadas reais a partir da label
    public Point2D getRealCoordinates(double xx, double yy, int size) {
        double ws = Double.parseDouble(zoomTextField.getText());
        double pixelSize = ws / size;
        double minX = center.getX() - ws / 2;
        double miny = center.getY() + ws / 2;
        double x = minX + pixelSize * xx;
        double y = miny - pixelSize * yy;
        return new Point2D.Double(x, y);
    }

    // metodo para obter os valores dos sliders de HSB
    public float[] getSliderHSB() {
        return new float[]{(float) hueSlider.getValue() / 100, (float) saturationSlider.getValue() / 100, (float) brightnessSlider.getValue() / 100};
    }


    public JLabel getFractalLabel() {
        return fractalLabel;
    }

    public void setFractalBufferedImage(BufferedImage fractalBufferedImage) {
        this.fractalBufferedImage = fractalBufferedImage;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setPl(ColorShifter pl) {
        this.pl = pl;
    }

    public JScrollPane getFractalScroll() {
        return fractalScroll;
    }

    public Object getZoomSize() {
        return zoomSize;
    }

    public int getIteration() {
        return iteration;
    }

    public int getPictureSizeX() {
        return pictureSizeX;
    }


    public int getPictureSizeY() {
        return pictureSizeY;
    }

    public JComboBox getFractalsCombo() {
        return fractalsCombo;
    }

    public JCheckBox getBigDecCheckBox() {
        return bigDecCheckBox;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public int getZoomSizeDecCount() {
        return zoomSizeDecCount;
    }
}
