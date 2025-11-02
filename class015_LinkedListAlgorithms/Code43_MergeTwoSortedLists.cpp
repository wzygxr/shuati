// 合并两个有序链表
// 测试链接: https://leetcode.cn/problems/merge-two-sorted-lists/

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
    // 合并两个有序链表
    // 方法：双指针法，比较两个链表的节点值，选择较小的节点连接到结果链表
    // 时间复杂度：O(m+n) - m和n分别是两个链表的长度
    // 空间复杂度：O(1) - 只使用常数额外空间
    // 参数：
    //   list1 - 第一个有序链表
    //   list2 - 第二个有序链表
    // 返回值：合并后的有序链表
    ListNode* mergeTwoLists(ListNode* list1, ListNode* list2) {
        // 创建虚拟头节点，简化边界情况处理
        ListNode* dummy = new ListNode(0);
        // 当前节点指针
        ListNode* current = dummy;
        
        // 当两个链表都未遍历完时继续循环
        while (list1 != nullptr && list2 != nullptr) {
            // 比较两个链表当前节点的值，选择较小的节点连接到结果链表
            if (list1->val <= list2->val) {
                current->next = list1;
                list1 = list1->next;
            } else {
                current->next = list2;
                list2 = list2->next;
            }
            // 移动指针
            current = current->next;
        }
        
        // 将剩余的节点连接到结果链表
        if (list1 != nullptr) {
            current->next = list1;
        } else {
            current->next = list2;
        }
        
        // 获取结果并释放虚拟头节点
        ListNode* result = dummy->next;
        delete dummy;
        
        // 返回合并后的链表头节点
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
    if (nums.size() == 0) return nullptr;
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
    
    // 测试用例1: [1,2,4] + [1,3,4] = [1,1,2,3,4,4]
    vector<int> nums1 = {1, 2, 4};
    vector<int> nums2 = {1, 3, 4};
    ListNode* list1 = buildList(nums1);
    ListNode* list2 = buildList(nums2);
    cout << "测试用例1 - list1: ";
    printList(list1);
    cout << "测试用例1 - list2: ";
    printList(list2);
    ListNode* result1 = solution.mergeTwoLists(list1, list2);
    cout << "合并结果: ";
    printList(result1);
    freeList(result1);
    
    // 测试用例2: [] + [] = []
    vector<int> nums3 = {};
    vector<int> nums4 = {};
    ListNode* list3 = buildList(nums3);
    ListNode* list4 = buildList(nums4);
    cout << "测试用例2 - list1: ";
    printList(list3);
    cout << "测试用例2 - list2: ";
    printList(list4);
    ListNode* result2 = solution.mergeTwoLists(list3, list4);
    cout << "合并结果: ";
    printList(result2);
    freeList(result2);
    
    // 测试用例3: [] + [0] = [0]
    vector<int> nums5 = {};
    vector<int> nums6 = {0};
    ListNode* list5 = buildList(nums5);
    ListNode* list6 = buildList(nums6);
    cout << "测试用例3 - list1: ";
    printList(list5);
    cout << "测试用例3 - list2: ";
    printList(list6);
    ListNode* result3 = solution.mergeTwoLists(list5, list6);
    cout << "合并结果: ";
    printList(result3);
    freeList(result3);
    
    return 0;
}

/*
 * 题目：LeetCode 21. 合并两个有序链表
 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
 * 链接：https://leetcode.cn/problems/merge-two-sorted-lists/
 * 
 * 题目描述：
 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * 
 * 解题思路：
 * 使用双指针法，比较两个链表的节点值，选择较小的节点连接到结果链表。
 * 
 * 时间复杂度：O(m+n) - m和n分别是两个链表的长度
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 是否最优解：是
 * 
 * 工程化考量：
 * 1. 使用虚拟头节点简化边界情况处理
 * 2. 边界情况处理：空链表、不同长度链表等
 * 3. 异常处理：输入参数校验
 * 4. 内存管理：正确释放动态分配的内存
 * 
 * 与机器学习等领域的联系：
 * 1. 在归并排序算法中，合并两个有序序列是核心步骤
 * 2. 在多路归并中，需要合并多个有序序列
 * 
 * 语言特性差异：
 * Java: 对象引用操作简单，垃圾回收自动管理内存
 * C++: 需要手动管理内存，注意指针操作
 * Python: 使用对象引用，无需手动管理内存
 * 
 * 极端输入场景：
 * 1. 空链表
 * 2. 单节点链表
 * 3. 非常长的链表
 * 4. 全相同元素链表
 * 5. 一个链表为空，另一个链表非空
 */