import mariadb
import sys

class DatabaseConnection(object):
    def __init__(self):
        self.conn = None
        self.cur = None

    def getConnection(self):
        try:
            self.conn = mariadb.connect(
                user="Software",
                password="software",
                host="210.117.128.200",
                port=3306,
                database="userInfo"
            )
        except mariadb.Error as e:
            print(f"Error connecting to MariaDB Platform: {e}")
            sys.exit(1)

        return self.conn
