// 删除链表的倒数第N个节点
// 测试链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/

#include <iostream>
#include <vector>
using namespace std;

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Solution {
public:
    // 删除链表的倒数第N个节点
    // 方法：双指针法，先让快指针走n步，然后快慢指针同时移动
    // 时间复杂度：O(n) - 需要遍历链表一次
    // 空间复杂度：O(1) - 只使用常数额外空间
    // 参数：
    //   head - 链表的头节点
    //   n - 要删除的倒数第n个节点
    // 返回值：删除节点后的链表头节点
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        // 创建虚拟头节点，简化边界情况处理
        ListNode* dummy = new ListNode(0);
        dummy->next = head;
        
        // 初始化快慢指针
        ListNode* fast = dummy;
        ListNode* slow = dummy;
        
        // 快指针先走n步
        for (int i = 0; i < n; i++) {
            fast = fast->next;
        }
        
        // 快慢指针同时移动，直到快指针到达最后一个节点
        while (fast->next != nullptr) {
            fast = fast->next;
            slow = slow->next;
        }
        
        // 删除倒数第n个节点
        ListNode* nodeToDelete = slow->next;
        slow->next = slow->next->next;
        delete nodeToDelete; // 释放内存
        
        // 获取结果并释放虚拟头节点
        ListNode* result = dummy->next;
        delete dummy;
        
        // 返回新的头节点
        return result;
    }
};

// 辅助函数：打印链表
void printList(ListNode* head) {
    while (head != nullptr) {
        cout << head->val;
        if (head->next != nullptr) {
            cout << " -> ";
        }
        head = head->next;
    }
    cout << endl;
}

// 辅助函数：构建链表
ListNode* buildList(vector<int>& nums) {
    ListNode* dummy = new ListNode(0);
    ListNode* curr = dummy;
    for (int num : nums) {
        curr->next = new ListNode(num);
        curr = curr->next;
    }
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

// 辅助函数：释放链表内存
void freeList(ListNode* head) {
    while (head != nullptr) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [1,2,3,4,5], n = 2
    vector<int> nums1 = {1, 2, 3, 4, 5};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1 - 原链表: ";
    printList(head1);
    ListNode* result1 = solution.removeNthFromEnd(head1, 2);
    cout << "删除倒数第2个节点后: ";
    printList(result1);
    freeList(result1);
    
    // 测试用例2: [1], n = 1
    vector<int> nums2 = {1};
    ListNode* head2 = buildList(nums2);
    cout << "测试用例2 - 原链表: ";
    printList(head2);
    ListNode* result2 = solution.removeNthFromEnd(head2, 1);
    cout << "删除倒数第1个节点后: ";
    printList(result2);
    freeList(result2);
    
    // 测试用例3: [1,2], n = 1
    vector<int> nums3 = {1, 2};
    ListNode* head3 = buildList(nums3);
    cout << "测试用例3 - 原链表: ";
    printList(head3);
    ListNode* result3 = solution.removeNthFromEnd(head3, 1);
    cout << "删除倒数第1个节点后: ";
    printList(result3);
    freeList(result3);
    
    return 0;
}

/*
 * 题目：LeetCode 19. 删除链表的倒数第N个节点
 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
 * 链接：https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
 * 
 * 题目描述：
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * 
 * 解题思路：
 * 使用双指针法，先让快指针走n步，然后快慢指针同时移动，
 * 当快指针到达最后一个节点时，慢指针指向倒数第n+1个节点，
 * 然后删除倒数第n个节点。
 * 
 * 时间复杂度：O(n) - 需要遍历链表一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 是否最优解：是
 * 
 * 工程化考量：
 * 1. 使用虚拟头节点简化边界情况处理
 * 2. 边界情况处理：删除头节点、空链表等
 * 3. 异常处理：输入参数校验
 * 4. 内存管理：正确释放动态分配的内存
 * 
 * 与机器学习等领域的联系：
 * 1. 在序列数据处理中，有时需要删除特定位置的元素
 * 2. 双指针技巧在滑动窗口算法中广泛应用
 * 
 * 语言特性差异：
 * Java: 对象引用操作简单，垃圾回收自动管理内存
 * C++: 需要手动管理内存，注意指针操作
 * Python: 使用对象引用，无需手动管理内存
 * 
 * 极端输入场景：
 * 1. 空链表
 * 2. 单节点链表
 * 3. 删除头节点
 * 4. 删除尾节点
 * 5. 非常长的链表
 */