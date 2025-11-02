// 删除排序链表中的重复元素II - LeetCode 82
// 测试链接: https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/
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
    ListNode* deleteDuplicates(ListNode* head) {
        // 处理边界情况
        if (!head || !head->next) {
            return head;
        }
        
        // 创建哑节点，简化头节点的处理
        ListNode* dummy = new ListNode(0);
        dummy->next = head;
        
        // prev指向当前不重复的最后一个节点
        ListNode* prev = dummy;
        // curr用于遍历链表
        ListNode* curr = head;
        
        while (curr) {
            // 标记当前节点是否重复
            bool hasDuplicate = false;
            
            // 跳过所有重复的节点
            while (curr->next && curr->val == curr->next->val) {
                hasDuplicate = true;
                ListNode* temp = curr;
                curr = curr->next;
                delete temp; // 释放重复节点的内存
            }
            
            if (hasDuplicate) {
                // 如果有重复，需要删除当前节点
                ListNode* temp = curr;
                curr = curr->next;
                delete temp; // 释放最后一个重复节点的内存
                prev->next = curr; // 跳过所有重复节点
            } else {
                // 如果没有重复，移动prev指针
                prev = curr;
                curr = curr->next;
            }
        }
        
        // 保存新的头节点
        ListNode* newHead = dummy->next;
        delete dummy; // 释放哑节点的内存
        
        return newHead;
    }
    
    // 方法2：递归版本
    ListNode* deleteDuplicatesRecursive(ListNode* head) {
        // 基本情况：空链表或单节点链表
        if (!head || !head->next) {
            return head;
        }
        
        // 如果当前节点与下一个节点重复
        if (head->val == head->next->val) {
            // 跳过所有重复的节点
            while (head->next && head->val == head->next->val) {
                ListNode* temp = head;
                head = head->next;
                delete temp; // 释放重复节点的内存
            }
            // 删除最后一个重复节点
            ListNode* temp = head;
            head = head->next;
            delete temp;
            // 递归处理剩余部分
            return deleteDuplicatesRecursive(head);
        } else {
            // 当前节点不重复，递归处理下一个节点
            head->next = deleteDuplicatesRecursive(head->next);
            return head;
        }
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
    
    // 测试用例1: [1,2,3,3,4,4,5]
    vector<int> nums1 = {1, 2, 3, 3, 4, 4, 5};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    ListNode* result1 = solution.deleteDuplicates(head1);
    cout << "删除重复元素后: ";
    printList(result1);
    freeList(result1);
    
    // 测试用例2: [1,1,1,2,3]
    vector<int> nums2 = {1, 1, 1, 2, 3};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    ListNode* result2 = solution.deleteDuplicatesRecursive(head2);
    cout << "删除重复元素后(递归): ";
    printList(result2);
    freeList(result2);
    
    // 测试用例3: []
    ListNode* head3 = nullptr;
    cout << "\n测试用例3:\n原始链表: 空链表" << endl;
    ListNode* result3 = solution.deleteDuplicates(head3);
    cout << "删除重复元素后: ";
    printList(result3);
    
    // 测试用例4: [1,1]
    vector<int> nums4 = {1, 1};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n原始链表: ";
    printList(head4);
    ListNode* result4 = solution.deleteDuplicates(head4);
    cout << "删除重复元素后: ";
    printList(result4);
    freeList(result4);
    
    // 测试用例5: [1]
    vector<int> nums5 = {1};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    ListNode* result5 = solution.deleteDuplicates(head5);
    cout << "删除重复元素后: ";
    printList(result5);
    freeList(result5);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 82. 删除排序链表中的重复元素II
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给定一个已排序的链表的头 head ，删除原始链表中所有重复数字的节点，只留下不同的数字 。返回已排序的链表。
 * 
 * 解题思路（迭代法）：
 * 1. 创建哑节点，简化头节点的处理
 * 2. 使用prev指针跟踪当前不重复的最后一个节点
 * 3. 使用curr指针遍历链表
 * 4. 当遇到重复节点时，跳过所有重复节点并删除它们
 * 5. 当没有重复节点时，移动prev指针
 * 
 * 时间复杂度：O(n) - 需要遍历链表一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * 解题思路（递归法）：
 * 1. 基本情况：空链表或单节点链表直接返回
 * 2. 如果当前节点与下一个节点重复，跳过所有重复节点并递归处理剩余部分
 * 3. 如果当前节点不重复，递归处理下一个节点
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n) - 递归调用栈的深度
 * 
 * 最优解：迭代法是最优解，空间复杂度更低
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 异常处理：确保指针操作的安全性
 * 3. 内存管理：在C++中需要正确释放删除的节点内存
 * 4. 代码可读性：迭代法逻辑清晰，递归法代码简洁
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，注意避免内存泄漏
 * Java: 垃圾回收机制会自动处理内存释放
 */