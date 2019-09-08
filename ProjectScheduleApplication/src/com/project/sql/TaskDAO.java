package com.project.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import com.project.domain.Task;

public class TaskDAO {

	public List<Task> getDependentTask(long taskID) {
		List<Task> tasks = new ArrayList<Task>();
		String query = "select T.* from TASK_DEPENDENCY td " +
				"inner join TASK t " +
				"on td.taskDependentID = t.taskID " +
				"where td.taskID = " + taskID;
		
		try {
			Statement stmt = DerbyConnection.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Task task = new Task(rs.getString("taskName"));
				task.setProjectID(rs.getLong("projID"));
				task.setStartDate(rs.getDate("startDate"));
				task.setEndDate(rs.getDate("endDate"));
				task.setTaskID(rs.getLong("taskID"));
				
				tasks.add(task);
			}
			
			rs.close();
            stmt.close();
		} 
		catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        }
		
		return tasks;
	}
	
	public List<Task> getProjectTasks(Long projectID, Long taskID) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Task> tasks = new ArrayList<Task>();
		String query = "select * from TASK where projID = " + projectID;
		if(taskID != null) {
			query = query + " and taskID <> " + taskID;
		}
		
		try {
			stmt = DerbyConnection.getConnection().createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Task task = new Task(projectID, rs.getString("taskName"));
				task.setTaskID(rs.getLong("taskID"));
				task.setStartDate(rs.getDate("startDate"));
				task.setEndDate(rs.getDate("endDate"));
				
				tasks.add(task);
			}
			
			rs.close();
            stmt.close();
		} catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        }
		
		return tasks;
	}
	
	public Task addTask(Task task, List<Task> dependentTasks, boolean isUpdateProjectEndDate) {
		Connection conn = DerbyConnection.getConnection();
		Task newTask = null;
		PreparedStatement ps = null;
		String query = "insert into TASK (taskName, startDate, endDate, projID) " +
				"values (?, ?, ?, ?)";
		
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, task.getTaskName());
			ps.setDate(2, new java.sql.Date(task.getStartDate().getTime()));
			ps.setDate(3, new java.sql.Date(task.getEndDate().getTime()));
			ps.setLong(4, task.getProjectID());
			
			int insertResult = ps.executeUpdate();
			if(insertResult > 0) {
				try {
					ResultSet newID = ps.getGeneratedKeys();
					if(newID.next()) {
						task.setTaskID(newID.getLong(1));
						newTask = task;
					}
					newID.close();
					
					if(isUpdateProjectEndDate) {
						ps.close();
						query = "update PROJECT set endDate = ? where projID = ?";
						ps = conn.prepareStatement(query);
						ps.setDate(1, new java.sql.Date(task.getEndDate().getTime()));
						ps.setLong(2, task.getProjectID());
						ps.execute();
					}
					
					if(!dependentTasks.isEmpty()) {
						ps.close();
						int index = 1;
						StringBuilder query2 = new StringBuilder("insert into TASK_DEPENDENCY " +
								"(taskID, taskDependentID) values ");
						ListIterator<Task> it = dependentTasks.listIterator();
						while(it.hasNext()) {
							query2.append("(?, ?),"); 
							it.next();
						}
						query2.deleteCharAt(query2.length() - 1);
						ps = conn.prepareStatement(query2.toString());
						for(Task depTask : dependentTasks) {
							ps.setLong(index++, task.getTaskID());
							ps.setLong(index++, depTask.getTaskID());
						}
					}
					ps.executeUpdate();
					conn.commit();
					
				} catch (SQLException sqlExcept)  {
					try {
		                conn.rollback();
		            } catch (SQLException e1) {
		                System.err.println("There was an error making a rollback");
		            }
					System.out.println("error in generated keys");
					sqlExcept.printStackTrace();
				}
			}
			
		} catch (SQLException sqlExcept)  {
			try {
                conn.rollback();
            } catch (SQLException e1) {
                System.err.println("There was an error making a rollback");
            }
            sqlExcept.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
	        } catch (SQLException e) {
	            System.err.println("Error in setting auto commit");
	        }
        	if(ps != null) {
                try {
                     ps.close();
                } catch(SQLException ex) {
                      System.out.println("Could not close query");
                }
           }
        }
		
		return newTask;
	}
	
	public void addDependencyTask(long taskID, long taskDependentID) {
		String query = "insert into task_dependency (taskID, taskDependentID) " +
				"values (?, ?)";
		
		try {
			PreparedStatement ps = DerbyConnection.getConnection().prepareStatement(query);
			ps.setLong(1, taskID);
			ps.setLong(2, taskDependentID);
			ps.executeUpdate();
			
		} catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        }
	}
	
	public boolean deleteDependencyTask(long taskID, long taskDependentID) {
		String query = "delete from task_dependency where taskID = ? and taskDependentID = ?";
		int result = 0; 
		
		try {
			PreparedStatement ps = DerbyConnection.getConnection().prepareStatement(query);
			ps.setLong(1, taskID);
			ps.setLong(2, taskDependentID);
			result = ps.executeUpdate();
		} catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        } finally {
        	return (result > 0);
        }
	}
}
