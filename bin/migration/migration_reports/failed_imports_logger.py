from .logging_utils import configure_logging
from .sql_queries import logger_queries

class FailedImportsLogger:
    def __init__(self, connection):
        self.connection = connection
        self.cursor = connection.cursor()
        self.log = configure_logging()

    def execute_query(self, query, values=None):
        try:
            if values:
                self.cursor.execute(query, values)
            else:
                self.cursor.execute(query)
            self.connection.commit()
        except Exception as e:
            self.log.error(f"Error executing query: {e}, Query: {query}, Values: {values}", exc_info=True)

    def create_table(self, table_name):
        self.execute_query(logger_queries['create_table_query'] % table_name)

    def mark_migrated(self, table_name, id):
        update_query = logger_queries['update_query'] % (table_name, id)
        self.execute_query(update_query)
        self.connection.commit()

    def check_existing_entry(self, table_name, table_id , case_id, recording_id):
        self.execute_query(logger_queries['existing_record_query'], (table_name, table_id, case_id, recording_id))
        result = self.cursor.fetchone()
        if result is not None:
            return result[0]
        else:
            return None 

    def log_failed_imports(self, failed_imports):
        for failed_import in failed_imports:
            table_name = failed_import.get('table_name')
            table_id = failed_import.get('table_id') 
            case_id = failed_import.get('case_id') 
            recording_id = failed_import.get('recording_id') 
            details = failed_import.get('details') or 'None'

            values = (table_name, table_id, case_id, recording_id, details)
            if not self.check_existing_entry(table_name, table_id, case_id, recording_id):
                self.execute_query(logger_queries['insert_query'], values)

    