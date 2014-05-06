package es.uniovi.computadores.mensajes;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ASTARTNotificationMessage extends NotificationMessage {

	public static final String SUBTYPE = "start";
	private static final String MATRIX_TAG = "matrix";
	private static final String ROW_TAG = "row";
	private static final int SQUARED_MATRIX_COUNT_MIN = 4;
	private static final int SQUARED_MATRIX_COUNT_MAX = 5;
	
	private Character [][] mMatrix;
	
	public ASTARTNotificationMessage(Character [][] matrix) {
		super(SUBTYPE);
		if (matrix == null) {
			throw new IllegalArgumentException("The matrix cannot be null");			
		}
		testValid(matrix);
		mMatrix = matrix;
	}
	
	ASTARTNotificationMessage(JSONObject params) {
		super(SUBTYPE);
		if (params == null) {
			throw new IllegalArgumentException("Parameters are required");
		}
		parseParams(params);
	}
	
	public Character [][] getMatrix() {
		return mMatrix;
	}
			
	@Override
	@SuppressWarnings("unchecked")
	protected JSONObject toJSONParams() {
		JSONObject params = new JSONObject();
		JSONArray matrix = new JSONArray();
		for (int i = 0; i < mMatrix.length; i++) {
			JSONObject row = new JSONObject();			
			ArrayList<String> list = new ArrayList<String>(mMatrix[i].length);
			for (int j = 0; j < mMatrix[i].length; j++) {
				list.add(String.valueOf(mMatrix[i][j]));
			}
			row.put(ROW_TAG, list);
			matrix.add(row);
		}		
		params.put(MATRIX_TAG, matrix);
		return params;
	}
	
	private void parseParams(JSONObject params) {
		JSONArray jsonArray = (JSONArray) params.get(MATRIX_TAG);
		if (jsonArray == null) {
			throw new IllegalArgumentException("The matrix is missing");
		}
		int rowCount = jsonArray.size();
		if ((rowCount < SQUARED_MATRIX_COUNT_MIN) || (rowCount > SQUARED_MATRIX_COUNT_MAX)) {
			throw new IllegalArgumentException("Invalid row count");
		}
		Character [][] matrix = new Character[rowCount][rowCount];
		for (int i = 0; i < rowCount; i++) {
			JSONObject rowObject = (JSONObject) jsonArray.get(i);
			JSONArray row = (JSONArray) rowObject.get(ROW_TAG);
			if (row == null) {
				throw new IllegalArgumentException("A row is missing");
			}
			if (row.size() != rowCount) {
				throw new IllegalArgumentException("Invalid column count");
			}
			for (int j = 0; j < rowCount; j++) {
				String letter = (String) row.get(j);
				if (letter.length() != 1) {
					throw new IllegalArgumentException("The matrix can only contain letters");
				}
				matrix[i][j] = letter.charAt(0);
			}
		}
		mMatrix = matrix;
	}
	
	private static void testValid(Character [][] matrix) {
		int rowCount = matrix.length;
		if ((rowCount < SQUARED_MATRIX_COUNT_MIN) || (rowCount > SQUARED_MATRIX_COUNT_MAX)) {
			throw new IllegalArgumentException("Invalid row count");
		}
		for (int i = 0; i < rowCount; i++) {
			if (matrix[i].length != rowCount) {
				throw new IllegalArgumentException("Invalid column count");
			}
		}
	}
}
