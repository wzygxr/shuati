/**
 * LeetCode 721 - 账户合并
 * https://leetcode-cn.com/problems/accounts-merge/
 * 
 * 题目描述：
 * 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素 accounts[i][0] 是名称 (name)，其余元素是 emails 表示该账户的邮箱地址。
 * 
 * 现在，我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
 * 
 * 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。账户本身可以以任意顺序返回。
 * 
 * 解题思路：
 * 1. 使用并查集来合并具有共同邮箱的账户
 * 2. 首先为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
 * 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
 * 4. 最后，将同一集合中的邮箱按照账户名称分组，并排序
 * 
 * 时间复杂度分析：
 * - 初始化并处理邮箱：O(n * m)，其中n是账户数量，m是平均每个账户的邮箱数量
 * - 合并操作：O(n * m * α(k))，其中k是唯一邮箱的数量，α是阿克曼函数的反函数，近似为常数
 * - 排序邮箱：O(k log k)，其中k是唯一邮箱的数量
 * - 总体时间复杂度：O(n * m + k log k)
 * 
 * 空间复杂度分析：
 * - 存储邮箱ID和映射关系：O(k)
 * - 并查集数组：O(k)
 * - 存储结果：O(k)
 * - 总体空间复杂度：O(k)
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <map>
#include <algorithm>

using namespace std;

class AccountsMerge {
private:
    // 并查集的父节点数组
    vector<int> parent;

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
     * 合并账户
     * @param accounts 账户列表
     * @return 合并后的账户列表
     */
    vector<vector<string>> accountsMerge(vector<vector<string>>& accounts) {
        // 1. 为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
        unordered_map<string, int> emailToId;
        unordered_map<string, string> emailToName;
        int emailId = 0;

        for (const auto& account : accounts) {
            const string& name = account[0];
            for (size_t i = 1; i < account.size(); i++) {
                const string& email = account[i];
                if (emailToId.find(email) == emailToId.end()) {
                    emailToId[email] = emailId++;
                    emailToName[email] = name;
                }
            }
        }

        // 2. 初始化并查集
        parent.resize(emailId);
        for (int i = 0; i < emailId; i++) {
            parent[i] = i;
        }

        // 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
        for (const auto& account : accounts) {
            if (account.size() > 1) { // 确保账户至少有一个邮箱
                const string& firstEmail = account[1];
                int firstId = emailToId[firstEmail];

                // 将当前账户的所有其他邮箱与第一个邮箱合并
                for (size_t i = 2; i < account.size(); i++) {
                    const string& currentEmail = account[i];
                    int currentId = emailToId[currentEmail];
                    
                    // 合并两个邮箱所在的集合
                    int root1 = find(firstId);
                    int root2 = find(currentId);
                    if (root1 != root2) {
                        parent[root2] = root1;
                    }
                }
            }
        }

        // 4. 收集每个集合中的邮箱
        unordered_map<int, vector<string>> idToEmails;
        for (const auto& entry : emailToId) {
            const string& email = entry.first;
            int emailIdValue = entry.second;
            int rootId = find(emailIdValue);

            idToEmails[rootId].push_back(email);
        }

        // 5. 构建结果
        vector<vector<string>> result;
        for (auto& entry : idToEmails) {
            vector<string>& emails = entry.second;
            // 排序邮箱
            sort(emails.begin(), emails.end());

            // 创建账户记录
            vector<string> account;
            // 添加名称（可以从任意一个邮箱获取）
            account.push_back(emailToName[emails[0]]);
            // 添加排序后的邮箱
            account.insert(account.end(), emails.begin(), emails.end());

            result.push_back(account);
        }

        return result;
    }
};

/**
 * 打印账户列表的辅助函数
 */
void printAccounts(const vector<vector<string>>& accounts) {
    for (const auto& account : accounts) {
        cout << "[";
        for (size_t i = 0; i < account.size(); i++) {
            cout << "\"" << account[i] << "\"";
            if (i < account.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]\n";
    }
}

/**
 * 主函数，用于测试
 */
int main() {
    AccountsMerge solution;

    // 测试用例1
    vector<vector<string>> accounts1 = {
        {"John", "johnsmith@mail.com", "john00@mail.com"},
        {"John", "johnnybravo@mail.com"},
        {"John", "johnsmith@mail.com", "john_newyork@mail.com"},
        {"Mary", "mary@mail.com"}
    };

    vector<vector<string>> result1 = solution.accountsMerge(accounts1);
    cout << "测试用例1结果：\n";
    printAccounts(result1);

    // 测试用例2：只有一个账户
    vector<vector<string>> accounts2 = {
        {"Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co"}
    };

    vector<vector<string>> result2 = solution.accountsMerge(accounts2);
    cout << "测试用例2结果：\n";
    printAccounts(result2);

    // 测试用例3：没有账户
    vector<vector<string>> accounts3 = {};
    vector<vector<string>> result3 = solution.accountsMerge(accounts3);
    cout << "测试用例3结果：\n";
    printAccounts(result3);

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用unordered_map代替HashMap，提供更好的平均查找性能
 * 2. 使用const引用和auto关键字，减少不必要的拷贝和提高代码可读性
 * 3. 使用emplace_back代替push_back，避免不必要的拷贝构造
 * 4. 使用size_t类型处理索引，避免潜在的类型转换问题
 * 5. 使用insert函数批量插入元素，提高效率
 * 
 * 注意事项：
 * 1. 确保并查集的大小足够容纳所有唯一邮箱
 * 2. 处理空账户和只有名称没有邮箱的情况
 * 3. 使用sort函数对邮箱进行排序，满足题目要求
 */