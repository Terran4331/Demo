// QjCsv.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "Csv.h"

int _tmain(int argc, _TCHAR* argv[])
{
	CCsv csv;
	csv.LoadFromFile(L"C:\\Users\\jinqijiang.SOGOU-INC\\Desktop\\test1.csv");
	//csv.Insert(L"∂Ã–≈¿‡–Õ");
	csv.Insert(L"∞°∞°,,");
	csv.Insert(L"2, ∞°,∞°\"");
	csv.Insert(L"  , \"∞°\"∞°3");
	csv.Insert(L" ,∞°\r\n,∞°  \"\"\" 4∞°");
	csv.Insert(L"5\r\n");/**/
	csv.Insert(L"\r\n\r\n");/**/
	csv.Insert(L"");/**/
	csv.Save2File(L"C:\\Users\\jinqijiang.SOGOU-INC\\Desktop\\test2.csv");

	return 0;
}

