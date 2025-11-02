// 排序链表 - LeetCode 148
// 测试链接: https://leetcode.cn/problems/sort-list/
#include <iostream>
#include <vector>
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
    // 方法1: 归并排序（递归版）- 自顶向下
    ListNode* sortList(ListNode* head) {
        // 边界条件：空链表或只有一个节点
        if (!head || !head->next) {
            return head;
        }
        
        // 找到链表的中点，分割链表
        ListNode* slow = head;
        ListNode* fast = head->next; // 这样分割可以保证slow指向左半部分的最后一个节点
        
        while (fast && fast->next) {
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 分割链表为两部分
        ListNode* mid = slow->next;
        slow->next = nullptr;
        
        // 递归排序左右两部分
        ListNode* left = sortList(head);
        ListNode* right = sortList(mid);
        
        // 合并两个有序链表
        return merge(left, right);
    }
    
    // 方法2: 归并排序（迭代版）- 自底向上
    ListNode* sortListIterative(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 计算链表长度
        int length = 0;
        ListNode* curr = head;
        while (curr) {
            length++;
            curr = curr->next;
        }
        
        ListNode dummy(0);
        dummy.next = head;
        
        // 自底向上归并，步长从1开始，每次翻倍
        for (int step = 1; step < length; step *= 2) {
            ListNode* prev = &dummy;
            curr = dummy.next;
            
            while (curr) {
                // 第一个子链表的头部
                ListNode* left = curr;
                // 分割出第一个子链表（长度为step）
                for (int i = 1; i < step && curr->next; i++) {
                    curr = curr->next;
                }
                
                // 第二个子链表的头部
                ListNode* right = curr->next;
                // 断开第一个子链表
                curr->next = nullptr;
                
                // 分割出第二个子链表（长度为step）
                curr = right;
                for (int i = 1; i < step && curr && curr->next; i++) {
                    curr = curr->next;
                }
                
                // 保存下一轮的起始节点
                ListNode* nextStart = nullptr;
                if (curr) {
                    nextStart = curr->next;
                    curr->next = nullptr; // 断开第二个子链表
                }
                
                // 合并两个子链表
                prev->next = merge(left, right);
                
                // 移动prev到合并后链表的末尾
                while (prev->next) {
                    prev = prev->next;
                }
                
                // 处理剩余节点
                curr = nextStart;
            }
        }
        
        return dummy.next;
    }
    
    // 方法3: 快速排序
    ListNode* sortListQuickSort(ListNode* head) {
        // 边界条件
        if (!head || !head->next) {
            return head;
        }
        
        // 快速排序
        quickSort(head, nullptr);
        
        return head;
    }
    
    // 方法4: 插入排序
    ListNode* sortListInsertionSort(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        ListNode dummy(0);
        ListNode* curr = head;
        ListNode* prev;
        ListNode* next;
        
        while (curr) {
            next = curr->next; // 保存下一个要处理的节点
            
            // 找到插入位置
            prev = &dummy;
            while (prev->next && prev->next->val < curr->val) {
                prev = prev->next;
            }
            
            // 插入当前节点
            curr->next = prev->next;
            prev->next = curr;
            
            // 移动到下一个节点
            curr = next;
        }
        
        return dummy.next;
    }
    
    // 方法5: 转换为数组排序后重建链表
    ListNode* sortListArray(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 收集链表中的所有值
        vector<int> values;
        ListNode* curr = head;
        while (curr) {
            values.push_back(curr->val);
            curr = curr->next;
        }
        
        // 排序数组
        sort(values.begin(), values.end());
        
        // 重建链表
        curr = head;
        for (int val : values) {
            curr->val = val;
            curr = curr->next;
        }
        
        return head;
    }
private:
    // 辅助函数：合并两个有序链表
    ListNode* merge(ListNode* l1, ListNode* l2) {
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
    
    // 辅助函数：快速排序实现
    void quickSort(ListNode* head, ListNode* tail) {
        // 递归终止条件
        if (head == tail || head->next == tail) {
            return;
        }
        
        // 选择第一个节点作为基准值
        int pivot = head->val;
        ListNode* slow = head;
        ListNode* fast = head->next;
        
        // 分区过程
        while (fast != tail) {
            if (fast->val < pivot) {
                slow = slow->next;
                swap(slow->val, fast->val);
            }
            fast = fast->next;
        }
        
        // 将基准值放到正确的位置
        swap(head->val, slow->val);
        
        // 递归排序左右两部分
        quickSort(head, slow);
        quickSort(slow->next, tail);
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

// 复制链表用于多方法测试
ListNode* copyList(ListNode* head) {
    if (!head) return nullptr;
    ListNode* dummy = new ListNode(0);
    ListNode* curr = dummy;
    while (head) {
        curr->next = new ListNode(head->val);
        curr = curr->next;
        head = head->next;
    }
    return dummy.next;
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [4,2,1,3]
    vector<int> nums1 = {4, 2, 1, 3};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    
    // 测试递归归并排序
    ListNode* head1_copy1 = copyList(head1);
    ListNode* result1 = solution.sortList(head1_copy1);
    cout << "归并排序（递归）结果: ";
    printList(result1);
    
    // 测试迭代归并排序
    ListNode* head1_copy2 = copyList(head1);
    ListNode* result1_iterative = solution.sortListIterative(head1_copy2);
    cout << "归并排序（迭代）结果: ";
    printList(result1_iterative);
    
    // 测试快速排序
    ListNode* head1_copy3 = copyList(head1);
    ListNode* result1_quick = solution.sortListQuickSort(head1_copy3);
    cout << "快速排序结果: ";
    printList(result1_quick);
    
    // 测试插入排序
    ListNode* head1_copy4 = copyList(head1);
    ListNode* result1_insertion = solution.sortListInsertionSort(head1_copy4);
    cout << "插入排序结果: ";
    printList(result1_insertion);
    
    // 测试数组排序法
    ListNode* head1_copy5 = copyList(head1);
    ListNode* result1_array = solution.sortListArray(head1_copy5);
    cout << "数组排序法结果: ";
    printList(result1_array);
    
    // 测试用例2: [-1,5,3,4,0]
    vector<int> nums2 = {-1, 5, 3, 4, 0};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    
    ListNode* result2 = solution.sortList(head2);
    cout << "归并排序（递归）结果: ";
    printList(result2);
    
    // 测试用例3: []
    ListNode* head3 = nullptr;
    cout << "\n测试用例3:\n原始链表: 空链表" << endl;
    
    ListNode* result3 = solution.sortList(head3);
    cout << "归并排序（递归）结果: ";
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
    
    ListNode* result4 = solution.sortList(head4);
    cout << "归并排序（递归）结果: ";
    printList(result4);
    
    // 测试用例5: [5,4,3,2,1]
    vector<int> nums5 = {5, 4, 3, 2, 1};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    
    // 测试各种排序方法
    ListNode* head5_copy1 = copyList(head5);
    ListNode* result5_recursive = solution.sortList(head5_copy1);
    cout << "归并排序（递归）结果: ";
    printList(result5_recursive);
    
    ListNode* head5_copy2 = copyList(head5);
    ListNode* result5_iterative = solution.sortListIterative(head5_copy2);
    cout << "归并排序（迭代）结果: ";
    printList(result5_iterative);
    
    // 释放内存
    freeList(head1);
    freeList(result1);
    freeList(result1_iterative);
    freeList(result1_quick);
    freeList(result1_insertion);
    freeList(result1_array);
    freeList(result2);
    // result3已经是空，无需释放
    freeList(result4);
    freeList(head5);
    freeList(result5_recursive);
    freeList(result5_iterative);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 148. 排序链表
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给你链表的头结点 head，请将其按 升序 排列并返回 排序后的链表 。
 * 要求：在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 * 
 * 解题思路：
 * 1. 归并排序（递归版）：自顶向下，使用快慢指针找到中点，分割链表，递归排序，合并
 * 2. 归并排序（迭代版）：自底向上，不需要递归调用栈，满足O(1)空间复杂度要求
 * 3. 快速排序：选择基准值，分区，递归排序
 * 4. 插入排序：对于小规模数据可能更快，但时间复杂度较高
 * 5. 转换为数组排序：将链表转换为数组，排序后重建链表
 * 
 * 时间复杂度：
 * - 归并排序（递归）：O(n log n)
 * - 归并排序（迭代）：O(n log n)
 * - 快速排序：平均O(n log n)，最坏O(n²)
 * - 插入排序：O(n²)
 * - 数组排序法：O(n log n)
 * 
 * 空间复杂度：
 * - 归并排序（递归）：O(log n)，递归调用栈的深度
 * - 归并排序（迭代）：O(1)，符合题目要求
 * - 快速排序：平均O(log n)，最坏O(n)
 * - 插入排序：O(1)
 * - 数组排序法：O(n)
 * 
 * 最优解：归并排序（迭代版），满足O(n log n)时间复杂度和O(1)空间复杂度的要求
 * 
 * 工程化考量：
 * 1. 对于本题要求，迭代版归并排序是最佳选择
 * 2. 在实际应用中，需要考虑链表长度、数据分布等因素选择合适的排序算法
 * 3. 注意内存管理，避免内存泄漏
 * 4. 对于大规模数据，自底向上的归并排序在空间效率上更优
 * 
 * 与机器学习等领域的联系：
 * 1. 排序算法是计算机科学的基础，在数据预处理、特征工程中广泛应用
 * 2. 归并排序的分治思想在分布式系统、并行计算中有重要应用
 * 3. 链表作为一种数据结构，在哈希表、图等高级数据结构中也有应用
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，递归深度过大会导致栈溢出
 * Java: 有自动内存管理，但递归深度也受限制
 * Python: 递归深度有上限（默认1000），可能需要手动调整
 * 
 * 算法深度分析：
 * 对于链表排序，归并排序是一种天然适合的算法，因为链表的合并操作可以在O(1)空间复杂度下完成。递归版的归并排序虽然代码简洁，但空间复杂度为O(log n)；而迭代版的归并排序通过自底向上的方式，避免了递归调用栈，达到了O(1)的空间复杂度，完美符合题目的要求。快速排序在链表上实现相对复杂，且最坏情况下性能较差。插入排序虽然简单，但时间复杂度较高，不适合大规模数据。
 */