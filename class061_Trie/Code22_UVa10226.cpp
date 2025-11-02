#include <iostream>
#include <map>
#include <string>
#include <iomanip>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * UVa 10226 Hardwood Species
 * 
 * 题目描述：
 * 统计森林中各种硬木的数量百分比。输入一系列树名，输出每种树名及其占总数的百分比。
 * 
 * 解题思路：
 * 1. 使用map统计每种树的数量（虽然题目在Trie专题中，但此题更适合用map）
 * 2. 遍历所有树名，统计每种树的数量
 * 3. 计算每种树的百分比并按字典序输出
 * 
 * 时间复杂度：O(N*logN)，其中N是树的数量（主要是排序的时间复杂度）
 * 空间复杂度：O(K)，其中K是不同树的种类数
 */
int main() {
    int t;
    cin >> t; // 测试用例数量
    cin.ignore(); // 消费换行符
    cin.ignore(); // 消费空行
    
    for (int i = 0; i < t; i++) {
        if (i > 0) {
            cout << endl; // 每个测试用例之间输出空行
        }
        
        map<string, int> treeCount; // 统计每种树的数量
        int totalCount = 0; // 树的总数量
        
        string line;
        // 读取树名，直到遇到空行或文件结束
        while (getline(cin, line) && !line.empty()) {
            treeCount[line]++;
            totalCount++;
        }
        
        // 输出每种树的百分比
        for (const auto& pair : treeCount) {
            const string& treeName = pair.first;
            int count = pair.second;
            double percentage = (double) count / totalCount * 100;
            cout << treeName << " " << fixed << setprecision(4) << percentage << endl;
        }
    }
    
    return 0;
}