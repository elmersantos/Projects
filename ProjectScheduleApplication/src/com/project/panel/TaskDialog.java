package com.project.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.project.domain.Project;
import com.project.domain.Task;
import com.project.sql.TaskDAO;
import com.toedter.calendar.JDateChooser;

public class TaskDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTaskName;
	private JDateChooser jDate_From;
	private JDateChooser jDate_To;
	private JLabel lblTaskDuration;
	private JLabel lblProjectName;
	private JList lstDependencyTasks;
	private DefaultListModel listModel;
	private boolean isTaskUpdated = false;
	private Task updatedTask = null;
	private Project project;
	private long duration = 1;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*TaskDialog dialog = new TaskDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TaskDialog(Project project) {
		this.setProject(project);
		
		setBounds(100, 100, 695, 478);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Project");
		lblNewLabel.setBounds(15, 16, 69, 20);
		contentPanel.add(lblNewLabel);
		
		lblProjectName = new JLabel(project.getProjName());
		lblProjectName.setBounds(140, 16, 468, 20);
		contentPanel.add(lblProjectName);
		
		JLabel lblNewLabel_1 = new JLabel("Task Name");
		lblNewLabel_1.setBounds(15, 52, 96, 20);
		contentPanel.add(lblNewLabel_1);
		
		txtTaskName = new JTextField();
		txtTaskName.setBounds(140, 49, 479, 26);
		contentPanel.add(txtTaskName);
		txtTaskName.setColumns(10);
		
		lblTaskDuration = new JLabel("1 Day(s)");
		lblTaskDuration.setBounds(137, 161, 482, 20);
		contentPanel.add(lblTaskDuration);
		
		JLabel lblNewLabel_2 = new JLabel("Start Date");
		lblNewLabel_2.setBounds(15, 89, 96, 20);
		contentPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("End Date");
		lblNewLabel_3.setBounds(15, 125, 69, 20);
		contentPanel.add(lblNewLabel_3);
		
		jDate_From = new JDateChooser();
	    jDate_From.setDateFormatString("MM/dd/yyyy");
	    jDate_From.setDate(new java.util.Date());
	    jDate_From.setBounds(164, 89, 300, 26);
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
		jDate_To.setBounds(164, 125, 300, 26);
		contentPanel.add(jDate_To);
		
		
		JLabel lblNewLabel_4 = new JLabel("Duration");
		lblNewLabel_4.setBounds(15, 161, 69, 20);
		contentPanel.add(lblNewLabel_4);
		
		
		JLabel lblDependentOnTask = new JLabel("Select Tasks To Be Dependent On :");
		lblDependentOnTask.setBounds(15, 197, 608, 20);
		contentPanel.add(lblDependentOnTask);
		
		getProjectTasks(project.getProjID(), null);
		lstDependencyTasks = new JList(listModel);
		lstDependencyTasks.setVisibleRowCount(5);
		lstDependencyTasks.setLayoutOrientation(JList.VERTICAL);

		JScrollPane listScrollPane = new JScrollPane(lstDependencyTasks);
		listScrollPane.setBounds(25, 233, 594, 96);
		contentPanel.add(listScrollPane);
		
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
	}
	
	public boolean isTaskUpdated() {
		return isTaskUpdated;
	}
		
	public Task getUpdatedTask() {
		return updatedTask;
	}

	public void setProject(Project project) {
		this.project = project;
		//lblProjectName.setText(project.getProjName());
		//getProjectTasks(project.getProjID(), null);
	}
	
	private void jBtn_CancelActionPerformed(java.awt.event.ActionEvent evt) {
		isTaskUpdated = false;
        this.setVisible(false);
    }
    
    private void jBtn_OKActionPerformed(java.awt.event.ActionEvent evt) {
    	if(txtTaskName.getText().trim().equals(""))       
         { JOptionPane.showMessageDialog(null,"Task name cannot be empty",        
                                         "Task Update",JOptionPane.WARNING_MESSAGE);
         txtTaskName.requestFocusInWindow();  
        } else if(jDate_From.getDate().after(jDate_To.getDate())) { 
    		JOptionPane.showMessageDialog(null,"Invalid Task Date",
                    "Task Update",JOptionPane.ERROR_MESSAGE); 
    		jDate_From.requestFocusInWindow();
        } else if(jDate_From.getDate().before(project.getStartDate())) { 
        	JOptionPane.showMessageDialog(null,"Task Start Date should not be earlier than the Project Date",
        			"Task Update",JOptionPane.ERROR_MESSAGE); 
        	jDate_From.requestFocusInWindow();
        } else {
        	Task dependentTask = getTaskScheduleDependent();
        	if(dependentTask != null) {
        		SimpleDateFormat formatter = new SimpleDateFormat ("MMMM dd, yyyy", Locale.US); 
        		JOptionPane.showMessageDialog(null,"Task Star Date should not be earlier than '" + 
        				dependentTask.getTaskName() + "' task End Date (" + formatter.format(dependentTask.getEndDate()) + ")",
                        "Task Update",JOptionPane.ERROR_MESSAGE); 
        		jDate_From.requestFocusInWindow();
        	} else {
        		if(jDate_To.getDate().after(project.getEndDate())) {
        			 if (JOptionPane.showConfirmDialog(null,"Project's End Date will be updated to the " +
        					 "Task's End Date value. Continue ? ",
        	                 "Task",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        				 updateTask(true);
        			 }
        		} else {
        			updateTask(false);
        		}
        	}	
    	}
    }
    
    private void getProjectTasks(Long projectID, Long taskID) {
    	listModel = new DefaultListModel();
    	TaskDAO taskDAO = new TaskDAO();
		List<Task> tasks = taskDAO.getProjectTasks(projectID, null);
		Iterator<Task> it = tasks.iterator();
		while(it.hasNext()) {
			listModel.addElement(it.next());
		}
		//lstDependencyTasks.setModel(listModel);
    }
    
    private Task getTaskScheduleDependent() {
    	Task dependentTask = null;
    	Date startDate = jDate_From.getDate();
    	List<Task> dependentTasks = lstDependencyTasks.getSelectedValuesList();
    	if(!dependentTasks.isEmpty()) {
    		Iterator<Task> it = dependentTasks.iterator();
    		while(it.hasNext()) {
    			Task task = it.next();
    			if(startDate.before(task.getEndDate())) {
    				dependentTask = task;
    				break;
    			}
    		}
    	}
    	
    	return dependentTask;
    }
    
    private void updateDuration() {
		Date startDate = jDate_From.getDate();
    	Date endDate = jDate_To.getDate();
        duration = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS) + 1; 
        lblTaskDuration.setText(duration + " Day(s)");
    }
    
    private boolean updateTask(boolean isUpdateProjectEndDate) {
    	Date startDate = jDate_From.getDate();
    	Date endDate = jDate_To.getDate();
    	
    	Task task = new Task(this.project.getProjID(), txtTaskName.getText().trim());
    	task.setStartDate(startDate);
    	task.setEndDate(endDate);
    	
    	TaskDAO dao = new TaskDAO();
    	updatedTask = dao.addTask(task, lstDependencyTasks.getSelectedValuesList(), isUpdateProjectEndDate);
    	        
    	isTaskUpdated = true;
        this.setVisible(false);
        return isTaskUpdated;
    }
}
