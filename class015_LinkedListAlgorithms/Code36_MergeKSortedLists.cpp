// 合并K个升序链表 - LeetCode 23
// 测试链接: https://leetcode.cn/problems/merge-k-sorted-lists/
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
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
    // 方法1: 优先级队列（最小堆）法
    ListNode* mergeKLists(vector<ListNode*>& lists) {
        // 处理边界情况
        if (lists.empty()) {
            return nullptr;
        }
        
        // 自定义比较函数，创建最小堆
        auto compare = [](ListNode* a, ListNode* b) {
            return a->val > b->val; // 小顶堆，注意这里是大于号
        };
        
        priority_queue<ListNode*, vector<ListNode*>, decltype(compare)> minHeap(compare);
        
        // 将所有链表的头节点加入最小堆
        for (ListNode* list : lists) {
            if (list) { // 确保节点不为空
                minHeap.push(list);
            }
        }
        
        // 创建哑节点作为结果链表的头节点
        ListNode dummy(0);
        ListNode* curr = &dummy;
        
        // 循环取出堆顶元素（最小值），并将其下一个节点加入堆
        while (!minHeap.empty()) {
            ListNode* top = minHeap.top();
            minHeap.pop();
            
            curr->next = top; // 将当前最小值节点加入结果链表
            curr = curr->next;
            
            if (top->next) { // 如果取出的节点还有下一个节点，将其加入堆
                minHeap.push(top->next);
            }
        }
        
        return dummy.next;
    }
    
    // 方法2: 分治法（两两合并）
    ListNode* mergeKListsDivideAndConquer(vector<ListNode*>& lists) {
        if (lists.empty()) {
            return nullptr;
        }
        
        int n = lists.size();
        // 分治合并，每次合并相邻的两个链表
        while (n > 1) {
            int mid = (n + 1) / 2; // 处理奇数的情况
            for (int i = 0; i < n / 2; i++) {
                lists[i] = mergeTwoLists(lists[i], lists[i + mid]);
            }
            n = mid;
        }
        
        return lists[0];
    }
    
    // 方法3: 暴力解法（将所有节点值存入数组，排序后重新构建链表）
    ListNode* mergeKListsBruteForce(vector<ListNode*>& lists) {
        // 存储所有节点值
        vector<int> values;
        
        // 遍历所有链表，收集节点值
        for (ListNode* list : lists) {
            while (list) {
                values.push_back(list->val);
                list = list->next;
            }
        }
        
        // 对节点值进行排序
        sort(values.begin(), values.end());
        
        // 构建新的排序链表
        ListNode dummy(0);
        ListNode* curr = &dummy;
        for (int val : values) {
            curr->next = new ListNode(val);
            curr = curr->next;
        }
        
        return dummy.next;
    }
    
    // 方法4: 迭代法（依次合并两个链表）
    ListNode* mergeKListsIterative(vector<ListNode*>& lists) {
        if (lists.empty()) {
            return nullptr;
        }
        
        ListNode* result = lists[0];
        
        // 依次合并后续的链表
        for (int i = 1; i < lists.size(); i++) {
            result = mergeTwoLists(result, lists[i]);
        }
        
        return result;
    }
    
private:
    // 辅助函数：合并两个有序链表
    ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
        ListNode dummy(0);
        ListNode* curr = &dummy;
        
        while (l1 && l2) {
            if (l1->val <= l2->val) {
                curr->next = l1;
                l1 = l1->next;
            } else {
                curr->next = l2;
                l2 = l2->next;
            }
            curr = curr->next;
        }
        
        // 连接剩余节点
        curr->next = l1 ? l1 : l2;
        
        return dummy.next;
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
    return dummy.next;
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

// 辅助函数：释放链表数组内存
void freeLists(vector<ListNode*>& lists) {
    for (ListNode* list : lists) {
        freeList(list);
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [[1,4,5],[1,3,4],[2,6]]
    vector<int> nums1 = {1, 4, 5};
    vector<int> nums2 = {1, 3, 4};
    vector<int> nums3 = {2, 6};
    vector<ListNode*> lists1 = {buildList(nums1), buildList(nums2), buildList(nums3)};
    
    cout << "测试用例1:\n原始链表:" << endl;
    for (int i = 0; i < lists1.size(); i++) {
        cout << "链表" << i + 1 << ": ";
        printList(lists1[i]);
    }
    
    ListNode* result1 = solution.mergeKLists(lists1);
    cout << "\n方法1（最小堆）合并结果: ";
    printList(result1);
    
    // 重新构建链表数组用于其他方法测试
    vector<ListNode*> lists1_copy = {buildList(nums1), buildList(nums2), buildList(nums3)};
    ListNode* result1_divide = solution.mergeKListsDivideAndConquer(lists1_copy);
    cout << "方法2（分治法）合并结果: ";
    printList(result1_divide);
    
    vector<ListNode*> lists1_copy2 = {buildList(nums1), buildList(nums2), buildList(nums3)};
    ListNode* result1_brute = solution.mergeKListsBruteForce(lists1_copy2);
    cout << "方法3（暴力解法）合并结果: ";
    printList(result1_brute);
    
    vector<ListNode*> lists1_copy3 = {buildList(nums1), buildList(nums2), buildList(nums3)};
    ListNode* result1_iterative = solution.mergeKListsIterative(lists1_copy3);
    cout << "方法4（迭代法）合并结果: ";
    printList(result1_iterative);
    
    // 测试用例2: []
    vector<ListNode*> lists2 = {};
    ListNode* result2 = solution.mergeKLists(lists2);
    cout << "\n测试用例2:\n原始链表: []" << endl;
    cout << "合并结果: ";
    if (result2) {
        printList(result2);
    } else {
        cout << "空链表" << endl;
    }
    
    // 测试用例3: [[]]
    vector<ListNode*> lists3 = {nullptr};
    ListNode* result3 = solution.mergeKLists(lists3);
    cout << "\n测试用例3:\n原始链表: [[]]" << endl;
    cout << "合并结果: ";
    if (result3) {
        printList(result3);
    } else {
        cout << "空链表" << endl;
    }
    
    // 测试用例4: [[3,5,7],[2,4,6],[1,8,9]]
    vector<int> nums4 = {3, 5, 7};
    vector<int> nums5 = {2, 4, 6};
    vector<int> nums6 = {1, 8, 9};
    vector<ListNode*> lists4 = {buildList(nums4), buildList(nums5), buildList(nums6)};
    
    cout << "\n测试用例4:\n原始链表:" << endl;
    for (int i = 0; i < lists4.size(); i++) {
        cout << "链表" << i + 1 << ": ";
        printList(lists4[i]);
    }
    
    ListNode* result4 = solution.mergeKLists(lists4);
    cout << "\n方法1（最小堆）合并结果: ";
    printList(result4);
    
    // 释放内存
    freeList(result1);
    freeList(result1_divide);
    freeList(result1_brute);
    freeList(result1_iterative);
    freeLists(lists1);
    freeLists(lists1_copy);
    freeLists(lists1_copy2);
    freeLists(lists1_copy3);
    freeLists(lists4);
    freeList(result4);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 23. 合并K个升序链表
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 
 * 解题思路：
 * 1. 最小堆法：使用优先级队列（最小堆）存储每个链表的头节点，每次取出最小节点加入结果链表
 * 2. 分治法：将K个链表两两合并，递归地减少问题规模
 * 3. 暴力解法：将所有节点值存入数组，排序后重新构建链表
 * 4. 迭代法：依次合并两个链表，最终得到一个合并后的链表
 * 
 * 时间复杂度：
 * - 最小堆法：O(N log K)，其中N是所有节点的总数，K是链表的数量
 * - 分治法：O(N log K)
 * - 暴力解法：O(N log N)
 * - 迭代法：O(N*K)
 * 
 * 空间复杂度：
 * - 最小堆法：O(K)
 * - 分治法：O(log K)，递归调用栈的深度
 * - 暴力解法：O(N)
 * - 迭代法：O(1)
 * 
 * 最优解：最小堆法和分治法都是最优的，时间复杂度为O(N log K)
 * 当K较大时，最小堆法更直观；当K较小时，分治法也很高效
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表数组、包含空链表的数组
 * 2. 内存管理：在C++中需要正确创建和释放链表节点
 * 3. 性能优化：避免不必要的节点创建和释放
 * 4. 代码可读性：不同方法各有优缺点，需要根据具体场景选择
 * 
 * 与机器学习等领域的联系：
 * 1. 多路归并问题在外部排序中非常常见
 * 2. 在分布式系统中，合并多个数据源的排序结果可以使用类似的方法
 * 3. 堆结构在优先队列、任务调度等场景中有广泛应用
 * 
 * 语言特性差异：
 * C++: 需要手动定义比较函数，可以使用lambda表达式和decltype
 * Java: 使用PriorityQueue，需要实现Comparator接口
 * Python: 使用heapq模块，需要将自定义对象转换为可比较的形式
 * 
 * 算法深度分析：
 * 合并K个有序链表是一个经典的多路归并问题。最小堆法的关键在于利用堆的性质，始终能够在O(log K)时间内找到K个链表头中的最小值。分治法则是将问题分解为更小的子问题，利用了归并排序的思想。两种方法的时间复杂度都是O(N log K)，但在实际应用中可能会有常数因子的差异。
 */