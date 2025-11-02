#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * 相关题目10: LeetCode 23. 合并K个升序链表
 * 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 解题思路: 使用最小堆维护K个链表的头节点，每次从堆中取出最小值，并将其下一个节点加入堆中
 * 时间复杂度: O(N log K)，其中N是所有节点的总数，K是链表的数量
 * 空间复杂度: O(K)，堆中最多存储K个节点
 * 是否最优解: 是，这是合并K个有序链表的最优解法之一
 * 
 * 本题属于堆的典型应用场景：多源有序数据的合并
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
    /**
     * 使用最小堆合并K个有序链表
     * @param lists K个有序链表的数组
     * @return 合并后的有序链表头节点
     */
    ListNode* mergeKLists(vector<ListNode*>& lists) {
        // 异常处理：检查输入数组是否为空
        if (lists.empty()) {
            return nullptr;
        }
        
        // 边界情况：如果只有一个链表，直接返回
        if (lists.size() == 1) {
            return lists[0];
        }
        
        // 自定义比较器，使优先队列成为最小堆
        auto compare = [](ListNode* a, ListNode* b) {
            return a->val > b->val; // 注意：这里返回a->val > b->val是为了让优先队列按升序排列
        };
        
        // 创建一个最小堆，存储ListNode*，按节点值升序排列
        priority_queue<ListNode*, vector<ListNode*>, decltype(compare)> minHeap(compare);
        
        // 初始化：将所有链表的头节点加入堆中（如果不为nullptr）
        for (ListNode* list : lists) {
            if (list != nullptr) {
                minHeap.push(list);
            }
        }
        
        // 创建一个哑节点作为合并后链表的头节点前一个节点
        ListNode* dummy = new ListNode(-1);
        ListNode* curr = dummy;
        
        // 不断从堆中取出最小值节点，直到堆为空
        while (!minHeap.empty()) {
            // 取出堆顶元素（当前最小值节点）
            ListNode* minNode = minHeap.top();
            minHeap.pop();
            
            // 将最小值节点添加到结果链表中
            curr->next = minNode;
            curr = curr->next;
            
            // 如果当前最小值节点还有下一个节点，将下一个节点加入堆中
            if (minNode->next != nullptr) {
                minHeap.push(minNode->next);
            }
        }
        
        // 保存结果头节点
        ListNode* result = dummy->next;
        // 释放哑节点的内存
        delete dummy;
        
        // 返回合并后链表的头节点
        return result;
    }
    
    /**
     * 递归方式合并两个有序链表
     * @param l1 第一个有序链表的头节点
     * @param l2 第二个有序链表的头节点
     * @return 合并后的有序链表头节点
     */
    ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
        if (l1 == nullptr) return l2;
        if (l2 == nullptr) return l1;
        
        if (l1->val <= l2->val) {
            l1->next = mergeTwoLists(l1->next, l2);
            return l1;
        } else {
            l2->next = mergeTwoLists(l1, l2->next);
            return l2;
        }
    }
    
    /**
     * 使用分治法合并K个有序链表
     * @param lists K个有序链表的数组
     * @return 合并后的有序链表头节点
     */
    ListNode* mergeKListsDivideConquer(vector<ListNode*>& lists) {
        if (lists.empty()) {
            return nullptr;
        }
        
        int n = lists.size();
        return mergeKLists(lists, 0, n - 1);
    }
    
private:
    /**
     * 分治法的递归实现
     * @param lists K个有序链表的数组
     * @param start 起始索引
     * @param end 结束索引
     * @return 合并后的有序链表头节点
     */
    ListNode* mergeKLists(vector<ListNode*>& lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        
        int mid = start + (end - start) / 2;
        ListNode* left = mergeKLists(lists, start, mid);
        ListNode* right = mergeKLists(lists, mid + 1, end);
        
        return mergeTwoLists(left, right);
    }
};

/**
 * 打印链表的辅助函数
 */
void printList(ListNode* head) {
    ListNode* curr = head;
    while (curr != nullptr) {
        cout << curr->val;
        if (curr->next != nullptr) {
            cout << " -> ";
        }
        curr = curr->next;
    }
    cout << endl;
}

/**
 * 创建链表的辅助函数
 */
ListNode* createList(const vector<int>& nums) {
    ListNode* dummy = new ListNode(-1);
    ListNode* curr = dummy;
    for (int num : nums) {
        curr->next = new ListNode(num);
        curr = curr->next;
    }
    ListNode* result = dummy->next;
    delete dummy; // 释放哑节点内存
    return result;
}

/**
 * 释放链表内存的辅助函数
 */
void deleteList(ListNode* head) {
    while (head != nullptr) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况
    ListNode* l1 = createList({1, 4, 5});
    ListNode* l2 = createList({1, 3, 4});
    ListNode* l3 = createList({2, 6});
    vector<ListNode*> lists1 = {l1, l2, l3};
    
    cout << "测试用例1（堆实现）: ";
    ListNode* result1 = solution.mergeKLists(lists1);
    printList(result1); // 期望输出: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
    
    // 释放结果链表内存
    deleteList(result1);
    
    // 重置测试用例1
    l1 = createList({1, 4, 5});
    l2 = createList({1, 3, 4});
    l3 = createList({2, 6});
    lists1 = {l1, l2, l3};
    
    cout << "测试用例1（分治实现）: ";
    ListNode* result1DivideConquer = solution.mergeKListsDivideConquer(lists1);
    printList(result1DivideConquer);
    
    // 释放结果链表内存
    deleteList(result1DivideConquer);
    
    // 测试用例2：空数组
    vector<ListNode*> lists2 = {};
    cout << "测试用例2: ";
    ListNode* result2 = solution.mergeKLists(lists2);
    printList(result2); // 期望输出: (空)
    
    // 测试用例3：包含空链表
    l1 = createList({1, 4, 5});
    l2 = createList({1, 3, 4});
    vector<ListNode*> lists3 = {nullptr, l1, nullptr, l2};
    cout << "测试用例3: ";
    ListNode* result3 = solution.mergeKLists(lists3);
    printList(result3); // 期望输出: 1 -> 1 -> 3 -> 4 -> 4 -> 5
    
    // 释放结果链表内存
    deleteList(result3);
    
    // 测试用例4：只有一个链表
    l3 = createList({2, 6});
    vector<ListNode*> lists4 = {l3};
    cout << "测试用例4: ";
    ListNode* result4 = solution.mergeKLists(lists4);
    printList(result4); // 期望输出: 2 -> 6
    
    // 释放结果链表内存
    deleteList(result4);
    
    return 0;
}