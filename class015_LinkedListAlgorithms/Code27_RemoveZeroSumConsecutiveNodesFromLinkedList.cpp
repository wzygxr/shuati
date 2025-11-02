// 删除零和连续节点 - LeetCode 1171
// 测试链接: https://leetcode.cn/problems/remove-zero-sum-consecutive-nodes-from-linked-list/
#include <iostream>
#include <vector>
#include <unordered_map>
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
    ListNode* removeZeroSumSublists(ListNode* head) {
        // 创建哑节点，简化头节点的处理
        ListNode* dummy = new ListNode(0);
        dummy->next = head;
        
        // 前缀和哈希表，键为前缀和，值为对应的节点
        unordered_map<int, ListNode*> prefixSum;
        int sum = 0;
        
        // 第一次遍历，记录每个前缀和的最后一次出现位置
        ListNode* curr = dummy;
        while (curr) {
            sum += curr->val;
            prefixSum[sum] = curr; // 后面出现的相同前缀和会覆盖前面的
            curr = curr->next;
        }
        
        // 重置前缀和，第二次遍历删除零和子链表
        sum = 0;
        curr = dummy;
        while (curr) {
            sum += curr->val;
            // 直接跳到相同前缀和的最后一个位置的下一个节点
            curr->next = prefixSum[sum]->next;
            curr = curr->next;
        }
        
        // 保存结果并释放哑节点
        ListNode* result = dummy->next;
        delete dummy;
        
        return result;
    }
    
    // 方法2：暴力解法（用于理解和验证）
    ListNode* removeZeroSumSublistsBruteForce(ListNode* head) {
        if (!head) return nullptr;
        
        // 创建哑节点
        ListNode* dummy = new ListNode(0);
        dummy->next = head;
        
        // 外层循环遍历每个起始位置
        for (ListNode* i = dummy; i != nullptr; i = i->next) {
            int sum = 0;
            // 内层循环查找以i为起点的零和子链表
            for (ListNode* j = i->next; j != nullptr; ) {
                sum += j->val;
                if (sum == 0) {
                    // 找到零和子链表，删除从i->next到j的所有节点
                    ListNode* temp = i->next;
                    i->next = j->next;
                    // 释放删除的节点内存
                    while (temp != j) {
                        ListNode* nodeToDelete = temp;
                        temp = temp->next;
                        delete nodeToDelete;
                    }
                    delete j; // 释放j节点
                    j = i->next; // 继续从新的i->next开始查找
                } else {
                    j = j->next;
                }
            }
        }
        
        // 保存结果并释放哑节点
        ListNode* result = dummy->next;
        delete dummy;
        
        return result;
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

// 辅助函数：打印链表
void printList(ListNode* head) {
    while (head) {
        cout << head->val;
        if (head->next) {
            cout << " -> ";
        }
        head = head->next;
    }
    cout << endl;
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
    
    // 测试用例1: [1,2,-3,3,1]
    vector<int> nums1 = {1, 2, -3, 3, 1};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    ListNode* result1 = solution.removeZeroSumSublists(head1);
    cout << "删除零和子链表后: ";
    printList(result1);
    freeList(result1);
    
    // 测试用例2: [1,2,3,-3,4]
    vector<int> nums2 = {1, 2, 3, -3, 4};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    ListNode* result2 = solution.removeZeroSumSublists(head2);
    cout << "删除零和子链表后: ";
    printList(result2);
    freeList(result2);
    
    // 测试用例3: [1,2,3,-3,-2]
    vector<int> nums3 = {1, 2, 3, -3, -2};
    ListNode* head3 = buildList(nums3);
    cout << "\n测试用例3:\n原始链表: ";
    printList(head3);
    ListNode* result3 = solution.removeZeroSumSublists(head3);
    cout << "删除零和子链表后: ";
    printList(result3);
    freeList(result3);
    
    // 测试用例4: 空链表
    ListNode* head4 = nullptr;
    cout << "\n测试用例4:\n原始链表: 空链表" << endl;
    ListNode* result4 = solution.removeZeroSumSublists(head4);
    cout << "删除零和子链表后: ";
    printList(result4);
    
    // 测试用例5: [0]
    vector<int> nums5 = {0};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    ListNode* result5 = solution.removeZeroSumSublists(head5);
    cout << "删除零和子链表后: ";
    printList(result5);
    freeList(result5);
    
    // 测试用例6: [1,-1]
    vector<int> nums6 = {1, -1};
    ListNode* head6 = buildList(nums6);
    cout << "\n测试用例6:\n原始链表: ";
    printList(head6);
    ListNode* result6 = solution.removeZeroSumSublistsBruteForce(head6);
    cout << "暴力解法删除零和子链表后: ";
    printList(result6);
    freeList(result6);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 1171. 删除零和连续节点
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 给你一个链表的头节点 head，请你编写代码，反复删去链表中由 总和 值为 0 的连续节点组成的序列，直到不存在这样的序列为止。
 * 删除完毕后，请你返回最终结果链表的头节点。
 * 你可以返回任何满足题目要求的答案。
 * （注意，下面示例中的所有序列，都是对 ListNode 对象序列化的表示。）
 * 
 * 解题思路（前缀和哈希表法）：
 * 1. 前缀和的性质：如果两个位置的前缀和相等，那么这两个位置之间的子数组和为0
 * 2. 第一次遍历：使用哈希表记录每个前缀和的最后一次出现位置
 * 3. 第二次遍历：对于每个前缀和，直接跳到该前缀和最后一次出现位置的下一个节点
 * 4. 这样就删除了中间和为0的子链表
 * 
 * 时间复杂度：O(n) - 需要遍历链表两次
 * 空间复杂度：O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 解题思路（暴力解法）：
 * 1. 对于每个起始位置，尝试查找以该位置开始的和为0的子链表
 * 2. 如果找到，删除该子链表并重新开始查找
 * 3. 否则继续查找下一个起始位置
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(1)
 * 
 * 最优解：前缀和哈希表法，时间复杂度更低
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 异常处理：确保指针操作的安全性
 * 3. 内存管理：在C++中需要正确释放删除的节点内存
 * 4. 代码可读性：前缀和方法思路巧妙，需要清晰的注释
 * 
 * 与机器学习等领域的联系：
 * 1. 前缀和技术在时间序列分析中经常使用
 * 2. 哈希表在数据处理和缓存中有广泛应用
 * 3. 类似的思想可以应用于异常检测和模式识别
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用unordered_map实现哈希表
 * Java: 可以使用HashMap，垃圾回收机制自动处理内存
 */