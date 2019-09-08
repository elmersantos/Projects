package com.project.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.project.domain.Project;

public class ProjectDAO {
	
	public ArrayList<Project> getAllProjects() {
		TaskDAO taskDAO = new TaskDAO();
		ArrayList<Project> projects = new ArrayList<Project>();
		String query = "select * from PROJECT";
		 
		try {
			Statement stmt = DerbyConnection.getConnection().createStatement();
		    ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Project project = new Project(rs.getString("projName"));
				project.setProjID(rs.getLong("projID"));
				project.setStartDate(rs.getDate("startDate"));
				project.setEndDate(rs.getDate("endDate"));
				
				project.setTasks(taskDAO.getProjectTasks(project.getProjID(), null));
				projects.add(project);
			}
			
			rs.close();
            stmt.close();
		} 
		catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        }
		
		return projects;
	}
	
	public Project addProject(Project project) {
		Project updatedProject = null;
		PreparedStatement ps = null;
		String query = "insert into PROJECT (projName, startDate, endDate) " +
				"values (?, ?, ?)";
		
		try {
			ps = DerbyConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, project.getProjName());
			ps.setDate(2, new java.sql.Date(project.getStartDate().getTime()));
			ps.setDate(3, new java.sql.Date(project.getEndDate().getTime()));
			
			int insertResult = ps.executeUpdate();
			if(insertResult > 0) {
				ResultSet newID = ps.getGeneratedKeys();
				if(newID.next()) {
					project.setProjID(newID.getLong(1));
				}
				updatedProject = project;
				newID.close();
			}
			
			
		} catch (SQLException sqlExcept)  {
            sqlExcept.printStackTrace();
        } finally {
        	if(ps != null) {
                try {
                     ps.close();
                } catch(SQLException ex) {
                      System.out.println("Could not close query");
                }
           }
        }
		
		return updatedProject;
	}
}
