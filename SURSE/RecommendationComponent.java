package org.example;

import javax.swing.*;
import java.awt.*;

class RecommendationComponent extends JPanel {
	private JLabel titleLabel;
	private JLabel imageLabel;

	public RecommendationComponent(String title, Icon image) {
		setLayout(new BorderLayout());
		titleLabel = new JLabel(title);
		imageLabel = new JLabel(image);

		add(titleLabel, BorderLayout.NORTH);
		add(imageLabel, BorderLayout.CENTER);
	}
}
