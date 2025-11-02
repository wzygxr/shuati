// LintCode 659. Encode and Decode Strings (字符串编码解码)
// 测试链接 : https://www.lintcode.com/problem/659/

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class LintCode659_EncodeDecodeStrings {
public:
    // 编码函数
    string encode(vector<string>& strs) {
        string encoded = "";
        
        for (string& str : strs) {
            // 格式：长度 + '#' + 字符串
            encoded += to_string(str.length()) + "#" + str;
        }
        
        return encoded;
    }
    
    // 解码函数
    vector<string> decode(string s) {
        vector<string> decoded;
        int i = 0;
        
        while (i < s.length()) {
            // 找到分隔符'#'
            int j = i;
            while (j < s.length() && s[j] != '#') {
                j++;
            }
            
            // 提取长度
            int length = stoi(s.substr(i, j - i));
            
            // 提取字符串
            string str = s.substr(j + 1, length);
            decoded.push_back(str);
            
            // 移动到下一个字符串的开始位置
            i = j + 1 + length;
        }
        
        return decoded;
    }
};

// 测试函数
int main() {
    LintCode659_EncodeDecodeStrings solution;
    
    // 测试用例1
    vector<string> strs1 = {"hello", "world"};
    string encoded1 = solution.encode(strs1);
    vector<string> decoded1 = solution.decode(encoded1);
    cout << "输入: ";
    for (string s : strs1) cout << s << " ";
    cout << endl;
    cout << "编码: " << encoded1 << endl;
    cout << "解码: ";
    for (string s : decoded1) cout << s << " ";
    cout << endl;
    cout << "期望: ";
    for (string s : strs1) cout << s << " ";
    cout << endl << endl;
    
    // 测试用例2
    vector<string> strs2 = {"", "abc", "def"};
    string encoded2 = solution.encode(strs2);
    vector<string> decoded2 = solution.decode(encoded2);
    cout << "输入: ";
    for (string s : strs2) cout << "\"" << s << "\" ";
    cout << endl;
    cout << "编码: " << encoded2 << endl;
    cout << "解码: ";
    for (string s : decoded2) cout << "\"" << s << "\" ";
    cout << endl;
    cout << "期望: ";
    for (string s : strs2) cout << "\"" << s << "\" ";
    cout << endl << endl;
    
    return 0;
}