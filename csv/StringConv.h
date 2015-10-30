#pragma once
#include <string>
// CStringConv
//
//		Utility class to convert strings between UNICODE and ANSI
//

class CStringConv
{
public:
	CStringConv(void);
	CStringConv(const char* pszA);
	CStringConv(const wchar_t* pszW);
	CStringConv(CStringConv &str);
	~CStringConv(void);

	bool	SetString	(const char*	pszA);
	bool	SetString	(const wchar_t* pszW);

	const char*		GetStringA(UINT nCodePage = CP_ACP);
	const wchar_t*	GetStringW( UINT nCodePage = CP_ACP);
	const TCHAR* GetStringT();
	size_t	GetLength()
	{
		if(m_pszA) return strlen(m_pszA);
		if(m_pszW) return wcslen(m_pszW);
		return 0;
	}

	operator const char*	()	{ return GetStringA(); }
	operator const wchar_t* ()	{ return GetStringW(); }

	CStringConv& operator = (const char *pszA);
	CStringConv& operator = (const wchar_t *pszW);
	CStringConv& operator = (CStringConv &str);

	void	Clear();

private:
	char*		m_pszA;
	wchar_t*	m_pszW;
};

std::wstring Ascii2Wide(const std::string &src, UINT nCodePage = CP_ACP);
std::string Wide2Ascii(const std::wstring &src, UINT nCodePage = CP_ACP);
