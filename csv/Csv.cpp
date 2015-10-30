#include "stdafx.h"
#include <Windows.h>
#include "Csv.h"
#include <assert.h>
#include <fstream>
#include <sstream>
#include <string>
#include "StringConv.h"

using std::fstream;
using std::wfstream;
using std::stringstream;
using std::ios;
using std::string;
using std::wstring;

void CCsv::Insert(const wchar_t *buf)
{
	wchar_t *cell = NULL;
	if (buf)
	{
		int len = wcslen(buf);
		if (len > 0)
		{
			cell = new wchar_t[len + 1];
			wcscpy_s(cell, len + 1, buf);
		}
	}
	int nCount = m_data.size();
	m_data.resize(nCount + 1);
	m_data[nCount] = cell;

}

void CCsv::LoadFromFile(const wchar_t *sFile)
{
	assert(sFile);

	fstream fs;
	fs.open(sFile, ios::in | ios::binary);
	if (!fs.fail())
	{
		stringstream ss;
		ss << fs.rdbuf();
		string sContent = ss.str();
		if (sContent.length()>=3)
		{
			BYTE cG = sContent[0], cB = sContent[1], cK = sContent[2];
			if (cG == 0xEF && cB == 0xBB && cK == 0xBF)
			{
				sContent.erase(0, 3);
			}
		}
		wstring wsContent = Ascii2Wide(sContent, CP_UTF8);
		const wchar_t* dump = wsContent.c_str();
		sContent.clear();

		if (dump)
		{
			int row = 0;
			bool bCell = false, bDelim = false, bEscp = false, bAppend = false;
			wstring sData = L"";
			const wchar_t* crt = dump;
			while (*crt)
			{
				bAppend = false;

				wchar_t tmp = *crt, ccccc = L'\"';
				if (tmp == L',')
				{
					if (bCell && (bEscp || !bDelim))
					{
						bCell = false;
						bDelim = false;
					}

					if (bCell)
					{
						bAppend = true;
					}
					else
					{
						Insert(sData.empty()?NULL:sData.c_str());
						sData.clear();
					}

					bEscp = false;
				} 
				else if (tmp == L'\"')
				{
					if (bCell)
					{
						if (bEscp)
						{
							bAppend = true;
							bEscp = false;
						} 
						else
						{
							bEscp = true;
						}
					}
					else
					{
						int count = 1;
						const wchar_t *pos = crt+1;
						while (*pos == L'\"')
						{
							count++;
							pos++;
						}
						if (count%2 != 0)
						{
							bDelim = true;
						}
						else
						{
							bDelim = false;
							bEscp = true;
						}
						bCell = true;
					}
				}
				else
				{
					if (bCell && bEscp)
					{
						bCell = false;
						bDelim = false;
					}

					if (!bCell)
					{
						bCell = true;
						bDelim = false;
					}
					bAppend = true;
					bEscp = false;
				}

				if (bAppend)
				{
					sData.append(1, tmp);
				}

				crt++;
			}

			if (!sData.empty())
			{
				Insert(sData.c_str());
				sData.clear();
			}
		}

		ss.clear();
		fs.close();
	}
}

void CCsv::Save2File(const wchar_t *sFile)
{
	assert(sFile);

	fstream fs;
	fs.open(sFile, ios::out | ios::binary);
	if (!fs.fail())
	{
		wstring wsContent = L"";

		int nCount = m_data.size();
		for (int idx = 0; idx < nCount; idx++)
		{
			const wchar_t* data = m_data[idx];
			if (data)
			{
				bool bExistDelim = wcschr(data, L',');
				if (bExistDelim)
				{
					wsContent.push_back(L'\"');
				}

				const wchar_t* crt = data;
				while(*crt)
				{
					if (*crt == L'\"')
					{
						wsContent.push_back(L'\"');
					} 
					wsContent.push_back(*crt);
					crt++;
				}

				if (bExistDelim)
				{
					wsContent.push_back(L'\"');
				}
			}

			if (idx < nCount - 1)
			{
				wsContent.push_back(L',');
			}
		}

		string strContent = Wide2Ascii(wsContent, CP_UTF8);
		char szBGK[3] = {0xEF,0xBB,0xBF};
		fs.write(szBGK, 3);
		fs << strContent;
		fs.close();
	}
}
