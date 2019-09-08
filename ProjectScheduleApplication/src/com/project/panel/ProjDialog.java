package com.project.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.project.domain.Project;
import com.project.sql.ProjectDAO;
import com.toedter.calendar.JDateChooser;

public class ProjDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtProjName;
	private JDateChooser jDate_From;
	private JDateChooser jDate_To;
	private JLabel lblDuration;
	private boolean isProjUpdated = false;
	private Long projID;
	private Project updatedProject = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*ProjDialog dialog = new ProjDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);*/
     	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProjDialog(Long projID) {
		this.projID = projID;
		setTitle("Project");
		setBounds(100, 100, 738, 273);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel("Project Name");
			label.setBounds(31, 16, 114, 20);
			contentPanel.add(label);
		}
		{
			txtProjName = new JTextField();
			txtProjName.setText((String) null);
			txtProjName.setColumns(10);
			txtProjName.setBounds(160, 13, 400, 26);
			contentPanel.add(txtProjName);
		}
		{
			JLabel lblStart = new JLabel("Start Date");
			lblStart.setBounds(31, 52, 114, 20);
			lblStart.setLabelFor(jDate_From);
			contentPanel.add(lblStart);
		}
		{
			lblDuration = new JLabel("1 Day(s)");
			lblDuration.setBounds(182, 124, 387, 20);
			contentPanel.add(lblDuration);
		}
		{
			jDate_From = new JDateChooser();
		    jDate_From.setDateFormatString("MM/dd/yyyy");
		    jDate_From.setDate(new java.util.Date());
		    jDate_From.setBounds(164, 52, 300, 26);
		    jDate_From.getSpinner().addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					updateDuration();
				}
			});
	        jDate_From.addKeyListener(new java.awt.event.KeyAdapter() {
	            public void keyPressed(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	            public void keyReleased(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	            public void keyTyped(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	        });
	        contentPanel.add(jDate_From);
		}
		{
			jDate_To = new JDateChooser();
			jDate_To.getSpinner().addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					updateDuration();
				}
			});
			jDate_To.addKeyListener(new java.awt.event.KeyAdapter() {
	            public void keyPressed(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	            public void keyReleased(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	            public void keyTyped(java.awt.event.KeyEvent evt) {
	            	updateDuration();
	            }
	        });
			jDate_To.setDateFormatString("MM/dd/yyyy");
			jDate_To.setDate(new java.util.Date());
			jDate_To.setBounds(164, 88, 300, 26);
			contentPanel.add(jDate_To);
		}
		{
			JLabel lblEnd = new JLabel("End Date");
			lblEnd.setBounds(31, 88, 69, 20);
			lblEnd.setLabelFor(jDate_To);
			contentPanel.add(lblEnd);
		}
		{
			JLabel label = new JLabel("Duration");
			label.setBounds(31, 124, 69, 20);
			contentPanel.add(label);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						jBtn_OKActionPerformed(arg0);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						jBtn_CancelActionPerformed(arg0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		//this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//this.setVisible(true);
	}
	
	private void updateDuration() {
		Date startDate = jDate_From.getDate();
    	Date endDate = jDate_To.getDate();
        long duration = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS) + 1; 
        lblDuration.setText(duration + " Day(s)");
    }
	
	public boolean isProjectUpdated() {
		return isProjUpdated;
	}

	public void setProjID(Long projID) {
		this.projID = projID;
	}
	
	private void jDate_FromKeyTyped(java.awt.event.KeyEvent evt) { }

    private void jDate_FromKeyReleased(java.awt.event.KeyEvent evt) { }

    private void jDate_FromKeyPressed(java.awt.event.KeyEvent evt) { }
    
    private void jBtn_CancelActionPerformed(java.awt.event.ActionEvent evt) {
    	isProjUpdated = false;
        this.setVisible(false);
    }
    
    private void jBtn_OKActionPerformed(java.awt.event.ActionEvent evt) {
    	if(txtProjName.getText().trim().equals(""))       
         { JOptionPane.showMessageDialog(null,"Project name cannot be empty",        
                                         "Project Update",JOptionPane.WARNING_MESSAGE);
         txtProjName.requestFocusInWindow();  
        } else if(jDate_From.getDate().after(jDate_To.getDate())) { 
    		JOptionPane.showMessageDialog(null,"Invalid Project Date",
                                                "Project Update",JOptionPane.ERROR_MESSAGE); 
    		jDate_From.requestFocusInWindow();
    	} else {
    		updateProject();
    	}
    }
    
    private boolean updateProject() {
    	String projName = txtProjName.getText().trim();
    	Date startDate = jDate_From.getDate();
    	Date endDate = jDate_To.getDate();
        //long duration = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS) + 1; 
    	//lblDuration.setText(duration + " Day(s)");
    	
    	Project project = new Project();
    	project.setProjName(projName);
    	project.setStartDate(startDate);
    	project.setEndDate(endDate);
    	
    	ProjectDAO dao = new ProjectDAO();
    	this.updatedProject =  dao.addProject(project);
        
    	isProjUpdated = true;
        this.setVisible(false);
        return isProjUpdated;
    }

	public Project getUpdatedProject() {
		return updatedProject;
	}

}
