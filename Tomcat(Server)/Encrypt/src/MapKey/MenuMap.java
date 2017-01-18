package MapKey;

public interface MenuMap {
	String GET_MENU_FILE_LIST_KEY = "get_menu_file_list_key";
	String SET_ENCRYPT_FILE_KEY = "set_encrypt_file_key";
	String SET_ENCRYPT_FILE_SUCCESS_KEY = "set_encrypt_file_success_key";
	String ENCRYPT_FILE_KEY = "encrypt_file_key";

	String UPLOAD_FILE_ID_KEY = "upload_file_id_key";
String DOWNLOAD_FILE_KEY="download_file_key";

	String GET_FILE_NAME_KEY = "get_file_name_key";
	String FILE_PROGRESS_KEY = "file_progress_key";
	String FILE_PROGRESS_CODE_KEY = "file_progress_code_key";
	String UPLOAD_FILE_PROGRESS_KEY = "upload_file_progress_key";
	String UPLOAD_FILE_PROGRESS_FINISH = "{\"upload_file_progress_key\":\"100\"}";
	String UPLOAD_FILE_PROGRESS_START = "{\"upload_file_progress_key\":\"0\"}";

}
