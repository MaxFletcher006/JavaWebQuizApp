package com.quizapp.Model;
//import com.quizapp.DOA.UserDAO; 

public class User {
	
	private String username = null ; 
	private String password = null ; 
	private String email = null ; 
	private String phone_number = null ; 
	
	public User(String username, String password, String email, String phone_number) {
		this.username = username ; 
		this.password = password ; 
		this.email = email ; 
		this.phone_number = phone_number ; 
	}
	
	public String getUsername() {
		return this.username ;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_number() {
		return this.phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		User user1 = new User("max","20020723","maxyboy@gmail.com","95237125") ;
//		UserDAO dao = new UserDAO() ;
//		
//		User user2 = new User("bill","0","0","0") ;
//		
//		System.out.println(dao.checkIfUserExisting(user1.getUsername(), user1.getEmail(), user1.getPhone_number()));
//		System.out.println(dao.login(user1.getUsername(), user1.getPassword()));
//		
//		System.out.println(dao.checkIfUserExisting(user2.getUsername(), user2.getEmail(), user2.getPhone_number())) ;
//	}
}
