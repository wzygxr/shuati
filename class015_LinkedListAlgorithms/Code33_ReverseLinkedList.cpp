// 反转链表 - LeetCode 206
// 测试链接: https://leetcode.cn/problems/reverse-linked-list/
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
    // 方法1: 迭代法反转链表
    ListNode* reverseList(ListNode* head) {
        ListNode* prev = nullptr;  // 前一个节点，初始为nullptr
        ListNode* curr = head;     // 当前节点
        ListNode* nextTemp = nullptr; // 临时保存下一个节点
        
        while (curr) {
            nextTemp = curr->next;  // 保存当前节点的下一个节点
            curr->next = prev;      // 反转当前节点的指针
            prev = curr;            // 前一个节点前进
            curr = nextTemp;        // 当前节点前进
        }
        
        // 反转后的头节点是prev
        return prev;
    }
    
    // 方法2: 递归法反转链表
    ListNode* reverseListRecursive(ListNode* head) {
        // 基本情况：空链表或单节点链表，直接返回
        if (!head || !head->next) {
            return head;
        }
        
        // 递归反转剩余部分
        ListNode* newHead = reverseListRecursive(head->next);
        
        // 调整当前节点和下一个节点的指针关系
        head->next->next = head;  // 让下一个节点指向当前节点
        head->next = nullptr;     // 断开当前节点的原有next指针
        
        // 返回新的头节点（即原链表的尾节点）
        return newHead;
    }
    
    // 方法3: 双指针优化版（与方法1类似，但更简洁）
    ListNode* reverseListTwoPointers(ListNode* head) {
        ListNode* prev = nullptr;
        ListNode* curr = head;
        
        while (curr) {
            // 这行代码同时完成了三个操作：
            // 1. curr->next = prev（反转指针）
            // 2. prev = curr（前指针前进）
            // 3. curr = curr->next（当前指针前进）
            tie(curr->next, prev, curr) = make_tuple(prev, curr, curr->next);
        }
        
        return prev;
    }
    
    // 方法4: 使用栈（额外空间）
    ListNode* reverseListStack(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 使用栈存储链表节点
        vector<ListNode*> stack;
        ListNode* curr = head;
        
        // 将所有节点入栈
        while (curr) {
            stack.push_back(curr);
            curr = curr->next;
        }
        
        // 从栈顶依次弹出节点，重构链表
        head = stack.back();  // 新的头节点是原链表的尾节点
        stack.pop_back();
        curr = head;
        
        while (!stack.empty()) {
            curr->next = stack.back();
            stack.pop_back();
            curr = curr->next;
        }
        
        // 确保最后一个节点的next为nullptr
        curr->next = nullptr;
        
        return head;
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
    
    // 测试用例1: [1,2,3,4,5]
    vector<int> nums1 = {1, 2, 3, 4, 5};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    
    ListNode* result1 = solution.reverseList(head1);
    cout << "迭代法反转结果: ";
    printList(result1);
    
    // 重新构建链表
    ListNode* head1_copy = buildList(nums1);
    ListNode* result1_recursive = solution.reverseListRecursive(head1_copy);
    cout << "递归法反转结果: ";
    printList(result1_recursive);
    
    // 测试用例2: [1,2]
    vector<int> nums2 = {1, 2};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    
    ListNode* result2 = solution.reverseListTwoPointers(head2);
    cout << "双指针优化版反转结果: ";
    printList(result2);
    
    // 测试用例3: []
    ListNode* head3 = nullptr;
    cout << "\n测试用例3:\n原始链表: 空链表" << endl;
    
    ListNode* result3 = solution.reverseList(head3);
    cout << "迭代法反转结果: ";
    if (result3) {
        printList(result3);
    } else {
        cout << "空链表" << endl;
    }
    
    // 测试用例4: [1]
    vector<int> nums4 = {1};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n原始链表: ";
    printList(head4);
    
    ListNode* result4 = solution.reverseListStack(head4);
    cout << "栈方法反转结果: ";
    printList(result4);
    
    // 测试用例5: [5,4,3,2,1]
    vector<int> nums5 = {5, 4, 3, 2, 1};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    
    ListNode* result5 = solution.reverseList(head5);
    cout << "迭代法反转结果: ";
    printList(result5);
    
    // 释放内存
    freeList(result1);
    freeList(result1_recursive);
    freeList(result2);
    freeList(result4);
    freeList(result5);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 206. 反转链表
 * 来源：LeetCode、LintCode、剑指Offer、牛客网
 * 
 * 题目描述：
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 * 
 * 解题思路：
 * 1. 迭代法：使用三个指针（前驱、当前、后继）逐步反转链表
 * 2. 递归法：递归处理剩余部分，然后调整当前节点的指针
 * 3. 双指针优化版：使用更简洁的写法实现迭代法
 * 4. 栈方法：使用栈存储节点，然后重构链表
 * 
 * 时间复杂度：
 * - 迭代法：O(n)，其中n是链表长度
 * - 递归法：O(n)，递归调用栈的深度
 * - 双指针优化版：O(n)
 * - 栈方法：O(n)
 * 
 * 空间复杂度：
 * - 迭代法：O(1)，只使用常数额外空间
 * - 递归法：O(n)，递归调用栈的深度
 * - 双指针优化版：O(1)
 * - 栈方法：O(n)，需要额外的栈空间
 * 
 * 最优解：迭代法（方法1），时间复杂度O(n)，空间复杂度O(1)
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 内存管理：在C++中需要正确处理链表节点的内存
 * 3. 代码可读性：迭代法最为直观，递归法代码简洁但可能较难理解
 * 4. 性能优化：避免不必要的内存分配和函数调用
 * 
 * 与机器学习等领域的联系：
 * 1. 链表反转操作在数据结构转换中有广泛应用
 * 2. 在自然语言处理中，序列反转是一种常见的预处理操作
 * 3. 在图像处理中，像素行或列的反转也涉及类似概念
 * 
 * 语言特性差异：
 * C++: 使用指针操作，需要注意空指针检查
 * Java: 使用引用，自动内存管理
 * Python: 使用对象引用，语法更为简洁
 * 
 * 算法深度分析：
 * 反转链表是一个基础但重要的链表操作，它涉及到对指针（引用）的灵活运用。迭代法的关键在于保存下一个节点，避免链表断开后无法继续遍历。递归法则体现了分治法的思想，将问题分解为更小的子问题。
 */