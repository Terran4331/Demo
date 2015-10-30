#include "StdAfx.h"
#include <atlbase.h>
#include <atlcom.h>
#include "StringConv.h"


// CStringConv::CStringConv
//
//		Default constructor
//
CStringConv::CStringConv(void)
: m_pszA(NULL)
, m_pszW(NULL)
{
}


// CStringConv::CStringConv
//
//		Constructs an object from an ANSI string
//
CStringConv::CStringConv(const char* pszA)
: m_pszA(NULL)
, m_pszW(NULL)
{
	SetString(pszA);
}


// CStringConv::CStringConv
//
//		Constructs an object from a UNICODE string
//
CStringConv::CStringConv(const wchar_t* pszW)
: m_pszA(NULL)
, m_pszW(NULL)
{
	SetString(pszW);
}


// CStringConv::CStringConv
//
//		Copy constructor (note that the arg is non const)
//
CStringConv::CStringConv(CStringConv &str)
{
	SetString(str.GetStringW());
}


// CStringConv::~CStringConv
//
//		Destructor
//
CStringConv::~CStringConv(void)
{
	Clear();
}


// CStringConv::operator =
//
//		Assigns an ANSI string
//

CStringConv& CStringConv::operator=( const char *pszA )
{
	SetString(pszA);
	return *(this);
}

// CStringConv::operator =
//
//		Assigns a UNICODE string
//
CStringConv& CStringConv::operator =(const wchar_t *pszW)
{
	SetString(pszW);
	return *(this);
}


// CStringConv::operator =
//
//		Copies two objects
//
CStringConv& CStringConv::operator =(CStringConv &str)
{
	if(this != &str)
		SetString(str.GetStringW());
	return *(this);
}



// CStringConv::Clear
//
//		Clears the internal buffers
//
void CStringConv::Clear()
{
	if(m_pszA != NULL)
		delete [] m_pszA;

	if(m_pszW != NULL)
		delete [] m_pszW;

	m_pszA = NULL;
	m_pszW = NULL;
}


// CStringConv::SetString
//
//		Sets a multi-byte character string
//
bool CStringConv::SetString(const char *pszA)
{
	if (pszA == NULL)
	{
		return false;
	}

	Clear();
	size_t nSize = strlen(pszA) + 1;

	m_pszA = new char[nSize];
	if(m_pszA == NULL)
		return false;

	memset(m_pszA, 0, nSize * sizeof(char));
	strcpy_s(m_pszA,nSize, pszA);

	return true;
}


// CStringConv::SetString
//
//		Sets a UNICODE character string
//
bool CStringConv::SetString(const wchar_t *pszW)
{
	if (pszW == NULL)
	{
		return false;
	}

	Clear();

	size_t nSize = wcslen(pszW) + 1;
	m_pszW = new wchar_t[nSize];
	if(m_pszW == NULL)
		return false;

	memset(m_pszW, 0, nSize*sizeof(wchar_t));
	wcscpy_s(m_pszW, nSize, pszW);

	return true;
}


// CStringConv::GetStringA
//
//		Gets the string as a muti-byte character string
//
const char* CStringConv::GetStringA(UINT nCodePage /*= CP_ACP*/)
{
	if(m_pszA == NULL)
	{
		int nSizeA = 0;
		int nSizeW = 0;
		int nConverted = 0;

		if(m_pszW == NULL || _tcslen(m_pszW) == 0)
		{
			return "";
		}

		nSizeW = (int)wcslen(m_pszW);
		nConverted = WideCharToMultiByte(nCodePage, 0,  m_pszW, nSizeW, NULL,0, NULL, NULL);
		if (0== nConverted)
		{
			return NULL;
		}
		nSizeA = nConverted + 1;

		m_pszA = new char[nSizeA];
		if(m_pszA == NULL)
		{
			return NULL;
		}

		memset(m_pszA, 0, nSizeA * sizeof(char));
		
		nConverted = WideCharToMultiByte(nCodePage, 0,  m_pszW, nSizeW, m_pszA, nSizeA, NULL, NULL);
		if (0== nConverted)
		{
			return NULL;
		}

	}

	return m_pszA;
}


// CStringConv::GetStringW
//
//		Gets ths string as a UNICODE string
//
const wchar_t* CStringConv::GetStringW(UINT nCodePage /*= CP_ACP*/)
{
	if(m_pszW == NULL)
	{
		int nSizeA = 0;
		int nSizeW = 0;
		int nConverted = 0;
		if(m_pszA == NULL || strlen(m_pszA) == 0)
		{
			return L"";
		}
		
		nSizeA = (int)strlen(m_pszA);
		nConverted = MultiByteToWideChar(nCodePage, 0,  m_pszA, nSizeA, NULL,0);
		if (0== nConverted)
		{
			return NULL;
		}
		nSizeW = nConverted + 1;
		m_pszW = new wchar_t[nSizeW];
		if(m_pszW == NULL)
			return NULL;

		memset(m_pszW, 0, nSizeW * sizeof(wchar_t));
		nConverted = MultiByteToWideChar(nCodePage, 0,  m_pszA, nSizeA, m_pszW, nSizeW);
		if (0 == nConverted)
		{
			return NULL;
		}

	}

	return m_pszW;
}

const TCHAR* CStringConv::GetStringT()
{
#ifdef UNICODE
	return GetStringW();
#else
	return GetStringA()
#endif // !UNICODE

}

std::wstring Ascii2Wide(const std::string &src, UINT nCodePage)
{
	CStringConv strConv(src.c_str());	
	return strConv.GetStringW(nCodePage);
}

std::string Wide2Ascii(const std::wstring &src, UINT nCodePage)
{
	CStringConv strConv(src.c_str());	
	return strConv.GetStringA(nCodePage);
}
