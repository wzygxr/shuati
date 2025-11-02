#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <string>
#include <algorithm>
#include <sstream>

using namespace std;

/**
 * Codeforces 1681D. Required Length
 * 
 * 题目来源：https://codeforces.com/contest/1681/problem/D
 * 
 * 题目描述：
 * 给定一个整数 n 和一个目标长度 k，你需要找到一个 n 位数 x，
 * 使得通过不断将 x 乘以它的某一位数字，最终能得到一个至少 k 位的数。
 * 求最小的操作次数，如果无法达到目标则返回 -1。
 * 
 * 算法思路：
 * 这是一个BFS搜索问题，可以使用以下方法解决：
 * 1. BFS搜索：从初始数字开始，逐步生成新的数字
 * 2. 剪枝优化：避免重复访问相同的数字
 * 3. 最近点对思想：在状态空间中寻找最优路径
 * 
 * 虽然这不是经典的最近点对问题，但可以看作是在状态空间中寻找最短路径的问题，
 * 与最近点对问题有相似的搜索和优化思想。
 * 
 * 时间复杂度：
 * - BFS搜索：O(状态数)
 * - 空间复杂度：O(状态数)
 * 
 * 应用场景：
 * 1. 数学游戏：数字变换游戏
 * 2. 密码学：数字序列生成
 * 3. 算法竞赛：状态搜索问题
 * 
 * 相关题目：
 * 1. LeetCode 973. 最接近原点的 K 个点
 * 2. LeetCode 612. 平面上的最短距离
 * 3. Codeforces 1042D. Petya and Array
 */

// 辅助函数：将long long转换为字符串
string longToString(long long n) {
    stringstream ss;
    ss << n;
    return ss.str();
}

/**
 * BFS解法
 * 时间复杂度：O(状态数)
 * 空间复杂度：O(状态数)
 * @param n 初始数字
 * @param k 目标长度
 * @return 最小操作次数，如果无法达到目标则返回-1
 */
int requiredLength(long long n, int k) {
    // 如果初始数字已经满足长度要求
    if (longToString(n).length() >= k) {
        return 0;
    }
    
    // BFS队列，存储[当前数字, 操作次数]
    queue<pair<long long, int> > q;
    q.push(make_pair(n, 0));
    
    // 记录已访问的数字
    set<long long> visited;
    visited.insert(n);
    
    while (!q.empty()) {
        pair<long long, int> current = q.front();
        q.pop();
        
        long long num = current.first;
        int steps = current.second;
        
        // 获取数字的每一位
        string numStr = longToString(num);
        for (int i = 0; i < numStr.length(); i++) {
            int digit = numStr[i] - '0';
            // 跳过0，因为乘以0会得到0
            if (digit == 0) continue;
            
            long long newNum = num * digit;
            // 如果新数字满足长度要求
            if (longToString(newNum).length() >= k) {
                return steps + 1;
            }
            
            // 如果新数字未访问过且长度小于目标长度太多（剪枝）
            if (visited.find(newNum) == visited.end() && longToString(newNum).length() < k) {
                visited.insert(newNum);
                q.push(make_pair(newNum, steps + 1));
            }
        }
    }
    
    return -1; // 无法达到目标
}

/**
 * 优化的BFS解法
 * 时间复杂度：O(状态数)
 * 空间复杂度：O(状态数)
 * @param n 初始数字
 * @param k 目标长度
 * @return 最小操作次数，如果无法达到目标则返回-1
 */
int requiredLengthOptimized(long long n, int k) {
    // 如果初始数字已经满足长度要求
    if (longToString(n).length() >= k) {
        return 0;
    }
    
    // BFS队列，存储[当前数字, 操作次数]
    queue<pair<long long, int> > q;
    q.push(make_pair(n, 0));
    
    // 记录已访问的数字
    set<long long> visited;
    visited.insert(n);
    
    // 目标长度
    int targetLength = k;
    
    while (!q.empty()) {
        pair<long long, int> current = q.front();
        q.pop();
        
        long long num = current.first;
        int steps = current.second;
        
        // 获取数字的每一位
        string numStr = longToString(num);
        // 按数字大小降序排列，优先处理大的数字（贪心策略）
        vector<int> digits;
        for (int i = 0; i < numStr.length(); i++) {
            digits.push_back(numStr[i] - '0');
        }
        sort(digits.begin(), digits.end(), greater<int>());
        
        for (int digit : digits) {
            // 跳过0，因为乘以0会得到0
            if (digit == 0) continue;
            
            long long newNum = num * digit;
            // 如果新数字满足长度要求
            if (longToString(newNum).length() >= targetLength) {
                return steps + 1;
            }
            
            // 剪枝：如果新数字长度已经超过目标太多，跳过
            if (longToString(newNum).length() > targetLength + 5) {
                continue;
            }
            
            // 如果新数字未访问过
            if (visited.find(newNum) == visited.end()) {
                visited.insert(newNum);
                q.push(make_pair(newNum, steps + 1));
            }
        }
    }
    
    return -1; // 无法达到目标
}

/**
 * 测试函数
 */
void testRequiredLength() {
    cout << "=== 测试 Codeforces 1681D. Required Length ===" << endl;
    
    // 测试用例1
    long long n1 = 1; 
    int k1 = 3;
    cout << "测试用例1:" << endl;
    cout << "n: " << n1 << ", k: " << k1 << endl;
    cout << "BFS解法结果: " << requiredLength(n1, k1) << endl;
    cout << "优化BFS解法结果: " << requiredLengthOptimized(n1, k1) << endl;
    cout << "期望结果: 3" << endl;
    cout << endl;
    
    // 测试用例2
    long long n2 = 123;
    int k2 = 5;
    cout << "测试用例2:" << endl;
    cout << "n: " << n2 << ", k: " << k2 << endl;
    cout << "BFS解法结果: " << requiredLength(n2, k2) << endl;
    cout << "优化BFS解法结果: " << requiredLengthOptimized(n2, k2) << endl;
    cout << "期望结果: 2" << endl;
    cout << endl;
    
    // 测试用例3
    long long n3 = 999;
    int k3 = 2;
    cout << "测试用例3:" << endl;
    cout << "n: " << n3 << ", k: " << k3 << endl;
    cout << "BFS解法结果: " << requiredLength(n3, k3) << endl;
    cout << "优化BFS解法结果: " << requiredLengthOptimized(n3, k3) << endl;
    cout << "期望结果: 0" << endl;
    cout << endl;
}

int main() {
    testRequiredLength();
    return 0;
}