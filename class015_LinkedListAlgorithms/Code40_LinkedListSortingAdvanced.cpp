// 链表排序高级算法综合实现
// 包括：链表的归并排序（更优化版本）、链表快速排序优化、链表基数排序
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cmath>
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
    // 方法1: 优化的归并排序（迭代版）
    ListNode* mergeSortOptimized(ListNode* head) {
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
    
    // 方法2: 优化的快速排序（三数取中法选择枢轴）
    ListNode* quickSortOptimized(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 使用三数取中法选择枢轴并将其移动到链表头部
        choosePivotMedianOfThree(head);
        
        // 快速排序
        ListNode* newHead = nullptr;
        ListNode* newTail = nullptr;
        quickSortHelper(head, &newHead, &newTail);
        
        return newHead;
    }
    
    // 方法3: 链表基数排序（仅适用于非负整数）
    ListNode* radixSort(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 找出链表中的最大值
        int maxVal = INT_MIN;
        ListNode* curr = head;
        while (curr) {
            // 只处理非负整数
            if (curr->val < 0) {
                cerr << "基数排序仅支持非负整数" << endl;
                return nullptr;
            }
            maxVal = max(maxVal, curr->val);
            curr = curr->next;
        }
        
        // 计算最大值的位数
        int maxDigits = 0;
        while (maxVal > 0) {
            maxDigits++;
            maxVal /= 10;
        }
        
        // 基数排序
        ListNode dummy(0);
        dummy.next = head;
        int exp = 1; // 当前处理的位数（个位、十位、百位...）
        
        for (int i = 0; i < maxDigits; i++) {
            // 创建10个桶（0-9）
            vector<ListNode*> buckets(10, nullptr);
            vector<ListNode*> bucketTails(10, nullptr);
            
            // 将节点分配到桶中
            curr = dummy.next;
            while (curr) {
                ListNode* next = curr->next;
                int digit = (curr->val / exp) % 10;
                
                if (!buckets[digit]) {
                    buckets[digit] = curr;
                    bucketTails[digit] = curr;
                } else {
                    bucketTails[digit]->next = curr;
                    bucketTails[digit] = curr;
                }
                curr->next = nullptr; // 断开原链表连接
                curr = next;
            }
            
            // 重新连接链表
            ListNode* tail = &dummy;
            for (int j = 0; j < 10; j++) {
                if (buckets[j]) {
                    tail->next = buckets[j];
                    tail = bucketTails[j];
                }
            }
            
            exp *= 10; // 处理下一位
        }
        
        return dummy.next;
    }
    
    // 方法4: 链表堆排序
    ListNode* heapSort(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 使用最小堆
        auto compare = [](ListNode* a, ListNode* b) {
            return a->val > b->val; // 小顶堆
        };
        priority_queue<ListNode*, vector<ListNode*>, decltype(compare)> minHeap(compare);
        
        // 将所有节点加入堆
        ListNode* curr = head;
        while (curr) {
            minHeap.push(curr);
            curr = curr->next;
        }
        
        // 重新构建链表
        ListNode dummy(0);
        curr = &dummy;
        while (!minHeap.empty()) {
            curr->next = minHeap.top();
            minHeap.pop();
            curr = curr->next;
        }
        curr->next = nullptr; // 确保链表结束
        
        return dummy.next;
    }
    
    // 方法5: 链表计数排序（适用于小范围整数）
    ListNode* countingSort(ListNode* head, int rangeStart, int rangeEnd) {
        if (!head || !head->next) {
            return head;
        }
        
        // 创建计数数组
        int range = rangeEnd - rangeStart + 1;
        vector<int> count(range, 0);
        
        // 统计每个元素出现的次数
        ListNode* curr = head;
        while (curr) {
            if (curr->val < rangeStart || curr->val > rangeEnd) {
                cerr << "元素超出指定范围" << endl;
                return nullptr;
            }
            count[curr->val - rangeStart]++;
            curr = curr->next;
        }
        
        // 重建链表
        curr = head;
        for (int i = 0; i < range; i++) {
            while (count[i] > 0) {
                curr->val = i + rangeStart;
                curr = curr->next;
                count[i]--;
            }
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
        
        curr->next = l1 ? l1 : l2;
        return dummy.next;
    }
    
    // 辅助函数：三数取中法选择枢轴
    void choosePivotMedianOfThree(ListNode* head) {
        if (!head || !head->next || !head->next->next) {
            return; // 链表太短，不需要选择枢轴
        }
        
        ListNode* mid = head;
        ListNode* tail = head;
        
        // 找到链表的中间节点和尾节点
        while (tail->next && tail->next->next) {
            mid = mid->next;
            tail = tail->next->next;
        }
        if (tail->next) {
            tail = tail->next; // 处理偶数长度的情况
        }
        
        // 现在有三个候选节点：head, mid, tail
        // 选择这三个节点中的中间值作为枢轴
        ListNode* median = nullptr;
        
        if ((head->val >= mid->val && head->val <= tail->val) || 
            (head->val <= mid->val && head->val >= tail->val)) {
            median = head;
        } else if ((mid->val >= head->val && mid->val <= tail->val) || 
                   (mid->val <= head->val && mid->val >= tail->val)) {
            median = mid;
        } else {
            median = tail;
        }
        
        // 将枢轴节点的值与头节点交换
        if (median != head) {
            swap(head->val, median->val);
        }
    }
    
    // 辅助函数：快速排序实现
    void quickSortHelper(ListNode* head, ListNode** newHead, ListNode** newTail) {
        // 基本情况
        if (!head) {
            *newHead = nullptr;
            *newTail = nullptr;
            return;
        }
        
        // 分区：小于枢轴、等于枢轴、大于枢轴
        ListNode dummyLess(0), dummyEqual(0), dummyGreater(0);
        ListNode *lessTail = &dummyLess, *equalTail = &dummyEqual, *greaterTail = &dummyGreater;
        
        int pivot = head->val; // 枢轴值
        ListNode* curr = head;
        
        while (curr) {
            ListNode* next = curr->next;
            curr->next = nullptr;
            
            if (curr->val < pivot) {
                lessTail->next = curr;
                lessTail = curr;
            } else if (curr->val > pivot) {
                greaterTail->next = curr;
                greaterTail = curr;
            } else {
                equalTail->next = curr;
                equalTail = curr;
            }
            
            curr = next;
        }
        
        // 递归排序小于和大于枢轴的部分
        ListNode *lessHead = nullptr, *lessTail = nullptr;
        ListNode *greaterHead = nullptr, *greaterTail = nullptr;
        
        quickSortHelper(dummyLess.next, &lessHead, &lessTail);
        quickSortHelper(dummyGreater.next, &greaterHead, &greaterTail);
        
        // 连接三个部分
        *newHead = nullptr;
        *newTail = nullptr;
        
        // 连接小于部分
        if (lessHead) {
            *newHead = lessHead;
            lessTail->next = dummyEqual.next;
        } else {
            *newHead = dummyEqual.next;
        }
        
        // 连接大于部分
        if (greaterHead) {
            equalTail->next = greaterHead;
            *newTail = greaterTail;
        } else {
            *newTail = equalTail;
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
    
    // 测试用例1: [4,2,1,3,5]
    vector<int> nums1 = {4, 2, 1, 3, 5};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    
    // 测试优化的归并排序
    ListNode* head1_copy1 = copyList(head1);
    ListNode* result1_merge = solution.mergeSortOptimized(head1_copy1);
    cout << "优化的归并排序结果: ";
    printList(result1_merge);
    
    // 测试优化的快速排序
    ListNode* head1_copy2 = copyList(head1);
    ListNode* result1_quick = solution.quickSortOptimized(head1_copy2);
    cout << "优化的快速排序结果: ";
    printList(result1_quick);
    
    // 测试堆排序
    ListNode* head1_copy3 = copyList(head1);
    ListNode* result1_heap = solution.heapSort(head1_copy3);
    cout << "堆排序结果: ";
    printList(result1_heap);
    
    // 测试用例2: [10, 5, 3, 1, 8, 9] - 用于基数排序测试
    vector<int> nums2 = {10, 5, 3, 1, 8, 9};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    
    // 测试基数排序
    ListNode* head2_copy1 = copyList(head2);
    ListNode* result2_radix = solution.radixSort(head2_copy1);
    cout << "基数排序结果: ";
    if (result2_radix) {
        printList(result2_radix);
    } else {
        cout << "基数排序失败（可能包含负数）" << endl;
    }
    
    // 测试计数排序
    ListNode* head2_copy2 = copyList(head2);
    ListNode* result2_counting = solution.countingSort(head2_copy2, 0, 20);
    cout << "计数排序结果: ";
    if (result2_counting) {
        printList(result2_counting);
    } else {
        cout << "计数排序失败（元素超出范围）" << endl;
    }
    
    // 测试用例3: [3,1,4,1,5,9,2,6] - 较大规模测试
    vector<int> nums3 = {3, 1, 4, 1, 5, 9, 2, 6};
    ListNode* head3 = buildList(nums3);
    cout << "\n测试用例3:\n原始链表: ";
    printList(head3);
    
    ListNode* result3_merge = solution.mergeSortOptimized(head3);
    cout << "优化的归并排序结果: ";
    printList(result3_merge);
    
    // 测试用例4: [5,5,5,5,5] - 所有元素相同
    vector<int> nums4 = {5, 5, 5, 5, 5};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n原始链表: ";
    printList(head4);
    
    ListNode* result4_quick = solution.quickSortOptimized(head4);
    cout << "优化的快速排序结果: ";
    printList(result4_quick);
    
    // 释放内存
    freeList(head1);
    freeList(result1_merge);
    freeList(result1_quick);
    freeList(result1_heap);
    freeList(head2);
    if (result2_radix) freeList(result2_radix);
    if (result2_counting) freeList(result2_counting);
    freeList(result3_merge);
    freeList(result4_quick);
    
    return 0;
}

/*
 * 题目扩展：链表高级排序算法综合
 * 来源：LeetCode、LintCode、牛客网、剑指Offer等综合题目
 * 
 * 题目描述：
 * 实现多种高级排序算法在链表上的应用，包括优化的归并排序、快速排序、基数排序、堆排序和计数排序。
 * 
 * 解题思路：
 * 1. 优化的归并排序：自底向上的归并排序，避免递归调用栈，空间复杂度O(1)
 * 2. 优化的快速排序：使用三数取中法选择枢轴，三路划分处理相等元素
 * 3. 基数排序：从低位到高位，使用桶排序的思想对每一位进行排序
 * 4. 堆排序：使用优先队列（最小堆）存储所有节点，然后重新构建链表
 * 5. 计数排序：适用于小范围整数的高效排序算法
 * 
 * 时间复杂度：
 * - 优化的归并排序：O(n log n)
 * - 优化的快速排序：平均O(n log n)，最坏O(n²)
 * - 基数排序：O(n * k)，其中k是最大元素的位数
 * - 堆排序：O(n log n)
 * - 计数排序：O(n + k)，其中k是数据范围
 * 
 * 空间复杂度：
 * - 优化的归并排序：O(1)
 * - 优化的快速排序：平均O(log n)，最坏O(n)
 * - 基数排序：O(n + 10)
 * - 堆排序：O(n)
 * - 计数排序：O(k)
 * 
 * 最优解选择：
 * - 对于一般情况，归并排序是最稳定的选择，时间复杂度O(n log n)，迭代版空间复杂度O(1)
 * - 对于接近有序的数据，插入排序可能更高效
 * - 对于小范围整数，计数排序或基数排序性能更好
 * - 对于随机数据，优化的快速排序可能表现不错
 * 
 * 工程化考量：
 * 1. 根据数据特点选择合适的排序算法
 * 2. 注意内存管理，避免内存泄漏
 * 3. 处理边界情况，如空链表、单节点链表
 * 4. 对于不同范围的数据，选择适合的排序方法
 * 5. 考虑算法的稳定性需求
 * 
 * 与机器学习等领域的联系：
 * 1. 排序算法是数据预处理的基础操作
 * 2. 在特征选择和特征排序中经常使用
 * 3. 高效的排序算法对大规模数据处理至关重要
 * 4. 分治思想在分布式机器学习中有广泛应用
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用STL容器辅助实现各种排序算法
 * Java: 有自动内存管理，提供了Collections.sort等工具方法
 * Python: 内置排序函数效率很高，且有丰富的数据结构支持
 * 
 * 算法深度分析：
 * 链表排序相比数组排序有其独特的挑战和优势。链表的插入操作不需要移动元素，只需要修改指针，这使得某些排序算法（如插入排序）在链表上的实现更加高效。然而，链表不支持随机访问，这使得某些依赖数组随机访问特性的排序算法（如快速排序的某些实现）在链表上效率较低。归并排序由于其分治特性和高效的合并操作，是链表排序的理想选择。优化的快速排序通过三数取中法和三路划分，可以减少最坏情况的发生概率。基数排序和计数排序在数据范围有限的情况下，可以达到接近线性的时间复杂度，是非常高效的排序方法。
 */