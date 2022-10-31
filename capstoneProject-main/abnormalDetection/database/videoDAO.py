from database.database_connector import DatabaseConnection
import logging

class VideoDAO(object):
    def __init__(self,):
        self.connector = DatabaseConnection()
        self.logger = logging.getLogger()

    def insertValue(self, id, name, url, ip):
        sql = "INSERT INTO video (id, name, url, ip) VALUES(?, ?, ?, ?)"
        try:
            self.conn = self.connector.getConnection()
            cursor = self.conn.cursor()
            cursor.execute(sql, (id, name, url, ip))
        except Exception as e:
            self.logger.error(e)
        finally:
            self.conn.close()
