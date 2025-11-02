#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <algorithm>

using namespace std;

/**
 * Fox and Names - 字典序建图与拓扑排序 - C++实现
 * 题目解析：通过字符串比较构建字母顺序图，使用拓扑排序判断合法性
 * 
 * 算法思路：
 * 1. 比较相邻字符串，找到第一个不同的字符建立顺序关系
 * 2. 构建有向图并进行拓扑排序
 * 3. 检测环的存在，输出结果
 * 
 * 时间复杂度：O(n * L)
 * 空间复杂度：O(1)，固定26个字母
 * 
 * 工程化考虑：
 * 1. 图构建：通过字符串比较建立关系
 * 2. 环检测：拓扑排序检测非法顺序
 * 3. 边界处理：前缀关系、空字符串等情况
 */
class Solution {
public:
    string alienOrder(vector<string>& words) {
        // 构建图：26个字母
        vector<vector<bool>> graph(26, vector<bool>(26, false));
        vector<int> indegree(26, 0);
        
        // 标记存在的字母
        vector<bool> exists(26, false);
        for (const string& word : words) {
            for (char c : word) {
                exists[c - 'a'] = true;
            }
        }
        
        // 比较相邻字符串，构建图
        for (int i = 0; i < words.size() - 1; i++) {
            const string& word1 = words[i];
            const string& word2 = words[i + 1];
            
            // 检查前缀关系
            if (word1.length() > word2.length() && 
                word1.substr(0, word2.length()) == word2) {
                return "Impossible";
            }
            
            // 找到第一个不同的字符
            int len = min(word1.length(), word2.length());
            for (int j = 0; j < len; j++) {
                char c1 = word1[j];
                char c2 = word2[j];
                
                if (c1 != c2) {
                    // 建立边 c1 -> c2
                    if (!graph[c1 - 'a'][c2 - 'a']) {
                        graph[c1 - 'a'][c2 - 'a'] = true;
                        indegree[c2 - 'a']++;
                    }
                    break; // 找到第一个不同字符后停止
                }
            }
        }
        
        // 拓扑排序
        queue<int> q;
        for (int i = 0; i < 26; i++) {
            if (exists[i] && indegree[i] == 0) {
                q.push(i);
            }
        }
        
        string result;
        int count = 0; // 已处理的字母数
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            result += (char)('a' + u);
            count++;
            
            for (int v = 0; v < 26; v++) {
                if (graph[u][v]) {
                    if (--indegree[v] == 0) {
                        q.push(v);
                    }
                }
            }
        }
        
        // 检查是否有环
        if (count != getExistCount(exists)) {
            return "Impossible";
        }
        
        return result;
    }
    
private:
    int getExistCount(const vector<bool>& exists) {
        int count = 0;
        for (bool exist : exists) {
            if (exist) count++;
        }
        return count;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<string> words1 = {"rivest", "shamir", "adleman"};
    cout << "测试用例1: " << solution.alienOrder(words1) << endl;
    
    // 测试用例2：存在环
    vector<string> words2 = {"abc", "ab"};
    cout << "测试用例2: " << solution.alienOrder(words2) << endl; // 输出: Impossible
    
    // 测试用例3：正常顺序
    vector<string> words3 = {"wrt", "wrf", "er", "ett", "rftt"};
    cout << "测试用例3: " << solution.alienOrder(words3) << endl;
    
    // 测试用例4：前缀关系非法
    vector<string> words4 = {"apple", "app"};
    cout << "测试用例4: " << solution.alienOrder(words4) << endl; // 输出: Impossible
    
    return 0;
}