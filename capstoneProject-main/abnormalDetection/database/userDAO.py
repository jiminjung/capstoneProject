from database.database_connector import DatabaseConnection
import logging

class UserDAO():
    def __init__(self,):
        self.connector = DatabaseConnection()
        self.logger = logging.getLogger()

    def userSelect(self, ip):
        try:
            conn = self.connector.getConnection()
            cursor = conn.cursor()

            cursor.execute("SELECT * FROM subproducts WHERE ip = ?", (ip,))
            for (id, name, age, phone_number, ip) in cursor:
                print(f"id: {id}, name: {name}, age: {age}, phone_number: {phone_number}, ip: {ip}")
        except Exception as e:
            self.logger.error(e)
        finally:
            conn.close()


    def getToken(self, userid):
        try:
            conn = self.connector.getConnection()
            cursor = conn.cursor()

            cursor.execute("SELECT * FROM products WHERE id = ?", (userid,))

            for (id, password, token) in cursor:
                userToken = token
        except Exception as e:
            self.logger.error(e)
        finally:
            conn.close()

        return userToken