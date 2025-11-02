#include <iostream>
#include <vector>
#include <string>
using namespace std;

// 简化版BM算法，只使用坏字符规则
int search(const string& text, const string& pattern) {
    int n = text.length();
    int m = pattern.length();
    
    // 构建坏字符规则表
    vector<int> badChar(256, -1);
    for (int i = 0; i < m; i++) {
        badChar[(unsigned char)pattern[i]] = i;
    }
    
    // 开始匹配
    int i = 0;
    while (i <= n - m) {
        int j = m - 1;
        
        // 从右向左匹配
        while (j >= 0 && pattern[j] == text[i + j]) {
            j--;
        }
        
        // 找到完全匹配
        if (j < 0) {
            return i;
        }
        
        // 计算坏字符规则的移动距离
        int badCharShift = max(1, j - badChar[(unsigned char)text[i + j]]);
        i += badCharShift;
    }
    
    return -1;
}

int main() {
    // 测试用例1：基本匹配
    string text1 = "hello world";
    string pattern1 = "world";
    cout << "测试1 - 查找'world'在'hello world'中的位置: " << search(text1, pattern1) << endl;
    
    // 测试用例5：BM算法优势场景
    string text5 = "GCATCGCAGAGAGTATACAGTACG";
    string pattern5 = "GCAGAGAG";
    cout << "测试5 - 查找模式串在文本串中的位置: " << search(text5, pattern5) << endl;
    
    return 0;
}