#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * 相关题目4: LeetCode 23. 合并K个排序链表
 * 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 解题思路: 使用最小堆维护K个链表的当前头节点，每次取出最小的节点并将其下一个节点加入堆
 * 时间复杂度: O(N log K)，其中N是所有链表的节点总数，K是链表的数量
 * 空间复杂度: O(K)，堆最多存储K个节点
 * 是否最优解: 是，这是合并K个排序链表的最优解法之一
 * 
 * 本题属于堆的典型应用场景：在多个有序集合中动态选择最小元素
 */

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
    // 自定义比较器，用于最小堆
    struct CompareNode {
        bool operator()(ListNode* a, ListNode* b) {
            // C++的优先队列默认是最大堆，所以我们使用大于号来实现最小堆
            return a->val > b->val;
        }
    };
    
    /**
     * 合并K个排序链表
     * @param lists K个排序链表的数组
     * @return 合并后的排序链表头节点
     * @throws invalid_argument 当输入数组为null时抛出异常
     */
    ListNode* mergeKLists(vector<ListNode*>& lists) {
        // 边界情况：数组为空或所有链表都为空
        int nonEmptyCount = 0;
        for (ListNode* list : lists) {
            if (list != nullptr) {
                nonEmptyCount++;
            }
        }
        
        if (nonEmptyCount == 0) {
            return nullptr; // 返回空链表
        }
        
        // 创建最小堆，按照节点值排序
        priority_queue<ListNode*, vector<ListNode*>, CompareNode> minHeap;
        
        // 将所有链表的头节点加入堆
        for (ListNode* list : lists) {
            if (list != nullptr) {
                minHeap.push(list);
                // 调试信息：打印加入堆的节点值
                // cout << "加入堆的节点值: " << list->val << endl;
            }
        }
        
        // 创建哑节点作为结果链表的头节点
        ListNode* dummy = new ListNode(-1);
        ListNode* current = dummy;
        
        // 不断从堆中取出最小的节点，直到堆为空
        while (!minHeap.empty()) {
            // 取出当前最小的节点
            ListNode* smallest = minHeap.top();
            minHeap.pop();
            
            // 调试信息：打印取出的节点值
            // cout << "取出的节点值: " << smallest->val << endl;
            
            // 将该节点加入结果链表
            current->next = smallest;
            current = current->next;
            
            // 如果该节点有下一个节点，则将其下一个节点加入堆
            if (smallest->next != nullptr) {
                minHeap.push(smallest->next);
                // 调试信息：打印新加入堆的节点值
                // cout << "新加入堆的节点值: " << smallest->next->val << endl;
            }
        }
        
        // 保存结果链表的头节点（跳过哑节点）
        ListNode* result = dummy->next;
        // 释放哑节点的内存
        delete dummy;
        
        return result;
    }
};

/**
 * 打印链表的辅助函数
 */
void printList(ListNode* head) {
    ListNode* current = head;
    while (current != nullptr) {
        cout << current->val;
        if (current->next != nullptr) {
            cout << " -> ";
        }
        current = current->next;
    }
    cout << endl;
}

/**
 * 释放链表内存的辅助函数
 */
void deleteList(ListNode* head) {
    ListNode* current = head;
    while (current != nullptr) {
        ListNode* temp = current;
        current = current->next;
        delete temp;
    }
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况
    // 创建链表1: 1->4->5
    ListNode* list1 = new ListNode(1, new ListNode(4, new ListNode(5)));
    // 创建链表2: 1->3->4
    ListNode* list2 = new ListNode(1, new ListNode(3, new ListNode(4)));
    // 创建链表3: 2->6
    ListNode* list3 = new ListNode(2, new ListNode(6));
    vector<ListNode*> lists1 = {list1, list2, list3};
    
    cout << "示例1输出: ";
    ListNode* result1 = solution.mergeKLists(lists1);
    printList(result1); // 期望输出: 1->1->2->3->4->4->5->6
    
    // 测试用例2：边界情况 - 空数组
    vector<ListNode*> lists2 = {};
    cout << "示例2输出: ";
    ListNode* result2 = solution.mergeKLists(lists2);
    printList(result2); // 期望输出: null
    
    // 测试用例3：边界情况 - 数组包含空链表
    vector<ListNode*> lists3 = {nullptr};
    cout << "示例3输出: ";
    ListNode* result3 = solution.mergeKLists(lists3);
    printList(result3); // 期望输出: null
    
    // 测试用例4：较大的K值
    ListNode* list4 = new ListNode(3);
    ListNode* list5 = new ListNode(2);
    ListNode* list6 = new ListNode(1);
    ListNode* list7 = new ListNode(4);
    vector<ListNode*> lists4 = {list4, list5, list6, list7};
    
    cout << "示例4输出: ";
    ListNode* result4 = solution.mergeKLists(lists4);
    printList(result4); // 期望输出: 1->2->3->4
    
    // 释放内存
    deleteList(result1);
    // result2 和 result3 已经是nullptr，无需释放
    deleteList(result4);
    
    return 0;
}