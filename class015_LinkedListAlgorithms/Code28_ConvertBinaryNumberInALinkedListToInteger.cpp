// 将链表二进制转换为整数 - LeetCode 1290
// 测试链接: https://leetcode.cn/problems/convert-binary-number-in-a-linked-list-to-integer/
#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

// 定义链表节点结构
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Solution {
public:
    // 方法1: 两次遍历法
    int getDecimalValue(ListNode* head) {
        // 第一次遍历，计算链表长度
        int length = 0;
        ListNode* curr = head;
        while (curr) {
            length++;
            curr = curr->next;
        }
        
        // 第二次遍历，计算十进制值
        int result = 0;
        curr = head;
        for (int i = length - 1; i >= 0; i--) {
            result += curr->val * pow(2, i);
            curr = curr->next;
        }
        
        return result;
    }
    
    // 方法2: 一次遍历法（位运算优化）
    int getDecimalValueOptimized(ListNode* head) {
        int result = 0;
        ListNode* curr = head;
        
        while (curr) {
            // 每次将结果左移一位（相当于乘以2），然后加上当前节点的值
            result = (result << 1) | curr->val;
            curr = curr->next;
        }
        
        return result;
    }
    
    // 方法3: 一次遍历法（算术运算）
    int getDecimalValueArithmetic(ListNode* head) {
        int result = 0;
        ListNode* curr = head;
        
        while (curr) {
            // 每次将结果乘以2，然后加上当前节点的值
            result = result * 2 + curr->val;
            curr = curr->next;
        }
        
        return result;
    }
    
    // 方法4: 递归解法
    int getDecimalValueRecursive(ListNode* head) {
        int length = 0;
        ListNode* curr = head;
        while (curr) { // 先计算长度
            length++;
            curr = curr->next;
        }
        
        return helper(head, length - 1);
    }
    
private:
    int helper(ListNode* node, int power) {
        if (!node) return 0;
        return node->val * pow(2, power) + helper(node->next, power - 1);
    }
};

// 辅助函数：构建链表
ListNode* buildList(vector<int>& nums) {
    ListNode* dummy = new ListNode(0);
    ListNode* curr = dummy;
    for (int num : nums) {
        curr->next = new ListNode(num);
        curr = curr->next;
    }
    return dummy->next;
}

// 辅助函数：释放链表内存
void freeList(ListNode* head) {
    while (head) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [1,0,1]
    vector<int> nums1 = {1, 0, 1};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n二进制链表: 1 -> 0 -> 1" << endl;
    cout << "两次遍历法结果: " << solution.getDecimalValue(head1) << endl;
    cout << "位运算优化结果: " << solution.getDecimalValueOptimized(head1) << endl;
    cout << "算术运算结果: " << solution.getDecimalValueArithmetic(head1) << endl;
    cout << "递归解法结果: " << solution.getDecimalValueRecursive(head1) << endl;
    freeList(head1);
    
    // 测试用例2: [0]
    vector<int> nums2 = {0};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n二进制链表: 0" << endl;
    cout << "两次遍历法结果: " << solution.getDecimalValue(head2) << endl;
    cout << "位运算优化结果: " << solution.getDecimalValueOptimized(head2) << endl;
    freeList(head2);
    
    // 测试用例3: [1]
    vector<int> nums3 = {1};
    ListNode* head3 = buildList(nums3);
    cout << "\n测试用例3:\n二进制链表: 1" << endl;
    cout << "两次遍历法结果: " << solution.getDecimalValue(head3) << endl;
    cout << "位运算优化结果: " << solution.getDecimalValueOptimized(head3) << endl;
    freeList(head3);
    
    // 测试用例4: [1,0,0,1,0,0,1,1,1,0,0,0,0,0,0]
    vector<int> nums4 = {1,0,0,1,0,0,1,1,1,0,0,0,0,0,0};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n二进制链表: 1->0->0->1->0->0->1->1->1->0->0->0->0->0->0" << endl;
    cout << "两次遍历法结果: " << solution.getDecimalValue(head4) << endl;
    cout << "位运算优化结果: " << solution.getDecimalValueOptimized(head4) << endl;
    cout << "算术运算结果: " << solution.getDecimalValueArithmetic(head4) << endl;
    cout << "递归解法结果: " << solution.getDecimalValueRecursive(head4) << endl;
    freeList(head4);
    
    // 测试用例5: [1,0,1,1]
    vector<int> nums5 = {1,0,1,1};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n二进制链表: 1->0->1->1" << endl;
    cout << "两次遍历法结果: " << solution.getDecimalValue(head5) << endl;
    cout << "位运算优化结果: " << solution.getDecimalValueOptimized(head5) << endl;
    freeList(head5);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 1290. 将链表二进制转换为整数
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 给你一个单链表的引用结点 head。链表中每个结点的值不是 0 就是 1。已知此链表是一个整数数字的二进制表示形式。
 * 请你返回该链表所表示数字的十进制值。
 * 
 * 解题思路：
 * 1. 两次遍历法：先计算链表长度，再根据二进制位权计算十进制值
 * 2. 一次遍历法（位运算）：使用位运算高效地计算十进制值
 * 3. 一次遍历法（算术运算）：使用乘法和加法计算十进制值
 * 4. 递归解法：递归计算每个位的贡献
 * 
 * 时间复杂度：
 * - 两次遍历法：O(n)
 * - 一次遍历法（位运算和算术运算）：O(n)
 * - 递归解法：O(n)
 * 
 * 空间复杂度：
 * - 两次遍历法：O(1)
 * - 一次遍历法（位运算和算术运算）：O(1)
 * - 递归解法：O(n) - 递归调用栈的深度
 * 
 * 最优解：一次遍历法（位运算），因为它既高效又避免了浮点数运算
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 性能优化：位运算通常比算术运算更快
 * 3. 数据范围：需要考虑结果可能超出int范围的情况（但题目保证结果在int范围内）
 * 4. 代码可读性：不同方法有不同的可读性和效率权衡
 * 
 * 与机器学习等领域的联系：
 * 1. 二进制表示在计算机系统中广泛使用
 * 2. 位运算在图像处理、数据压缩中有重要应用
 * 3. 类似的转换问题在编码解码中常见
 * 
 * 语言特性差异：
 * C++: 位运算效率高，使用<<运算符实现左移
 * Java: 位运算实现类似，但类型转换规则略有不同
 * Python: 支持大整数，不需要考虑溢出问题
 * 
 * 算法深度分析：
 * 位运算优化的核心思想是利用位移操作的高效性。左移一位相当于乘以2，结合或运算（|）可以在不使用乘法的情况下高效计算二进制到十进制的转换。
 */