/**
 * Sunday字符串匹配算法实现
 * 时间复杂度：
 *   - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)
 *   - 平均情况: O(n)
 * 空间复杂度：O(1) - 使用固定大小的字符集
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <stdexcept>
using namespace std;

class SundayAlgorithm {
private:
    // 构建偏移表
    static unordered_map<char, int> buildShiftTable(const string& pattern) {
        int m = pattern.length();
        unordered_map<char, int> shift;
        
        // 对于模式串中的每个字符，记录它到模式串末尾的距离
        for (int i = 0; i < m; i++) {
            shift[pattern[i]] = m - i;
        }
        
        return shift;
    }

public:
    /**
     * Sunday字符串匹配算法
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     * @throws invalid_argument 如果输入参数为nullptr
     */
    static int search(const string& text, const string& pattern) {
        // 检查输入参数
        if (text.empty() && !pattern.empty()) {
            return -1;  // 空文本串不可能匹配非空模式串
        }
        if (pattern.empty()) {
            return 0;  // 空模式串匹配任何位置的开始
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return -1;  // 文本串比模式串短，不可能匹配
        }
        
        // 构建偏移表
        unordered_map<char, int> shift = buildShiftTable(pattern);
        
        // 开始匹配
        int i = 0;
        while (i <= n - m) {
            // 尝试匹配当前位置
            bool match = true;
            for (int j = 0; j < m; j++) {
                if (text[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            
            if (match) {
                return i;  // 找到匹配
            }
            
            // 计算下一次跳转的距离
            int nextPos = i + m;
            if (nextPos >= n) {
                break;  // 已经到达文本串末尾
            }
            
            // 根据下一个字符在模式串中的位置计算跳转距离
            char nextChar = text[nextPos];
            if (shift.find(nextChar) != shift.end()) {
                i += shift[nextChar];
            } else {
                i += m + 1;  // 不在模式串中的字符，跳过整个模式串+1
            }
        }
        
        return -1;  // 未找到匹配
    }
    
    /**
     * 查找模式串在文本串中所有出现的位置
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置的vector
     */
    static vector<int> searchAll(const string& text, const string& pattern) {
        vector<int> matches;
        
        // 检查输入参数
        if (text.empty() && !pattern.empty()) {
            return matches;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0) {
            // 空模式串匹配每个位置的开始
            for (int i = 0; i <= n; i++) {
                matches.push_back(i);
            }
            return matches;
        }
        
        if (n < m) {
            return matches;  // 无匹配
        }
        
        // 构建偏移表
        unordered_map<char, int> shift = buildShiftTable(pattern);
        
        int i = 0;
        while (i <= n - m) {
            bool match = true;
            for (int j = 0; j < m; j++) {
                if (text[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            
            if (match) {
                matches.push_back(i);
                // 找到匹配后，移动一个位置继续查找（可以优化为更大的跳转）
                i++;
            } else {
                int nextPos = i + m;
                if (nextPos >= n) {
                    break;
                }
                char nextChar = text[nextPos];
                if (shift.find(nextChar) != shift.end()) {
                    i += shift[nextChar];
                } else {
                    i += m + 1;
                }
            }
        }
        
        return matches;
    }
};

int main() {
    // 测试用例1：基本匹配
    string text1 = "hello world";
    string pattern1 = "world";
    cout << "测试1 - 查找'world'在'hello world'中的位置: " << SundayAlgorithm::search(text1, pattern1) << endl;  // 应该是6
    
    // 测试用例2：多次匹配
    string text2 = "abababa";
    string pattern2 = "aba";
    vector<int> results2 = SundayAlgorithm::searchAll(text2, pattern2);
    cout << "测试2 - 查找所有'aba'在'abababa'中的位置: ";
    for (int pos : results2) {
        cout << pos << " ";  // 应该是0 2 4
    }
    cout << endl;
    
    // 测试用例3：无匹配
    string text3 = "hello";
    string pattern3 = "world";
    cout << "测试3 - 查找'world'在'hello'中的位置: " << SundayAlgorithm::search(text3, pattern3) << endl;  // 应该是-1
    
    // 测试用例4：边界情况
    string text4 = "test";
    string pattern4 = "";
    vector<int> results4 = SundayAlgorithm::searchAll(text4, pattern4);
    cout << "测试4 - 查找空串在'test'中的所有位置: ";
    for (int pos : results4) {
        cout << pos << " ";  // 应该是0 1 2 3 4
    }
    cout << endl;
    
    // 测试用例5：特殊字符
    string text5 = "a!b@c#d$e%";
    string pattern5 = "c#d";
    cout << "测试5 - 查找'c#d'在特殊字符串中的位置: " << SundayAlgorithm::search(text5, pattern5) << endl;  // 应该是4
    
    return 0;
}