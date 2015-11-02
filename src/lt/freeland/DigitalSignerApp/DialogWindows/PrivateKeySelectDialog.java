package lt.freeland.DigitalSignerApp.DialogWindows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class PrivateKeySelectDialog extends JDialog implements ActionListener
	{
		private JPanel contentPanel = new JPanel();
		private JButton okButton;
		private JComboBox privateKeyList;
		private JButton cancelButton;
		
		private BorderLayout bLayout;
		
		String result;

		public PrivateKeySelectDialog(Enumeration<String> aliases)
			{
				bLayout = new BorderLayout();
				this.getContentPane().setLayout(bLayout);
				this.setTitle("Privatūs raktai");
				this.setBounds(0, 0, 450, 110);	
				this.setResizable(false);
				this.setLocationRelativeTo(null);
				getContentPane().add(contentPanel, BorderLayout.PAGE_START);
				
				privateKeyList = new JComboBox();
				privateKeyList.setPreferredSize(new Dimension(420, 25));
				
				while(aliases.hasMoreElements())
					{
						String objAlias = aliases.nextElement();
						privateKeyList.addItem(objAlias);
					}
				
				contentPanel.add(privateKeyList);
				
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
				getContentPane().add(buttonPane, BorderLayout.PAGE_END);
				
				okButton = new JButton("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				cancelButton = new JButton("Atšaukti");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
				
				this.setModal(true);
				this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				this.setVisible(true);
			}

		@Override
		public void actionPerformed(ActionEvent e)
			{
				this.result = null;
			
				if(e.getSource() == okButton)
					{			
						this.result	= this.privateKeyList.getSelectedItem().toString();
					}		
				
				this.dispose();
			}
		
		public String getAlias()
			{
				return this.result;
			}
	}
