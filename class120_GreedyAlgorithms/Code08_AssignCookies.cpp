/**
 * 分发饼干 - 贪心算法 + 双指针解决方案（C++实现，LeetCode版本）
 * 
 * 题目描述：
 * 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干
 * 对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸
 * 并且每块饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i
 * 目标是尽可能满足越多数量的孩子，并输出这个最大数值
 * 
 * 测试链接：https://leetcode.cn/problems/assign-cookies/
 * 
 * 算法思想：
 * 贪心算法 + 双指针
 * 1. 将孩子胃口值数组g和饼干尺寸数组s都按升序排列
 * 2. 使用双指针分别指向当前考虑的孩子和饼干
 * 3. 如果当前饼干能满足当前孩子，则两个指针都前移，满足孩子数加1
 * 4. 如果当前饼干不能满足当前孩子，则饼干指针前移，寻找更大的饼干
 * 5. 直到其中一个数组遍历完为止
 * 
 * 时间复杂度分析：
 * O(m*logm + n*logn) - 其中m是孩子数量，n是饼干数量
 * - 对孩子胃口值数组排序需要O(m*logm)
 * - 对饼干尺寸数组排序需要O(n*logn)
 * - 双指针遍历需要O(m+n)
 * 
 * 空间复杂度分析：
 * O(1) - 只使用了常数级别的额外空间（不考虑排序所需的额外空间）
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个元素数组等特殊情况
 * 2. 输入验证：检查输入是否为有效数组
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 贪心策略证明：
 * 使用最小的饼干满足最小的孩子，可以最大化满足的孩子数量
 * 这种策略满足贪心选择性质和最优子结构性质
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <stdexcept>

using namespace std;

class Solution {
public:
    /**
     * 计算最多能满足的孩子数量
     * 
     * @param g 孩子胃口值数组
     * @param s 饼干尺寸数组
     * @return 最多能满足的孩子数量
     * 
     * 算法步骤：
     * 1. 对孩子胃口值和饼干尺寸进行排序
     * 2. 使用双指针遍历两个数组
     * 3. 如果当前饼干能满足当前孩子，则满足孩子数加1
     * 4. 返回最终满足的孩子数量
     */
    int findContentChildren(vector<int>& g, vector<int>& s) {
        // 输入验证
        if (g.empty() || s.empty()) {
            return 0;
        }
        
        // 对孩子胃口值数组和饼干尺寸数组都按升序排列
        sort(g.begin(), g.end());
        sort(s.begin(), s.end());
        
        int childIndex = 0;   // 孩子指针
        int cookieIndex = 0;   // 饼干指针
        int satisfiedChildren = 0; // 满足的孩子数
        
        // 双指针遍历
        while (childIndex < g.size() && cookieIndex < s.size()) {
            // 如果当前饼干能满足当前孩子
            if (s[cookieIndex] >= g[childIndex]) {
                satisfiedChildren++; // 满足孩子数加1
                childIndex++;        // 孩子指针前移
            }
            // 无论是否满足，饼干指针都要前移
            cookieIndex++;
        }
        
        return satisfiedChildren;
    }
    
    /**
     * 调试版本：打印计算过程中的中间结果
     * 
     * @param g 孩子胃口值数组
     * @param s 饼干尺寸数组
     * @return 最多能满足的孩子数量
     */
    int debugFindContentChildren(vector<int>& g, vector<int>& s) {
        if (g.empty() || s.empty()) {
            cout << "孩子或饼干数组为空，无法分配" << endl;
            return 0;
        }
        
        cout << "孩子胃口值数组: ";
        for (int appetite : g) {
            cout << appetite << " ";
        }
        cout << endl;
        
        cout << "饼干尺寸数组: ";
        for (int size : s) {
            cout << size << " ";
        }
        cout << endl;
        
        // 排序
        sort(g.begin(), g.end());
        sort(s.begin(), s.end());
        
        cout << "排序后孩子胃口值: ";
        for (int appetite : g) {
            cout << appetite << " ";
        }
        cout << endl;
        
        cout << "排序后饼干尺寸: ";
        for (int size : s) {
            cout << size << " ";
        }
        cout << endl;
        
        int childIndex = 0;
        int cookieIndex = 0;
        int satisfiedChildren = 0;
        
        cout << "\n分配过程:" << endl;
        while (childIndex < g.size() && cookieIndex < s.size()) {
            cout << "考虑孩子" << childIndex << "(胃口=" << g[childIndex] 
                 << ") 和饼干" << cookieIndex << "(尺寸=" << s[cookieIndex] << ")";
            
            if (s[cookieIndex] >= g[childIndex]) {
                satisfiedChildren++;
                cout << " -> 分配成功，满足孩子数: " << satisfiedChildren << endl;
                childIndex++;
            } else {
                cout << " -> 饼干太小，跳过此饼干" << endl;
            }
            cookieIndex++;
        }
        
        cout << "\n最终结果: 最多能满足 " << satisfiedChildren << " 个孩子" << endl;
        return satisfiedChildren;
    }
};

/**
 * 测试函数：验证分发饼干算法的正确性
 */
void testFindContentChildren() {
    Solution solution;
    
    cout << "分发饼干算法测试开始" << endl;
    cout << "====================" << endl;
    
    // 测试用例1: g = [1,2,3], s = [1,1]
    vector<int> g1 = {1, 2, 3};
    vector<int> s1 = {1, 1};
    int result1 = solution.findContentChildren(g1, s1);
    cout << "输入: g = [1,2,3], s = [1,1]" << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: 1" << endl;
    cout << (result1 == 1 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例2: g = [1,2], s = [1,2,3]
    vector<int> g2 = {1, 2};
    vector<int> s2 = {1, 2, 3};
    int result2 = solution.findContentChildren(g2, s2);
    cout << "输入: g = [1,2], s = [1,2,3]" << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: 2" << endl;
    cout << (result2 == 2 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例3: g = [1,2,7,8,9], s = [1,3,5,9,10]
    vector<int> g3 = {1, 2, 7, 8, 9};
    vector<int> s3 = {1, 3, 5, 9, 10};
    int result3 = solution.findContentChildren(g3, s3);
    cout << "输入: g = [1,2,7,8,9], s = [1,3,5,9,10]" << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: 4" << endl;
    cout << (result3 == 4 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例4: 空孩子数组
    vector<int> g4 = {};
    vector<int> s4 = {1, 2, 3};
    int result4 = solution.findContentChildren(g4, s4);
    cout << "输入: g = [], s = [1,2,3]" << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期: 0" << endl;
    cout << (result4 == 0 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例5: 空饼干数组
    vector<int> g5 = {1, 2, 3};
    vector<int> s5 = {};
    int result5 = solution.findContentChildren(g5, s5);
    cout << "输入: g = [1,2,3], s = []" << endl;
    cout << "输出: " << result5 << endl;
    cout << "预期: 0" << endl;
    cout << (result5 == 0 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
}

/**
 * 性能测试：测试算法在大规模数据下的表现
 */
void performanceTest() {
    Solution solution;
    
    cout << "性能测试开始" << endl;
    cout << "============" << endl;
    
    // 生成大规模测试数据
    int n = 10000;  // 孩子数量
    int m = 15000; // 饼干数量
    
    vector<int> g(n);
    vector<int> s(m);
    
    // 生成随机胃口值和饼干尺寸
    for (int i = 0; i < n; i++) {
        g[i] = rand() % 1000 + 1;  // 胃口值在1-1000之间
    }
    for (int i = 0; i < m; i++) {
        s[i] = rand() % 1000 + 1;  // 饼干尺寸在1-1000之间
    }
    
    long startTime = clock();
    int result = solution.findContentChildren(g, s);
    long endTime = clock();
    
    cout << "数据规模: " << n << " 个孩子, " << m << " 块饼干" << endl;
    cout << "执行时间: " << (endTime - startTime) * 1000.0 / CLOCKS_PER_SEC << " 毫秒" << endl;
    cout << "满足孩子数: " << result << endl;
    cout << "性能测试结束" << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "分发饼干 - 贪心算法 + 双指针解决方案" << endl;
    cout << "===================================" << endl;
    
    // 运行基础测试
    testFindContentChildren();
    
    cout << "\n调试模式示例:" << endl;
    Solution solution;
    vector<int> debugG = {1, 2, 3};
    vector<int> debugS = {1, 1};
    cout << "对测试用例 g = [1,2,3], s = [1,1] 进行调试跟踪:" << endl;
    int debugResult = solution.debugFindContentChildren(debugG, debugS);
    cout << "最终结果: " << debugResult << endl;
    
    cout << "\n算法分析:" << endl;
    cout << "- 时间复杂度: O(m*logm + n*logn) - 排序和双指针遍历" << endl;
    cout << "- 空间复杂度: O(1) - 只使用常数级别额外空间" << endl;
    cout << "- 贪心策略: 使用最小的饼干满足最小的孩子" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    cout << "- 证明: 反证法可以证明这是最优分配策略" << endl;
    
    // 可选：运行性能测试
    // cout << "\n性能测试:" << endl;
    // performanceTest();
    
    return 0;
}