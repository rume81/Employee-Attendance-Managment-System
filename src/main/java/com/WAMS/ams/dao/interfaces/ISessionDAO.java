package com.WAMS.ams.dao.interfaces;

import com.webhawks.Hawks_model.Session;


/**
 * @author Monsur
 * 
 */
public interface ISessionDAO {
	
	public int getSessionLastId();
	
	public int insertSession(Session session)  throws Exception;
}
