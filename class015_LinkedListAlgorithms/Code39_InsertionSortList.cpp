// 对链表进行插入排序 - LeetCode 147
// 测试链接: https://leetcode.cn/problems/insertion-sort-list/
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
    // 方法1: 标准插入排序实现
    ListNode* insertionSortList(ListNode* head) {
        // 边界条件：空链表或只有一个节点
        if (!head || !head->next) {
            return head;
        }
        
        // 创建哑节点，简化头节点的处理
        ListNode dummy(0);
        dummy.next = head;
        
        // 已排序部分的最后一个节点
        ListNode* lastSorted = head;
        // 当前待插入的节点
        ListNode* curr = head->next;
        
        while (curr) {
            if (lastSorted->val <= curr->val) {
                // 如果当前节点值大于等于已排序部分的最后一个节点值
                // 则当前节点已经在正确的位置
                lastSorted = lastSorted->next;
            } else {
                // 找到合适的插入位置
                ListNode* prev = &dummy;
                while (prev->next->val <= curr->val) {
                    prev = prev->next;
                }
                
                // 保存下一个待处理节点
                lastSorted->next = curr->next;
                // 插入当前节点到正确位置
                curr->next = prev->next;
                prev->next = curr;
            }
            // 更新当前待处理节点
            curr = lastSorted->next;
        }
        
        return dummy.next;
    }
    
    // 方法2: 优化的插入排序（减少不必要的比较）
    ListNode* insertionSortListOptimized(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        ListNode dummy(INT_MIN); // 使用INT_MIN作为哑节点的值，避免比较时的边界检查
        ListNode* curr = head;
        
        while (curr) {
            // 保存下一个待处理节点
            ListNode* next = curr->next;
            
            // 从已排序部分的头部开始查找插入位置
            ListNode* prev = &dummy;
            while (prev->next && prev->next->val <= curr->val) {
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
    
    // 方法3: 拆分成两个链表的方式
    ListNode* insertionSortListSplit(ListNode* head) {
        if (!head || !head->next) {
            return head;
        }
        
        // 已排序链表的头节点
        ListNode dummy(0);
        
        // 遍历原链表
        while (head) {
            // 保存下一个待处理节点
            ListNode* next = head->next;
            
            // 在已排序链表中查找插入位置
            ListNode* curr = &dummy;
            while (curr->next && curr->next->val < head->val) {
                curr = curr->next;
            }
            
            // 插入到已排序链表中
            head->next = curr->next;
            curr->next = head;
            
            // 处理原链表的下一个节点
            head = next;
        }
        
        return dummy.next;
    }
    
    // 方法4: 递归实现的插入排序
    ListNode* insertionSortListRecursive(ListNode* head) {
        // 基本情况：空链表或只有一个节点
        if (!head || !head->next) {
            return head;
        }
        
        // 递归排序剩余部分
        head->next = insertionSortListRecursive(head->next);
        
        // 插入当前节点到已排序的部分
        return insertNode(head);
    }
    
private:
    // 辅助函数：将节点插入到正确的位置
    ListNode* insertNode(ListNode* head) {
        // 如果当前节点已经在正确位置
        if (!head || !head->next || head->val <= head->next->val) {
            return head;
        }
        
        // 创建哑节点
        ListNode dummy(0);
        dummy.next = head;
        ListNode* curr = &dummy;
        
        // 保存要插入的节点
        ListNode* toInsert = head;
        
        // 找到插入位置
        while (curr->next && curr->next->val < toInsert->val) {
            curr = curr->next;
        }
        
        // 如果当前节点已经在正确位置，直接返回
        if (curr->next == toInsert) {
            return head;
        }
        
        // 重新连接节点
        head = head->next;
        toInsert->next = curr->next;
        curr->next = toInsert;
        
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
    
    // 测试标准插入排序
    ListNode* head1_copy1 = copyList(head1);
    ListNode* result1 = solution.insertionSortList(head1_copy1);
    cout << "标准插入排序结果: ";
    printList(result1);
    
    // 测试优化的插入排序
    ListNode* head1_copy2 = copyList(head1);
    ListNode* result1_optimized = solution.insertionSortListOptimized(head1_copy2);
    cout << "优化插入排序结果: ";
    printList(result1_optimized);
    
    // 测试拆分链表的方式
    ListNode* head1_copy3 = copyList(head1);
    ListNode* result1_split = solution.insertionSortListSplit(head1_copy3);
    cout << "拆分链表方式结果: ";
    printList(result1_split);
    
    // 测试递归实现
    ListNode* head1_copy4 = copyList(head1);
    ListNode* result1_recursive = solution.insertionSortListRecursive(head1_copy4);
    cout << "递归插入排序结果: ";
    printList(result1_recursive);
    
    // 测试用例2: [-1,5,3,4,0]
    vector<int> nums2 = {-1, 5, 3, 4, 0};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    
    ListNode* result2 = solution.insertionSortList(head2);
    cout << "标准插入排序结果: ";
    printList(result2);
    
    // 测试用例3: []
    ListNode* head3 = nullptr;
    cout << "\n测试用例3:\n原始链表: 空链表" << endl;
    
    ListNode* result3 = solution.insertionSortList(head3);
    cout << "标准插入排序结果: ";
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
    
    ListNode* result4 = solution.insertionSortList(head4);
    cout << "标准插入排序结果: ";
    printList(result4);
    
    // 测试用例5: [5,4,3,2,1]
    vector<int> nums5 = {5, 4, 3, 2, 1};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    
    ListNode* result5 = solution.insertionSortList(head5);
    cout << "标准插入排序结果: ";
    printList(result5);
    
    // 测试用例6: [1,1,1,2,2]
    vector<int> nums6 = {1, 1, 1, 2, 2};
    ListNode* head6 = buildList(nums6);
    cout << "\n测试用例6:\n原始链表: ";
    printList(head6);
    
    ListNode* result6 = solution.insertionSortList(head6);
    cout << "标准插入排序结果: ";
    printList(result6);
    
    // 释放内存
    freeList(head1);
    freeList(result1);
    freeList(result1_optimized);
    freeList(result1_split);
    freeList(result1_recursive);
    freeList(result2);
    // result3已经是空，无需释放
    freeList(result4);
    freeList(result5);
    freeList(result6);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 147. 对链表进行插入排序
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给定单个链表的头 head，使用 插入排序 对链表进行排序，并返回 排序后链表的头 。
 * 插入排序 算法的步骤:
 * 1. 插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。
 * 2. 每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。
 * 3. 重复直到所有输入数据插入完为止。
 * 
 * 解题思路：
 * 1. 标准插入排序：维护已排序部分和未排序部分，每次从未排序部分取一个节点，插入到已排序部分的正确位置
 * 2. 优化插入排序：使用最小值作为哑节点值，减少边界检查
 * 3. 拆分链表：将原链表拆分为已排序和未排序两个链表
 * 4. 递归实现：递归排序剩余部分，然后插入当前节点
 * 
 * 时间复杂度：
 * 所有方法的时间复杂度都是 O(n²)，其中n是链表长度
 * 
 * 空间复杂度：
 * - 标准插入排序、优化插入排序、拆分链表方式：O(1)，只使用常数额外空间
 * - 递归实现：O(n)，递归调用栈的深度
 * 
 * 最优解：标准插入排序或优化插入排序，空间复杂度O(1)，实现简单直观
 * 
 * 工程化考量：
 * 1. 插入排序对于小规模数据或基本有序的数据效率较高
 * 2. 使用哑节点可以简化链表操作，特别是处理头节点的情况
 * 3. 注意指针操作的正确性，避免链表断裂
 * 4. 对于大规模数据，插入排序效率较低，建议使用归并排序等更高效的算法
 * 
 * 与机器学习等领域的联系：
 * 1. 插入排序是一种稳定的排序算法，在某些需要保持相等元素相对顺序的场景中有用
 * 2. 在增量学习和在线学习中，插入排序的思想可以用于更新模型
 * 3. 链表操作是基础数据结构操作，在很多算法中都会用到
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，注意指针操作的安全性
 * Java: 有自动内存管理，使用引用来操作链表
 * Python: 没有指针概念，但可以通过对象引用来模拟链表操作
 * 
 * 算法深度分析：
 * 插入排序对链表来说是一种自然的排序方法，因为链表的插入操作可以在O(1)时间内完成（不考虑查找位置的时间）。与数组上的插入排序相比，链表版本不需要移动元素，只需要修改指针，这是一个优势。然而，查找插入位置仍然需要O(n)时间，导致整体时间复杂度为O(n²)。对于基本有序的链表，插入排序的性能会接近O(n)，这是其优势所在。在实际应用中，如果链表规模较小或者预期接近有序，插入排序是一个不错的选择；否则，应该考虑使用归并排序等O(n log n)的算法。
 */