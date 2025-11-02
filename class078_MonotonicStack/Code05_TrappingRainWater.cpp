#include <vector>
#include <stack>
#include <algorithm>
#include <iostream>
#include <string>
#include <sstream>
using namespace std;

/**
 * 接雨水问题 - 单调栈解法
 * 
 * 题目描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 测试链接：https://leetcode.cn/problems/trapping-rain-water/
 * 题目来源：LeetCode
 * 难度：困难
 * 
 * 核心算法：单调栈
 * 
 * 其他相关题目：
 * 1. 柱状图中最大的矩形 (Largest Rectangle in Histogram)
 * 2. 统计全1子矩形的数量 (Count Submatrices With All Ones)
 * 3. 最大矩形 (Maximal Rectangle)
 * 4. 接雨水 II (Trapping Rain Water II)
 * 5. 最大子数组和 (Maximum Subarray) - 变种应用
  * 
  * 更多单调栈相关题目：
  * 6. 子数组的最大最小值之差（HackerRank）- 使用两个单调队列维护滑动窗口的最小最大值
  * 7. 所有可能的递增子序列（AtCoder ABC217-D）- 使用单调栈记录递增子序列结束位置
  * 8. 寻找右侧第一个小于当前元素的位置（LintCode 495）- 使用单调递增栈从右向左遍历
  * 9. 最大子矩阵 III（HDU 3480）- 基于单调栈的最大矩形问题扩展
  * 10. 合并石头的最低成本（LeetCode 1000）- 动态规划与单调栈优化
  * 11. 最短路径访问所有节点（LeetCode 847）- BFS与状态压缩结合，使用栈优化路径
  * 12. 最多能完成排序的块（LeetCode 1601）- 状态压缩与单调栈优化
  * 13. 最大连续子序列（SPOJ KGSS）- 使用单调栈优化最大子序列和计算
  * 14. 矩形覆盖（ACWing 399）- 基于单调栈的矩形覆盖问题
  * 15. 双栈排序（洛谷 P1198）- 使用单调栈进行双栈排序
  * 16. 股票买卖 III（LeetCode 123）- 使用单调栈优化股票买卖策略
  * 17. 最小字典序字符串（Codeforces 1204B）- 使用单调栈构建最小字典序字符串
  * 18. 最长交替子序列（LeetCode 516）- 使用单调栈优化最长交替子序列计算
  * 19. 二维接雨水（LeetCode 407）- 优先队列与单调栈结合解决二维接雨水问题
  * 20. 寻找子数组的最小和最大元素（CodeChef MAXAND18）- 使用单调栈快速查询子数组最值
  * 21. 字符串合并（Codeforces 1294E）- 动态规划与单调栈优化
  * 22. 最大交换次数（LeetCode 670）- 使用单调栈找到最佳交换位置
  * 23. 最多能完成排序的块 II（LeetCode 768）- 使用单调栈维护块的最大值
  * 24. 不同的子序列 II（LeetCode 940）- 动态规划与单调栈优化
  * 25. 最小覆盖子数组（ACWing 154）- 滑动窗口与单调栈结合
  * 26. 最大子矩阵和（LintCode 405）- 二维前缀和与单调栈结合
  * 27. 路径规划问题（SPOJ ADASTRNG）- 使用单调栈优化路径规划
  * 28. 最小生成树（AtCoder ABC206-E）- Kruskal算法与单调栈优化
  * 29. 网络流问题（HackerEarth）- 单调栈优化网络流算法
  * 30. 字符串匹配问题（牛客）- KMP算法与单调栈结合
  * 31. 最大宽度坡（LeetCode 962）- 使用单调递减栈存储可能的坡底索引
  * 32. 柱状图中最大的矩形（LeetCode 84）- 使用单调递增栈计算最大矩形面积
 */

class Solution {
public:
    /**
     * 解题思路详解：
     * 
     * 1. 核心思想：使用单调递减栈来找到形成凹槽的左右边界
     * 2. 为什么使用单调栈？
     *    - 我们需要快速找到某个位置左侧和右侧第一个比它高的柱子
     *    - 单调递减栈可以在O(n)时间内解决这个问题
     * 
     * 具体算法步骤：
     * 1. 维护一个单调递减栈，栈中存储的是柱子的索引（而非高度值）
     * 2. 遍历数组中的每个元素：
     *    a. 当栈不为空且当前元素高度大于栈顶索引对应的高度时，说明找到了一个凹槽
     *    b. 弹出栈顶元素作为凹槽的底部
     *    c. 如果栈为空，说明没有左边界，无法形成凹槽，跳出内部循环
     *    d. 新的栈顶元素是左边界的索引
     *    e. 当前元素是右边界的索引
     *    f. 计算凹槽的高度和宽度，累加雨水量
     * 3. 将当前索引入栈
     * 
     * 时间复杂度分析：
     * - 每个元素最多入栈和出栈各一次，总共有n个元素
     * - 内部while循环的总操作次数是O(n)，因为每个元素最多被弹出一次
     * - 因此总体时间复杂度为O(n)
     * 
     * 空间复杂度分析：
     * - 栈的空间在最坏情况下为O(n)（当数组完全递减时）
     * - 其他变量占用O(1)空间
     * - 因此总体空间复杂度为O(n)
     * 
     * 是否为最优解：
     * 是，这是解决该问题的最优解之一。其他最优解法还包括双指针法和动态规划法，
     * 但单调栈方法在理解和实现上更为直观，并且可以推广到类似的问题。
     * 
     * 工程化考量：
     * 1. 健壮性：处理了数组长度小于3的边界情况
     * 2. 性能优化：使用索引而非实际值入栈，避免了不必要的值传递
     * 3. 可读性：使用清晰的变量名和注释说明算法步骤
     * 4. 线程安全：该函数是无状态的，可以安全地在多线程环境中并发调用
     * 
     * C++语言特性优化：
     * 1. 使用STL的stack容器，提供高效的栈操作
     * 2. 使用vector的size()方法获取数组长度，避免数组越界
     * 3. 利用引用参数避免不必要的拷贝操作
     * 
     * @param height 柱子高度数组（引用传递）
     * @return 能接住的雨水量
     */
    int trap(vector<int>& height) {
        // 边界条件检查：数组长度小于3时，无法接水
        if (height.size() <= 2) {
            return 0;
        }

        // 使用栈存储索引，维护单调递减栈
        stack<int> st;
        int result = 0; // 总雨水量

        // 遍历每个柱子
        for (int i = 0; i < height.size(); ++i) {
            // 当栈不为空且当前高度大于栈顶索引对应的高度时，可能形成凹槽
            while (!st.empty() && height[i] > height[st.top()]) {
                // 弹出栈顶元素作为凹槽底部
                int bottomIndex = st.top();
                st.pop();

                // 如果栈为空，说明没有左边界，无法形成凹槽
                if (st.empty()) {
                    break;
                }

                // 左边界索引
                int leftBoundaryIndex = st.top();
                // 右边界就是当前索引i
                
                // 计算雨水高度 = min(左边界高度, 右边界高度) - 凹槽底部高度
                int waterHeight = min(height[leftBoundaryIndex], height[i]) - height[bottomIndex];
                // 计算雨水宽度 = 右边界索引 - 左边界索引 - 1
                int waterWidth = i - leftBoundaryIndex - 1;
                // 累加雨水量 = 高度 × 宽度
                result += waterHeight * waterWidth;
            }
            // 将当前索引入栈
            st.push(i);
        }

        return result;
    }

    /**
     * 调试辅助函数：打印栈的当前状态
     * 用于调试算法过程，观察栈的变化
     * 
     * @param st 当前的栈
     */
    void printStack(stack<int> st) {
        cout << "Stack: [";
        bool first = true;
        while (!st.empty()) {
            if (!first) {
                cout << ", ";
            }
            cout << st.top();
            st.pop();
            first = false;
        }
        cout << "]" << endl;
    }
};

/**
 * 数组工具函数：将向量转换为字符串表示
 * 
 * @param vec 输入向量
 * @return 格式化的字符串表示
 */
string vectorToString(const vector<int>& vec) {
    stringstream ss;
    ss << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        ss << vec[i];
        if (i < vec.size() - 1) {
            ss << ", ";
        }
    }
    ss << "]";
    return ss.str();
}

/**
 * 测试用例展示
 * 包含多种场景：
 * 1. 常规场景：有多个凹槽
 * 2. 特殊场景：一侧有很高的柱子
 * 3. 边界场景：空数组、单调数组等
 */
int main() {
    Solution solution;
    
    // 测试用例集合
    vector<pair<vector<int>, int>> testCases = {
        // {输入数组, 期望输出}
        {{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, 6},  // 常规情况 - 多个凹槽
        {{4, 2, 0, 3, 2, 5}, 9},                     // 右侧有高柱子
        {{}, 0},                                     // 空数组
        {{1, 2, 3, 4, 5}, 0},                        // 单调递增
        {{5, 4, 3, 2, 1}, 0},                        // 单调递减
        {{2, 0, 2}, 2},                              // 中间低两边高
        {{3, 0, 0, 2, 0, 4}, 10},                    // 多个宽凹槽
        {{0, 0, 0, 0}, 0},                           // 全为0
        {{100, 0, 100}, 100},                        // 宽而深的凹槽
        {{3, 1, 2, 4, 0, 1, 3, 2}, 8}                // 复杂地形
    };

    // 运行所有测试用例
    for (size_t i = 0; i < testCases.size(); ++i) {
        vector<int> height = testCases[i].first;
        int expected = testCases[i].second;
        int result = solution.trap(height);
        
        cout << "测试用例" << (i + 1) << ": " << endl;
        cout << "  输入: " << vectorToString(height) << endl;
        cout << "  输出: " << result << endl;
        cout << "  期望: " << expected << endl;
        cout << "  结果: " << (result == expected ? "通过" : "失败") << endl << endl;
    }

    return 0;
}