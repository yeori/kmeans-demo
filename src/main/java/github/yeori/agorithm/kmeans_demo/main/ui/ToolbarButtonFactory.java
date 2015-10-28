package github.yeori.agorithm.kmeans_demo.main.ui;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public final class ToolbarButtonFactory {
	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source text "RUN"
	 */
	public static JButton createToolbarButton(String text, String iconName, int size) {
		JButton button = new JButton();
		button.setActionCommand(text);
		String path = "/icon/" + iconName ;
		URL iconUrl = ToolbarButtonFactory.class.getResource(path);
		try {
			Image icon = fitSize(ImageIO.read(iconUrl), size);
			button.setIcon(new ImageIcon(icon));
			button.setFocusable(false);
			return button;
		} catch (IOException e) {
			e.printStackTrace();
			return button;
		}
	}
	
	public static JButton createToolbarBotton(String buttonText, int height) {
		JButton btn = new JButton(buttonText);
		return btn;
	}

	private static Image fitSize(BufferedImage image, int size) {
		return image.getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
	}
}