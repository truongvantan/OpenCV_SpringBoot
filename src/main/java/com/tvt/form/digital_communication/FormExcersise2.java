package com.tvt.form.digital_communication;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tvt.config.AppConfiguration;
import javax.swing.JTextField;

@Component
public class FormExcersise2 extends JFrame {

	private static final long serialVersionUID = 1L;

	private final AppConfiguration appConfiguration;

	private JPanel contentPane;
	private JList<String> listSource;
	private JList<String> listTarget;
	private JLabel lblNewLabel;
	private JLabel lblTarget;
	private JButton btnMoveUp;
	private JButton btnMoveDown;
	private JButton btnAdd;
	private JButton btnRemove;
	private Vector<String> itemsSource;
	private Vector<String> itemsTarget;
	private JTextField txtItemValue;
	private JButton btnInsert;

//	public FormExcersise2() {
//		initComponent();
//	}

	@Autowired
	public FormExcersise2(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		initComponent();
	}

	private void initComponent() {
		try {
			UIManager.setLookAndFeel(appConfiguration.getUILookAndFeel());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		this.setTitle("Excersise 2");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1028, 582);
		this.setLocationRelativeTo(null);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		itemsSource = new Vector<>(List.of("A", "B", "C", "D", "E", "F"));

		listSource = new JList<String>();
		listSource.setListData(itemsSource);

		listSource.setFont(new Font("Tahoma", Font.PLAIN, 13));
		listSource.setVisibleRowCount(10);
		listSource.setBounds(26, 75, 286, 365);
		contentPane.add(listSource);

		itemsTarget = new Vector<String>();

		listTarget = new JList<String>();
		listTarget.setListData(itemsTarget);
		listTarget.setVisibleRowCount(10);
		listTarget.setBounds(499, 75, 286, 365);
		contentPane.add(listTarget);

		lblNewLabel = new JLabel("Source");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(26, 47, 71, 14);
		contentPane.add(lblNewLabel);

		lblTarget = new JLabel("Target");
		lblTarget.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTarget.setBounds(499, 49, 71, 14);
		contentPane.add(lblTarget);

		btnAdd = new JButton("Add >>");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAdd.setBounds(338, 204, 118, 32);
		contentPane.add(btnAdd);

		btnRemove = new JButton("<< Remove");
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRemove.setBounds(338, 247, 118, 32);
		contentPane.add(btnRemove);

		btnMoveUp = new JButton("^ Move Up");
		btnMoveUp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnMoveUp.setBounds(818, 204, 118, 32);
		contentPane.add(btnMoveUp);

		btnMoveDown = new JButton("Move Down");
		btnMoveDown.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnMoveDown.setBounds(818, 247, 118, 32);
		contentPane.add(btnMoveDown);

		txtItemValue = new JTextField();
		txtItemValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtItemValue.setBounds(148, 451, 164, 23);
		contentPane.add(txtItemValue);
		txtItemValue.setColumns(10);

		btnInsert = new JButton("Insert item");
		btnInsert.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnInsert.setBounds(26, 451, 100, 23);
		contentPane.add(btnInsert);

		handleEvent();
	}

	private void handleEvent() {
		btnAdd_Click();
		btnRemove_Click();
		btnMoveUp_Click();
		btnMoveDown_Click();
		btnInsert_Click();
	}

	private void btnInsert_Click() {
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtItemValue.getText() == null || txtItemValue.getText().isBlank()) {
					JOptionPane.showMessageDialog(null, "Cannot insert a blank value", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (itemsSource.contains(txtItemValue.getText())) {
					JOptionPane.showMessageDialog(null, "The item: " + txtItemValue.getText() + " is exist!!!", "WARNING",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				itemsSource.add(txtItemValue.getText());
				listSource.setListData(itemsSource);
			}
		});
	}

	private void btnMoveDown_Click() {
		btnMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexListTargetSelected = listTarget.getSelectedIndex();

				if (indexListTargetSelected < 0) {
					JOptionPane.showMessageDialog(null, "Please select at least 1 row in target box", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int[] selectedIndices = listTarget.getSelectedIndices();
				int lengthSelected = selectedIndices.length;
				int indexSelected = -1;
				String itemSelected = null;
				int[] newSelectedIndices = new int[selectedIndices.length];
				int newIndex = -1;
				int itemsLength = itemsTarget.size();

				for (int i = lengthSelected - 1; i >= 0; i--) {
					indexSelected = selectedIndices[i];
					if (indexSelected < itemsLength - 1) {
						itemSelected = itemsTarget.get(indexSelected);
						itemsTarget.removeElementAt(indexSelected);
						itemsTarget.insertElementAt(itemSelected, indexSelected + 1);

						// update new selected index
						newIndex = Math.min(itemsLength - 1, indexSelected + 1);
						newSelectedIndices[i] = newIndex;
					} else {
						newSelectedIndices[i] = indexSelected;
					}
				}
				// Update selection
				listTarget.setSelectedIndices(newSelectedIndices);
			}
		});
	}

	private void btnMoveUp_Click() {
		btnMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexListTargetSelected = listTarget.getSelectedIndex();

				if (indexListTargetSelected < 0) {
					JOptionPane.showMessageDialog(null, "Please select at least 1 row in target box", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int[] selectedIndices = listTarget.getSelectedIndices();
				int lengthSelected = selectedIndices.length;
				int indexSelected = -1;
				String itemSelected = null;
				int[] newSelectedIndices = new int[selectedIndices.length];
				int newIndex = -1;

				for (int i = 0; i < lengthSelected; i++) {
					indexSelected = selectedIndices[i];
					if (indexSelected > 0) {
						itemSelected = itemsTarget.get(indexSelected);
						itemsTarget.removeElementAt(indexSelected);
						itemsTarget.insertElementAt(itemSelected, indexSelected - 1);

						// update new selected index
						newIndex = Math.max(0, indexSelected - 1);
						newSelectedIndices[i] = newIndex;
					}
				}

				// Update selection
				listTarget.setSelectedIndices(newSelectedIndices);

			}
		});
	}

	private void btnRemove_Click() {
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexListTargetSelected = listTarget.getSelectedIndex();

				if (indexListTargetSelected < 0) {
					JOptionPane.showMessageDialog(null, "Please select at least 1 row in target box", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// add items selected to source box
				addItemsToJList(listTarget, listSource, itemsSource);

				// remove items selected in target box
				removeItemsJList(listTarget, itemsTarget);
			}
		});
	}

	private void btnAdd_Click() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexListSourceSelected = listSource.getSelectedIndex();

				if (indexListSourceSelected < 0) {
					JOptionPane.showMessageDialog(null, "Please select at least 1 row in source box", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// add items selected to target box
				addItemsToJList(listSource, listTarget, itemsTarget);
				// remove items selected in source box
				removeItemsJList(listSource, itemsSource);
			}
		});
	}

	/**
	 * Add items selected from JList Source to JList target
	 */
	private void addItemsToJList(JList<String> jListSource, JList<String> jListTarget, Vector<String> itemsTarget) {
		List<String> selectedValuesList = jListSource.getSelectedValuesList();
		itemsTarget.addAll(selectedValuesList);
		jListTarget.setListData(itemsTarget);
	}

	/**
	 * Remove list items selected of a JList
	 */
	private void removeItemsJList(JList<String> jList, Vector<String> items) {
		int[] selectedIndices = jList.getSelectedIndices();
		int lengthSelected = selectedIndices.length;

		for (int i = lengthSelected - 1; i >= 0; i--) {
			items.remove(selectedIndices[i]);
		}

		jList.setListData(items);
	}
}
