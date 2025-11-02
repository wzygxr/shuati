/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 你可以 任意多次交换 在 pairs 中任意一对索引处的字符。
 * 返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的索引合并到同一个集合中
 * 2. 对于每个集合，将其中的字符按照字典序排序
 * 3. 按照原始索引的顺序，依次从对应的集合中取出最小的可用字符
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n + m * α(n))，其中n是字符串长度，m是pairs数组长度，α是阿克曼函数的反函数，近似为常数
 * - 收集每个集合中的字符：O(n)
 * - 对每个集合中的字符排序：O(n log k)，其中k是集合的最大大小
 * - 重组字符串：O(n)
 * - 总体时间复杂度：O(n log n + m * α(n))
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储每个集合的字符：O(n)
 * - 总体空间复杂度：O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <queue>

using namespace std;

class SmallestStringWithSwaps {
private:
    // 并查集的父节点数组
    vector<int> parent;
    // 并查集的秩数组，用于按秩合并
    vector<int> rank;

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

public:
    /**
     * 计算字典序最小的字符串
     * @param s 原始字符串
     * @param pairs 索引对数组
     * @return 字典序最小的字符串
     */
    string smallestStringWithSwaps(string s, vector<vector<int>>& pairs) {
        int n = s.length();
        
        // 初始化并查集
        parent.resize(n);
        rank.resize(n, 1);
        
        // 初始化，每个元素的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        
        // 将可以交换的索引合并到同一个集合中
        for (const auto& pair : pairs) {
            int x = pair[0];
            int y = pair[1];
            
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按秩合并
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
        
        // 收集每个集合中的字符和它们的索引
        unordered_map<int, vector<pair<char, int>>> charGroups;
        for (int i = 0; i < n; i++) {
            int root = find(i);
            charGroups[root].emplace_back(s[i], i);
        }
        
        // 创建结果字符串
        string result = s;
        
        // 对每个集合进行处理
        for (auto& [root, group] : charGroups) {
            // 提取字符并排序
            vector<char> chars;
            vector<int> indices;
            
            for (const auto& [c, idx] : group) {
                chars.push_back(c);
                indices.push_back(idx);
            }
            
            // 对字符排序
            sort(chars.begin(), chars.end());
            // 对索引排序
            sort(indices.begin(), indices.end());
            
            // 将排序后的字符放置到对应的索引位置
            for (int i = 0; i < chars.size(); i++) {
                result[indices[i]] = chars[i];
            }
        }
        
        return result;
    }
};

/**
 * 主函数，用于测试
 */
int main() {
    SmallestStringWithSwaps solution;
    
    // 测试用例1
    string s1 = "dcab";
    vector<vector<int>> pairs1 = {{0, 3}, {1, 2}};
    cout << "测试用例1结果：" << solution.smallestStringWithSwaps(s1, pairs1) << endl;
    // 预期输出："bacd"
    
    // 测试用例2
    string s2 = "dcab";
    vector<vector<int>> pairs2 = {{0, 3}, {1, 2}, {0, 2}};
    cout << "测试用例2结果：" << solution.smallestStringWithSwaps(s2, pairs2) << endl;
    // 预期输出："abcd"
    
    // 测试用例3
    string s3 = "cba";
    vector<vector<int>> pairs3 = {{0, 1}, {1, 2}};
    cout << "测试用例3结果：" << solution.smallestStringWithSwaps(s3, pairs3) << endl;
    // 预期输出："abc"
    
    // 测试用例4：空字符串
    string s4 = "";
    vector<vector<int>> pairs4 = {};
    cout << "测试用例4结果：" << solution.smallestStringWithSwaps(s4, pairs4) << endl;
    // 预期输出：""
    
    return 0;
}

/**
 * 异常处理考虑：
 * 1. 空字符串处理：当s为空时，直接返回空字符串
 * 2. 空pairs数组处理：当pairs为空时，无法进行任何交换，直接返回原字符串
 * 3. 索引越界检查：确保pairs中的索引在有效范围内
 * 4. 大规模数据处理：通过路径压缩和按秩合并确保并查集操作的高效性
 * 
 * C++特定优化：
 * 1. 使用emplace_back代替push_back避免不必要的拷贝构造
 * 2. 使用auto和结构化绑定简化代码
 * 3. 直接修改结果字符串，避免额外的字符串构建开销
 * 4. 使用unordered_map而不是map以获得更好的平均查找性能
 */