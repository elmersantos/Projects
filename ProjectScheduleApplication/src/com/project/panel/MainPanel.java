package com.project.panel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.project.domain.Project;
import com.project.domain.Task;
import com.project.sql.DerbyConnection;
import com.project.sql.ProjectDAO;
import com.project.sql.TaskDAO;

public class MainPanel {

	private JFrame frame;
	private JComboBox cboProject;
	private JComboBox cboTasks;
	private JComboBox cboDependentTasks;
	private JLabel lblProjSD;
	private JLabel lblProjED;
	private JLabel lblProjDuration;
	private JLabel lblTaskSD;
	private JLabel lblTaskED;
	private JLabel lblTaskDuration;
	private JLabel lblDepSD;
	private JLabel lblDepED;
	private JLabel lblDepDuration;
	private JButton btnAddTask;
	//private JButton btnEditTask;
	private JButton btnRemoveDepTask;
	private SimpleDateFormat formatter = new SimpleDateFormat ("EEEE MMMM dd, yyyy", Locale.US);  
	
	Dimension screenSize = new Dimension();
	int mainWidth = 0;
	int mainHeight = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DerbyConnection.connect();
					MainPanel window = new MainPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainPanel() {
		initialize();
		mainWidth = screenSize.width;
	    mainHeight = screenSize.height;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		centerContainer(frame, 906, 631);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Project");
		lblNewLabel.setBounds(39, 28, 69, 20);
		frame.getContentPane().add(lblNewLabel);
		
		cboProject = new JComboBox();
		cboProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cbo = (JComboBox) arg0.getSource();
				Project proj = (Project) cbo.getSelectedItem();
				showProjectDetails(proj);
			}
			
		});
		cboProject.setBounds(152, 25, 445, 36);
		frame.getContentPane().add(cboProject);
		
		JButton btnNewProj = new JButton("Add Project");
		btnNewProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showProjDialog();
			}
		});
		btnNewProj.setBounds(612, 32, 115, 29);
		frame.getContentPane().add(btnNewProj);
		
		JLabel lblNewLabel_1 = new JLabel("Start Date");
		lblNewLabel_1.setBounds(39, 87, 94, 20);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setBounds(39, 123, 69, 20);
		frame.getContentPane().add(lblEndDate);
		
		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(39, 159, 69, 20);
		frame.getContentPane().add(lblDuration);
		
		lblProjSD = new JLabel("");
		lblProjSD.setBounds(152, 87, 467, 20);
		frame.getContentPane().add(lblProjSD);
		
		lblProjED = new JLabel("");
		lblProjED.setBounds(152, 123, 474, 20);
		frame.getContentPane().add(lblProjED);
		
		lblProjDuration = new JLabel("");
		lblProjDuration.setBounds(152, 159, 454, 20);
		frame.getContentPane().add(lblProjDuration);
		
		JLabel lblNewLabel_2 = new JLabel("Project Tasks");
		lblNewLabel_2.setBounds(39, 241, 115, 20);
		frame.getContentPane().add(lblNewLabel_2);
		
		cboTasks = new JComboBox(); 
		cboTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cbo = (JComboBox) arg0.getSource();
				Task task = (Task) cbo.getSelectedItem();
				showTaskDetails(task);
			}
			
		});
		cboTasks.setBounds(152, 227, 486, 34);
		frame.getContentPane().add(cboTasks);
		
		btnAddTask = new JButton("Add Task");
		btnAddTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showTaskDialog((Project) cboProject.getSelectedItem());
			}
		});
		btnAddTask.setEnabled(false);
		btnAddTask.setBounds(653, 224, 115, 29);
		frame.getContentPane().add(btnAddTask);
		
		/*btnEditTask = new JButton("Edit Task");
		btnEditTask.setEnabled(false);
		btnEditTask.setBounds(653, 269, 115, 29);
		frame.getContentPane().add(btnEditTask);*/
		
		JLabel label = new JLabel("Start Date");
		label.setBounds(39, 291, 94, 20);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("End Date");
		label_1.setBounds(39, 320, 69, 20);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Duration");
		label_2.setBounds(39, 356, 69, 20);
		frame.getContentPane().add(label_2);
		
		lblTaskSD = new JLabel("");
		lblTaskSD.setBounds(130, 291, 467, 20);
		frame.getContentPane().add(lblTaskSD);
		
		lblTaskED = new JLabel("");
		lblTaskED.setBounds(121, 320, 474, 20);
		frame.getContentPane().add(lblTaskED);
		
		lblTaskDuration = new JLabel("");
		lblTaskDuration.setBounds(111, 356, 454, 20);
		frame.getContentPane().add(lblTaskDuration);
		
		JLabel lblTaskDependentOn = new JLabel("Task Dependent On");
		lblTaskDependentOn.setBounds(39, 431, 170, 20);
		frame.getContentPane().add(lblTaskDependentOn);
		
		cboDependentTasks = new JComboBox();
		cboDependentTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cbo = (JComboBox) arg0.getSource();
				Task task = (Task) cbo.getSelectedItem();
				showDependencyTaskDetails(task);
			}
		});
		cboDependentTasks.setBounds(189, 415, 486, 34);
		frame.getContentPane().add(cboDependentTasks);
		
		JLabel label_3 = new JLabel("Start Date");
		label_3.setBounds(39, 467, 94, 20);
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("End Date");
		label_4.setBounds(39, 492, 69, 20);
		frame.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("Duration");
		label_5.setBounds(39, 528, 69, 20);
		frame.getContentPane().add(label_5);
		
		lblDepSD = new JLabel("SD");
		lblDepSD.setBounds(130, 467, 377, 20);
		frame.getContentPane().add(lblDepSD);
		
		lblDepED = new JLabel("ED");
		lblDepED.setBounds(123, 492, 404, 20);
		frame.getContentPane().add(lblDepED);
		
		lblDepDuration = new JLabel("Duration");
		lblDepDuration.setBounds(123, 528, 346, 20);
		frame.getContentPane().add(lblDepDuration);
		
		btnRemoveDepTask = new JButton("Remove Dependency");
		btnRemoveDepTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeDependencyOnTask((Task)cboTasks.getSelectedItem(), 
						(Task)cboDependentTasks.getSelectedItem());
			}
		});
		btnRemoveDepTask.setEnabled(false);
		btnRemoveDepTask.setBounds(494, 465, 181, 29);
		frame.getContentPane().add(btnRemoveDepTask);
		
		getProjects();
	}
	
	private void removeDependencyOnTask(Task task, Task depTask) {
		 if (JOptionPane.showConfirmDialog(null,"Remove Dependency of task '" + task.getTaskName() + 
				 "' to task '" + depTask.getTaskName() + "' ?",
                 "Task",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			 TaskDAO taskDAO = new TaskDAO();
			 if(taskDAO.deleteDependencyTask(task.getTaskID(), depTask.getTaskID())) {
				 cboDependentTasks.removeItem(depTask); 
				 btnRemoveDepTask.setEnabled(cboDependentTasks.getItemCount() > 0);
			 };
		 }
             
	}
	
	private void getProjects() {
		ProjectDAO projDAO = new ProjectDAO();
		ArrayList<Project> projects = projDAO.getAllProjects();
		Iterator<Project> it = projects.iterator();
		while(it.hasNext()) {
			cboProject.addItem(it.next());
		}
		
		btnAddTask.setEnabled(cboProject.getItemCount() > 0);
	}
	
	private void showProjectDetails(Project project) {
		clearTaskDetails();
		clearDependencyTaskDetails();
		cboDependentTasks.removeAllItems();
		
		lblProjSD.setText(formatter.format(project.getStartDate()));
		lblProjED.setText(formatter.format(project.getEndDate()));
		lblProjDuration.setText(getDuration(project.getStartDate(), project.getEndDate()) + " Day(s)");
		showProjectTasks(project.getProjID());
	}
	
	private long getDuration(Date startDate, Date endDate) {
		long duration = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS) + 1;
		return duration;
	}
	
	private void showProjectTasks(Long projectID) {
		cboTasks.removeAllItems();
		TaskDAO taskDAO = new TaskDAO();
		List<Task> tasks = taskDAO.getProjectTasks(projectID, null);
		Iterator<Task> it = tasks.iterator();
		while(it.hasNext()) {
			cboTasks.addItem(it.next());
		}
		
		//btnEditTask.setEnabled(cboTasks.getItemCount() > 0);
	}
	
	private void showTaskDetails(Task task) {
		clearTaskDetails();
		if(task != null) {
			lblTaskSD.setText(formatter.format(task.getStartDate()));
			lblTaskED.setText(formatter.format(task.getEndDate()));
			lblTaskDuration.setText(getDuration(task.getStartDate(), task.getEndDate()) + " Day(s)");
			showDependencyTasks(task.getTaskID());
		}	
	}
	
	private void showDependencyTasks(Long taskID) {
		cboDependentTasks.removeAllItems();
		TaskDAO taskDAO = new TaskDAO();
		List<Task> tasks = taskDAO.getDependentTask(taskID);
		Iterator<Task> it = tasks.iterator();
		while(it.hasNext()) {
			cboDependentTasks.addItem(it.next());
		}
		boolean isNotEmpty = cboDependentTasks.getItemCount() > 0;
		btnRemoveDepTask.setEnabled(isNotEmpty);
	}
	
	private void showDependencyTaskDetails(Task task) {
		clearDependencyTaskDetails();
		if(task != null) {
			lblDepSD.setText(formatter.format(task.getStartDate()));
			lblDepED.setText(formatter.format(task.getEndDate()));
			lblDepDuration.setText(getDuration(task.getStartDate(), task.getEndDate()) + " Day(s)");
		}	
	}
	
	private void clearTaskDetails() {
		lblTaskSD.setText("");
		lblTaskED.setText("");
		lblTaskDuration.setText("");
	}
	
	private void clearDependencyTaskDetails() {
		lblDepSD.setText("");
		lblDepED.setText("");
		lblDepDuration.setText("");
	}
	
	 private void showProjDialog() {
	        ProjDialog proj = new ProjDialog(null);
	        centerContainer(proj, proj.getWidth(), proj.getHeight());
	        proj.setModal(true);
	        proj.setVisible(true); 
	        
	        if(proj.isProjectUpdated()) {
	        	Project updatedProject = proj.getUpdatedProject();
	        	cboProject.addItem(updatedProject);
	        	cboProject.setSelectedItem(updatedProject);
	        }
	  }
	 
	 private void showTaskDialog(Project project) {       
	        TaskDialog task = new TaskDialog(project);
	        centerContainer(task, task.getWidth(), task.getHeight());
	        task.setProject(project);
	        task.setModal(true);
	        task.setVisible(true); 
	        
	        if(task.isTaskUpdated()) {
	        	Task updatedTask = task.getUpdatedTask();
	        	if(updatedTask.getEndDate().after(project.getEndDate())) {
	        		((Project) cboProject.getSelectedItem()).setEndDate(updatedTask.getEndDate());
	        		lblProjED.setText(formatter.format(updatedTask.getEndDate()));
	        		lblProjDuration.setText(getDuration(project.getStartDate(), project.getEndDate()) + " Day(s)");
	        	}
	        	cboTasks.addItem(updatedTask);
	        	cboTasks.setSelectedItem(updatedTask);
	        }
	  }
	 
	 private static void centerContainer (Container frame, int width, int height) {
		 Dimension screenSize = new Dimension();
	     screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	     
		 frame.setBounds((int)((screenSize.getWidth()/2) - (width /2)), 
                 (int)((screenSize.getHeight()/2) - (height /2)),
                 width, height);
	 }
}
