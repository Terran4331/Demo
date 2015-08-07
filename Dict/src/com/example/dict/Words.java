package com.example.dict;

import android.net.Uri;
import android.provider.BaseColumns;

public class Words {
	public static final String AUTHORITY = "org.crazyit.providers.dictprovider"; 	

	public static final class Word implements BaseColumns
	{
		//定义Content所允许操作的三个数据列
		public final static String _ID = "_id"; 
		public final static String WORD = "_id"; 
		public final static String DETAIL = "_id";
		
		//定义该Content提供服务的两个Uri
		public final static Uri DICT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/words");
		public final static Uri WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/word");
	}
}
