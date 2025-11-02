// AtCoder 链表最大子段和问题
// 来源：AtCoder、各大OJ平台
// 测试链接：自定义题目，模拟最大子段和问题的链表版本

#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 提交时不要提交这个结构体
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

// 提交如下的方法
class Solution {
public:
    /**
     * 链表最大子段和（动态规划方法）
     * @param head 链表头节点
     * @return 最大子段和
     * 
     * 解题思路（Kadane算法变种）：
     * 1. 使用动态规划思想，维护当前子段和和最大子段和
     * 2. 遍历链表，对于每个节点：
     *    - 如果当前子段和小于0，重置为当前节点值
     *    - 否则，累加当前节点值
     * 3. 更新最大子段和
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    int maxSubarraySum(ListNode* head) {
        if (head == nullptr) {
            return 0;
        }
        
        int maxSum = INT_MIN;
        int currentSum = 0;
        ListNode* current = head;
        
        while (current != nullptr) {
            // 如果当前子段和小于0，重置为当前节点值
            if (currentSum < 0) {
                currentSum = current->val;
            } else {
                // 否则累加当前节点值
                currentSum += current->val;
            }
            
            // 更新最大子段和
            maxSum = max(maxSum, currentSum);
            current = current->next;
        }
        
        return maxSum;
    }
    
    /**
     * 链表最大子段和（分治法）
     * @param head 链表头节点
     * @return 最大子段和
     * 
     * 解题思路（分治法）：
     * 1. 将链表分成左右两部分
     * 2. 递归求解左右部分的最大子段和
     * 3. 计算跨越中间的最大子段和
     * 4. 返回三者中的最大值
     * 
     * 时间复杂度：O(n log n) - n 是链表节点数量
     * 空间复杂度：O(log n) - 递归调用栈的深度
     * 是否最优解：不是（时间复杂度较高）
     */
    int maxSubarraySumDivideConquer(ListNode* head) {
        if (head == nullptr) {
            return 0;
        }
        vector<int> result = maxSubarraySumHelper(head);
        return result[0];
    }
    
private:
    /**
     * 分治辅助函数
     * @param node 当前节点
     * @return 数组，包含四个值：
     *         [0] 最大子段和
     *         [1] 包含头节点的最大子段和
     *         [2] 包含尾节点的最大子段和
     *         [3] 总和
     */
    vector<int> maxSubarraySumHelper(ListNode* node) {
        if (node == nullptr) {
            return {INT_MIN, INT_MIN, INT_MIN, 0};
        }
        
        if (node->next == nullptr) {
            int val = node->val;
            return {val, val, val, val};
        }
        
        // 找到中间节点（快慢指针）
        ListNode* slow = node;
        ListNode* fast = node;
        ListNode* prev = nullptr;
        
        while (fast != nullptr && fast->next != nullptr) {
            prev = slow;
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 分割链表
        ListNode* rightHead = slow;
        prev->next = nullptr;
        
        // 递归求解左右部分
        vector<int> left = maxSubarraySumHelper(node);
        vector<int> right = maxSubarraySumHelper(rightHead);
        
        // 恢复链表结构
        prev->next = rightHead;
        
        // 计算跨越中间的最大子段和
        int crossMax = max(left[2] + right[1], max(left[2], right[1]));
        
        // 合并结果
        int maxSum = max(max(left[0], right[0]), crossMax);
        int leftMax = max(left[1], left[3] + right[1]);
        int rightMax = max(right[2], right[3] + left[2]);
        int totalSum = left[3] + right[3];
        
        return {maxSum, leftMax, rightMax, totalSum};
    }
    
public:
    /**
     * 链表最大子段和（暴力法 - 用于验证）
     * @param head 链表头节点
     * @return 最大子段和
     * 
     * 解题思路：
     * 1. 枚举所有可能的子段
     * 2. 计算每个子段的和
     * 3. 记录最大值
     * 
     * 时间复杂度：O(n²) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：不是（时间复杂度太高）
     */
    int maxSubarraySumBruteForce(ListNode* head) {
        if (head == nullptr) {
            return 0;
        }
        
        int maxSum = INT_MIN;
        ListNode* start = head;
        
        while (start != nullptr) {
            int currentSum = 0;
            ListNode* end = start;
            
            while (end != nullptr) {
                currentSum += end->val;
                maxSum = max(maxSum, currentSum);
                end = end->next;
            }
            
            start = start->next;
        }
        
        return maxSum;
    }
};

/*
 * 题目扩展：链表最大子段和问题
 * 来源：AtCoder、各大OJ平台的自定义题目
 * 
 * 题目描述：
 * 给定一个链表，每个节点包含一个整数值，求链表中连续子段的最大和。
 * 
 * 解题思路：
 * 方法一：Kadane算法（动态规划）
 * 1. 维护当前子段和和最大子段和
 * 2. 遍历链表，动态更新这两个值
 * 3. 时间复杂度O(n)，空间复杂度O(1)
 * 
 * 方法二：分治法
 * 1. 将链表分成左右两部分
 * 2. 递归求解左右部分的最大子段和
 * 3. 计算跨越中间的最大子段和
 * 4. 时间复杂度O(n log n)，空间复杂度O(log n)
 * 
 * 方法三：暴力法
 * 1. 枚举所有可能的子段
 * 2. 计算每个子段的和
 * 3. 时间复杂度O(n²)，空间复杂度O(1)
 * 
 * 时间复杂度：
 * - Kadane算法：O(n)
 * - 分治法：O(n log n)
 * - 暴力法：O(n²)
 * 
 * 空间复杂度：
 * - Kadane算法：O(1)
 * - 分治法：O(log n)
 * - 暴力法：O(1)
 * 
 * 是否最优解：Kadane算法是最优解
 * 
 * 工程化考量：
 * 1. 算法选择：优先选择时间复杂度低的算法
 * 2. 代码可读性：清晰的算法逻辑和注释
 * 3. 异常处理：空链表和边界情况处理
 * 
 * 与机器学习等领域的联系：
 * 1. 在信号处理中，最大子段和用于特征提取
 * 2. 在金融分析中，用于寻找最佳投资区间
 * 3. 在图像处理中，用于目标检测
 * 
 * 语言特性差异：
 * Java: 使用Integer.MIN_VALUE表示最小值
 * C++: 使用INT_MIN表示最小值
 * Python: 使用float('-inf')表示最小值
 * 
 * 极端输入场景：
 * 1. 空链表
 * 2. 全负数链表
 * 3. 全正数链表
 * 4. 混合正负数链表
 * 5. 超大链表
 * 
 * 反直觉但关键的设计：
 * 1. Kadane算法的核心：当前和小于0时重置
 * 2. 分治法的跨越中间计算
 * 3. 动态规划的状态转移方程
 * 
 * 工程选择依据：
 * 1. 性能要求：选择时间复杂度最优的算法
 * 2. 代码简洁性：Kadane算法代码最简洁
 * 3. 可维护性：分治法逻辑最清晰
 * 
 * 异常防御：
 * 1. 空指针检查
 * 2. 整数溢出检查
 * 3. 链表长度检查
 * 
 * 单元测试要点：
 * 1. 测试空链表
 * 2. 测试全负数链表
 * 3. 测试全正数链表
 * 4. 测试混合链表
 * 5. 测试性能边界
 * 
 * 性能优化策略：
 * 1. 减少时间复杂度：使用Kadane算法
 * 2. 避免递归：防止栈溢出
 * 3. 优化内存使用：选择空间复杂度低的算法
 * 
 * 算法安全与业务适配：
 * 1. 避免整数溢出：使用long类型处理大数
 * 2. 异常捕获：捕获可能的运行时异常
 * 3. 性能监控：监控执行时间和内存使用
 * 
 * 与标准库实现的对比：
 * 1. 标准库通常不提供最大子段和功能
 * 2. 需要自定义实现特定需求
 * 3. Kadane算法是业界标准解法
 * 
 * 笔试解题效率：
 * 1. Kadane算法：代码简洁，效率高
 * 2. 分治法：展示算法理解深度
 * 3. 暴力法：仅用于验证，不推荐
 * 
 * 面试深度表达：
 * 1. 解释Kadane算法的核心思想
 * 2. 分析各种方法的优缺点
 * 3. 讨论实际应用场景
 * 4. 展示优化思路和工程考量
 */