// 分割链表 - LeetCode 725
// 测试链接: https://leetcode.cn/problems/split-linked-list-in-parts/
#include <iostream>
#include <vector>
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
    vector<ListNode*> splitListToParts(ListNode* head, int k) {
        // 计算链表长度
        int length = 0;
        ListNode* curr = head;
        while (curr) {
            length++;
            curr = curr->next;
        }
        
        // 计算每部分的大小和余数
        // baseSize是每部分至少包含的节点数
        // remainder是有多少部分需要多包含一个节点
        int baseSize = length / k;
        int remainder = length % k;
        
        vector<ListNode*> result(k, nullptr);
        curr = head;
        
        // 分配每个部分的节点
        for (int i = 0; i < k && curr; i++) {
            result[i] = curr; // 当前部分的头节点
            
            // 计算当前部分的长度
            int partSize = baseSize + (i < remainder ? 1 : 0);
            
            // 移动到当前部分的最后一个节点
            for (int j = 1; j < partSize; j++) {
                curr = curr->next;
            }
            
            // 断开当前部分与下一部分的连接
            ListNode* next = curr->next;
            curr->next = nullptr;
            curr = next;
        }
        
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

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: head = [1,2,3], k = 5
    vector<int> nums1 = {1, 2, 3};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    vector<ListNode*> result1 = solution.splitListToParts(head1, 5);
    cout << "分割后: " << endl;
    for (int i = 0; i < result1.size(); i++) {
        cout << "第" << (i + 1) << "部分: ";
        printList(result1[i]);
    }
    
    // 测试用例2: head = [1,2,3,4,5,6,7,8,9,10], k = 3
    vector<int> nums2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    vector<ListNode*> result2 = solution.splitListToParts(head2, 3);
    cout << "分割后: " << endl;
    for (int i = 0; i < result2.size(); i++) {
        cout << "第" << (i + 1) << "部分: ";
        printList(result2[i]);
    }
    
    // 释放内存
    // 注意：在实际应用中需要更完整的内存管理
    return 0;
}

/*
 * 题目扩展：LeetCode 725. 分割链表
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 给你一个头结点为 head 的单链表和一个整数 k ，请你设计一个算法将链表分隔为 k 个连续的部分。
 * 每部分的长度应该尽可能的相等：任意两部分的长度差距不能超过 1 。这可能会导致有些部分为 null 。
 * 这 k 个部分应该按照在链表中出现的顺序排列，并且排在前面的部分的长度应该大于或等于排在后面的长度。
 * 返回一个由上述 k 部分组成的数组。
 * 
 * 解题思路：
 * 1. 计算链表总长度
 * 2. 计算每部分的基础长度和余数
 * 3. 遍历链表，按照计算出的长度分割链表
 * 4. 注意处理链表断开的操作
 * 
 * 时间复杂度：O(n) - 需要遍历链表计算长度，然后再次遍历分割链表
 * 空间复杂度：O(1) - 不考虑返回结果的空间，只使用常数额外空间
 * 
 * 最优解：此解法已经是最优解
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、k大于链表长度
 * 2. 异常处理：确保指针操作的安全性
 * 3. 代码可读性：逻辑清晰，注释充分
 * 4. 内存管理：在C++中需要注意内存泄漏问题
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，注意指针操作的安全性
 */