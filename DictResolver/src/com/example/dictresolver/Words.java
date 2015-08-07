package com.example.dictresolver;

import android.net.Uri;
import android.provider.BaseColumns;

public class Words {
	public static final String AUTHORITY = "org.crazyit.providers.dictprovider"; 	

	public static final class Word implements BaseColumns
	{
		//����Content���������������������
		public final static String _ID = "_id"; 
		public final static String WORD = "_id"; 
		public final static String DETAIL = "_id";
		
		//�����Content�ṩ���������Uri
		public final static Uri DICT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/words");
		public final static Uri WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/word");
	}
}
