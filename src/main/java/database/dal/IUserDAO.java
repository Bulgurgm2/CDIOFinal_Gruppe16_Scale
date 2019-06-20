/*
Forfatter: Lukas
Ansvar: Klassen er oprettet som interface og beskriver hvilke metoder der skal være i et "data access object" for brugere.
*/

package database.dal;

import database.dto.*;

import java.util.List;

public interface IUserDAO extends IDALException{
	//Create
	void createUser(IUserDTO user) throws DALException;
	//Read
	IUserDTO getUser(int userId) throws DALException;

	List<IUserDTO> getUserList() throws DALException;
	//Update
	void updateUser(IUserDTO user) throws DALException;

}