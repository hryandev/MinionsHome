package com.rex.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "USERS")
public class User {

	@Id
	@Column(name = "USER_ID")
	private String id;

	@Column(name = "USER_PWD")
	private String pwd;

	@Column(name = "USER_PRENOM")
	private String prenom = "";

	@Column(name = "USER_NOM")
	private String nom = "";

	@Column(name = "USER_MAIL")
	private String mail = "";
	
	public User() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
