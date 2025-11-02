// C++标准库头文件
#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <set>
#include <algorithm>
using namespace std;

/**
 * 账户合并 (C++版本)
 * 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素是名称 (name)，
 * 其余元素是邮箱地址表示该账户的邮箱地址。
 * 现在我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。
 * 请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。
 * 一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
 * 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。
 * 账户本身可以以任意顺序返回。
 * 
 * 示例 1:
 * 输入: 
 * accounts = [
 *   ["John", "johnsmith@mail.com", "john00@mail.com"],
 *   ["John", "johnnybravo@mail.com"],
 *   ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
 *   ["Mary", "mary@mail.com"]
 * ]
 * 输出: 
 * [
 *   ["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],
 *   ["John", "johnnybravo@mail.com"],
 *   ["Mary", "mary@mail.com"]
 * ]
 * 
 * 解释: 
 * 第一个和第三个 John 是同一个人，因为他们有共同的邮箱地址 "johnsmith@mail.com"。
 * 第二个 John 和 Mary 是不同的人，因为他们的邮箱地址没有被其他帐户使用。
 * 可以以任意顺序返回这些列表，例如答案 
 * [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'], 
 *  ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']]
 *  也是正确的。
 * 
 * 约束条件：
 * 1 <= accounts.length <= 1000
 * 2 <= accounts[i].length <= 10
 * 1 <= accounts[i][j].length <= 30
 * accounts[i][0] 由英文字母组成
 * accounts[i][j] (for j > 0) 是有效的邮箱地址
 * 
 * 测试链接: https://leetcode.cn/problems/accounts-merge/
 * 相关平台: LeetCode 721, LintCode 1297, 牛客网
 */

class UnionFind {
private:
    vector<int> parent;  // parent[i]表示节点i的父节点

public:
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    UnionFind(int n) {
        parent.resize(n);
        // 初始时每个节点都是自己的父节点
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    /**
     * 查找节点的根节点（代表元素）
     * 使用路径压缩优化
     * @param x 要查找的节点
     * @return 节点x所在集合的根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将路径上的所有节点直接连接到根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 合并两个集合
     * @param x 第一个节点
     * @param y 第二个节点
     */
    void unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        // 如果已经在同一个集合中，则无需合并
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }
};

class Solution {
public:
    /**
     * 使用并查集解决账户合并问题
     * 
     * 解题思路：
     * 1. 将每个账户看作一个节点，使用并查集来维护账户之间的连接关系
     * 2. 对于每个邮箱地址，记录它第一次出现的账户索引
     * 3. 如果一个邮箱地址在多个账户中出现，则将这些账户合并到同一个集合中
     * 4. 最后，将同一个集合中的所有账户的邮箱地址合并，并按ASCII顺序排序
     * 
     * 时间复杂度：O(N * M * α(N))，其中N是账户数量，M是每个账户的平均邮箱数量，α是阿克曼函数的反函数
     * 空间复杂度：O(N * M)
     * 
     * @param accounts 账户列表
     * @return 合并后的账户列表
     */
    static vector<vector<string>> accountsMerge(vector<vector<string>>& accounts) {
        if (accounts.empty()) {
            return {};
        }
        
        int n = accounts.size();
        UnionFind unionFind(n);
        
        // 记录每个邮箱第一次出现的账户索引
        unordered_map<string, int> emailToIndex;
        
        // 遍历所有账户
        for (int i = 0; i < n; i++) {
            // 从索引1开始，因为索引0是账户名称
            for (int j = 1; j < accounts[i].size(); j++) {
                string email = accounts[i][j];
                if (emailToIndex.count(email)) {
                    // 如果邮箱已经出现过，则合并当前账户和之前出现该邮箱的账户
                    int prevIndex = emailToIndex[email];
                    unionFind.unionSets(i, prevIndex);
                } else {
                    // 记录邮箱第一次出现的账户索引
                    emailToIndex[email] = i;
                }
            }
        }
        
        // 将同一个集合中的所有邮箱地址合并到一起
        unordered_map<int, set<string>> indexToEmails;
        for (int i = 0; i < n; i++) {
            int root = unionFind.find(i);
            for (int j = 1; j < accounts[i].size(); j++) {
                indexToEmails[root].insert(accounts[i][j]);
            }
        }
        
        // 构造结果
        vector<vector<string>> result;
        for (auto& entry : indexToEmails) {
            int index = entry.first;
            set<string>& emails = entry.second;
            
            vector<string> mergedAccount;
            // 添加账户名称
            mergedAccount.push_back(accounts[index][0]);
            // 添加排序后的邮箱地址
            for (const string& email : emails) {
                mergedAccount.push_back(email);
            }
            result.push_back(mergedAccount);
        }
        
        return result;
    }
};

// 测试方法
int main() {
    // 测试用例1
    vector<vector<string>> accounts1 = {
        {"John", "johnsmith@mail.com", "john00@mail.com"},
        {"John", "johnnybravo@mail.com"},
        {"John", "johnsmith@mail.com", "john_newyork@mail.com"},
        {"Mary", "mary@mail.com"}
    };
    
    cout << "测试用例1结果:" << endl;
    vector<vector<string>> result1 = Solution::accountsMerge(accounts1);
    for (const auto& account : result1) {
        cout << "[";
        for (int i = 0; i < account.size(); i++) {
            cout << "\"" << account[i] << "\"";
            if (i < account.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    // 测试用例2
    vector<vector<string>> accounts2 = {
        {"Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"},
        {"Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"},
        {"Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"},
        {"Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"},
        {"Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"}
    };
    
    cout << "\n测试用例2结果:" << endl;
    vector<vector<string>> result2 = Solution::accountsMerge(accounts2);
    for (const auto& account : result2) {
        cout << "[";
        for (int i = 0; i < account.size(); i++) {
            cout << "\"" << account[i] << "\"";
            if (i < account.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    return 0;
}