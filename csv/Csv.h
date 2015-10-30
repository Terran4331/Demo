#ifndef __CSV_H
#define __CSV_H
#include <vector>

using std::vector;

class CCsv
{
public:
	void LoadFromFile(const wchar_t *sFile);
	void Save2File(const wchar_t *sFile);
	void Insert(const wchar_t *buf);

private:
	vector<wchar_t *> m_data;
};

#endif
