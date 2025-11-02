#include <iostream>
#include <vector>
#include <stack>
#include <unordered_set>
#include <chrono>
#include <stdexcept>
#include <cstdlib>
#include <ctime>

using namespace std;

/**
 * 链表反转相关算法题目集合
 * 包含LeetCode、牛客网、Codeforces、LintCode、HackerRank等平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析和多种解法
 * 
 * 题目列表：
 * 1. 反转链表 (LeetCode 206, 牛客网, HackerRank, LintCode 35, 剑指Offer 24)
 * 2. 反转链表 II (LeetCode 92)
 * 3. K个一组翻转链表 (LeetCode 25)
 * 4. 回文链表 (LeetCode 234, LintCode 223, 牛客网 NC78)
 * 5. 旋转链表 (LeetCode 61, LintCode 170)
 * 6. 合并两个有序链表 (LeetCode 21, LintCode 165, 剑指Offer 25, 牛客网 NC33)
 * 7. 两两交换链表中的节点 (LeetCode 24, LintCode 451)
 * 8. 重排链表 (LeetCode 143, LintCode 99)
 * 9. 删除链表的倒数第N个节点 (LeetCode 19, LintCode 174, 牛客网 NC53, 剑指Offer 22)
 * 10. 奇偶链表 (LeetCode 328, LintCode 1292, 牛客网 NC142)
 * 11. 分隔链表 (LeetCode 86, LintCode 96, 牛客网 NC188)
 * 12. 链表求和 (LeetCode 2, LeetCode 445, LintCode 167, 牛客网 NC40)
 * 13. 环形链表 (LeetCode 141, LeetCode 142, LintCode 102, 牛客网 NC4, 剑指Offer 23)
 * 14. 相交链表 (LeetCode 160, LintCode 380, 牛客网 NC66, 剑指Offer 52)
 * 15. 排序链表 (LeetCode 148, LintCode 98)
 * 16. 链表随机节点 (LeetCode 382)
 * 17. 复制带随机指针的链表 (LeetCode 138, 剑指Offer 35)
 * 18. 链表组件 (LeetCode 817)
 * 19. 链表中的下一个更大节点 (LeetCode 1019)
 * 20. 链表最大孪生和 (LeetCode 2130)
 */

// 单链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

// 双链表节点定义
struct DoubleListNode {
    int val;
    DoubleListNode *prev;
    DoubleListNode *next;
    DoubleListNode() : val(0), prev(nullptr), next(nullptr) {}
    DoubleListNode(int x) : val(x), prev(nullptr), next(nullptr) {}
};

/**
 * 工具函数：创建链表
 */
ListNode* createList(const std::vector<int>& vals) {
    if (vals.empty()) return nullptr;
    
    ListNode* head = new ListNode(vals[0]);
    ListNode* current = head;
    for (size_t i = 1; i < vals.size(); i++) {
        current->next = new ListNode(vals[i]);
        current = current->next;
    }
    return head;
}

/**
 * 工具函数：打印链表
 */
void printList(ListNode* head) {
    ListNode* current = head;
    while (current != nullptr) {
        std::cout << current->val;
        if (current->next != nullptr) {
            std::cout << " -> ";
        }
        current = current->next;
    }
    std::cout << std::endl;
}

/**
 * 工具函数：释放链表内存
 */
void deleteList(ListNode* head) {
    while (head != nullptr) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

/**
 * 方法1: 迭代法反转链表
 * 时间复杂度: O(n) - 需要遍历链表一次
 * 空间复杂度: O(1) - 只使用了常数级别的额外空间
 * 
 * 解题思路:
 * 使用三个指针(pre, current, next)来逐个反转链表中的节点指向关系
 * 1. pre指向已反转部分的最后一个节点
 * 2. current指向当前待处理节点
 * 3. next保存current的下一个节点，防止断链
 * 
 * 执行过程:
 * 原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
 * 步骤1: null <- 1    2 -> 3 -> 4 -> 5 -> null
 * 步骤2: null <- 1 <- 2    3 -> 4 -> 5 -> null
 * 步骤3: null <- 1 <- 2 <- 3    4 -> 5 -> null
 * ...
 * 最终: null <- 1 <- 2 <- 3 <- 4 <- 5
 */
ListNode* reverseListIterative(ListNode* head) {
    ListNode* pre = nullptr;     // 已反转部分的头节点
    ListNode* current = head;    // 当前待处理节点
    ListNode* next = nullptr;    // 保存current的下一个节点
    
    while (current != nullptr) {
        next = current->next;    // 保存下一个节点
        current->next = pre;     // 反转当前节点的指向
        pre = current;           // 移动pre指针
        current = next;          // 移动current指针
    }
    
    return pre; // pre指向原链表的最后一个节点，即新链表的头节点
}

/**
 * 方法2: 递归法反转链表
 * 时间复杂度: O(n) - 递归调用n次
 * 空间复杂度: O(n) - 递归调用栈的深度为n
 * 
 * 解题思路:
 * 1. 递归到链表末尾
 * 2. 在回溯过程中逐个反转节点的指向
 * 3. 假设除了当前节点外，后续链表已经完成反转
 * 
 * 执行过程:
 * 原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
 * 递归到5，返回5
 * 回溯到4: 4.next.next = 4 (即5->4)，4.next = null
 * 回溯到3: 3.next.next = 3 (即4->3)，3.next = null
 * ...
 */
ListNode* reverseListRecursive(ListNode* head) {
    // 递归终止条件：空节点或只有一个节点
    if (head == nullptr || head->next == nullptr) {
        return head;
    }
    
    // 递归处理后续节点，获取反转后链表的头节点
    ListNode* newHead = reverseListRecursive(head->next);
    
    // 反转当前节点和下一个节点的连接关系
    head->next->next = head;  // 让下一个节点指向当前节点
    head->next = nullptr;     // 断开当前节点的next指针
    
    return newHead; // 返回反转后链表的头节点
}

/**
 * 反转链表指定区间
 * 时间复杂度: O(n) - 最多遍历一次链表
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 * 
 * 解题思路:
 * 1. 找到需要反转区间的前一个节点(pre)
 * 2. 找到需要反转区间的第一个节点(start)
 * 3. 使用头插法将区间内的节点逐个插入到pre节点之后
 * 4. 连接反转后的链表与其他部分
 * 
 * 执行过程:
 * 原链表: 1 -> 2 -> 3 -> 4 -> 5, left=2, right=4
 * 步骤1: 找到pre(节点1)和start(节点2)
 * 步骤2: 将节点3插入到pre之后: 1 -> 3 -> 2 -> 4 -> 5
 * 步骤3: 将节点4插入到pre之后: 1 -> 4 -> 3 -> 2 -> 5
 * 结果: 1 -> 4 -> 3 -> 2 -> 5
 */
ListNode* reverseBetween(ListNode* head, int left, int right) {
    // 创建虚拟头节点，简化边界处理
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    
    // 找到反转区间的前一个节点
    ListNode* pre = dummy;
    for (int i = 0; i < left - 1; i++) {
        pre = pre->next;
    }
    
    // start指向反转区间的第一个节点
    ListNode* start = pre->next;
    // then指向待处理节点
    ListNode* then = start->next;
    
    // 头插法实现区间反转
    for (int i = 0; i < right - left; i++) {
        start->next = then->next;
        then->next = pre->next;
        pre->next = then;
        then = start->next;
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * K个一组反转链表
 * 时间复杂度: O(n) - 每个节点最多被访问两次
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 * 
 * 解题思路:
 * 1. 分组处理，每次处理k个节点
 * 2. 对每组节点进行反转
 * 3. 连接各组之间的关系
 * 4. 处理不足k个的剩余节点（保持原顺序）
 * 
 * 执行过程:
 * 原链表: 1 -> 2 -> 3 -> 4 -> 5, k=3
 * 第一组(1,2,3)反转: 3 -> 2 -> 1
 * 第二组(4,5)不足k个，保持原顺序: 4 -> 5
 * 结果: 3 -> 2 -> 1 -> 4 -> 5
 */
ListNode* reverseKGroup(ListNode* head, int k) {
    // 计算链表长度
    int length = 0;
    ListNode* current = head;
    while (current != nullptr) {
        length++;
        current = current->next;
    }
    
    // 创建虚拟头节点
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    
    // pre指向已处理部分的最后一个节点
    ListNode* pre = dummy;
    
    // 分组处理
    while (length >= k) {
        // start指向当前组的第一个节点
        ListNode* start = pre->next;
        // then指向待处理节点
        ListNode* then = start->next;
        
        // 对当前组进行k-1次头插操作
        for (int i = 0; i < k - 1; i++) {
            start->next = then->next;
            then->next = pre->next;
            pre->next = then;
            then = start->next;
        }
        
        // 更新pre指针和剩余长度
        pre = start;
        length -= k;
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 反转双链表
 * 时间复杂度: O(n) - 需要遍历双链表一次
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 * 
 * 题目来源：
 * 1. LeetCode 445. 两数相加 II（涉及链表反转思想）
 * 2. 剑指Offer 24. 反转链表（扩展到双链表）
 * 3. 牛客网 反转双链表
 */
DoubleListNode* reverseDoubleList(DoubleListNode* head) {
    DoubleListNode* pre = nullptr;
    DoubleListNode* next = nullptr;
    while (head != nullptr) {
        next = head->next;      // 保存下一个节点
        head->next = pre;       // 反转next指针
        head->prev = next;      // 反转prev指针
        pre = head;             // 移动pre指针
        head = next;            // 移动head指针
    }
    return pre;  // 返回新的头节点
}

/**
 * 补充题目4: 回文链表
 * 题目来源：
 * 1. LeetCode 234. 回文链表 - https://leetcode.cn/problems/palindrome-linked-list/
 * 2. LintCode 223. 回文链表 - https://www.lintcode.com/problem/palindrome-linked-list/
 * 3. 牛客网 NC78 链表中倒数最后k个结点（相关题目）
 * 
 * 题目描述：判断一个链表是否是回文链表
 * 输入：1->2->2->1
 * 输出：true
 * 
 * 最优解法：使用快慢指针找到中点，反转后半部分，然后比较
 * 时间复杂度: O(n) - 只需要一次遍历找到中点，一次反转，一次比较
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 */
bool isPalindrome(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return true;  // 空链表或单节点链表是回文的
    }
    
    // 步骤1: 使用快慢指针找到链表的中点
    ListNode* slow = head;
    ListNode* fast = head;
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;        // 慢指针每次走一步
        fast = fast->next->next;  // 快指针每次走两步
    }
    // 循环结束后，slow指向中点位置（如果节点数为奇数）或后半部分的第一个节点（如果节点数为偶数）
    
    // 步骤2: 反转后半部分链表
    ListNode* secondHalfHead = reverseListIterative(slow);
    // 保存反转后的头节点，用于后续恢复
    ListNode* secondHalfStart = secondHalfHead;
    
    // 步骤3: 比较前半部分和反转后的后半部分
    ListNode* firstHalfHead = head;
    bool isPalindromeFlag = true;
    while (secondHalfHead != nullptr) {
        if (firstHalfHead->val != secondHalfHead->val) {
            isPalindromeFlag = false;
            break;
        }
        firstHalfHead = firstHalfHead->next;
        secondHalfHead = secondHalfHead->next;
    }
    
    // 步骤4: 恢复链表（可选，但这是良好的工程实践）
    reverseListIterative(secondHalfStart);
    
    return isPalindromeFlag;
}

/**
 * 补充题目5: 旋转链表
 * 题目来源：
 * 1. LeetCode 61. 旋转链表 - https://leetcode.cn/problems/rotate-list/
 * 2. LintCode 170. 旋转链表 - https://www.lintcode.com/problem/rotate-list/
 * 3. 牛客网 NC53 删除链表的倒数第n个节点（相关题目）
 * 
 * 题目描述：将链表向右旋转k个位置
 * 输入：1->2->3->4->5->NULL, k = 2
 * 输出：4->5->1->2->3->NULL
 * 
 * 解题思路：
 * 1. 先计算链表长度
 * 2. 将链表首尾相连形成环
 * 3. 在合适位置断开环
 * 时间复杂度: O(n) - 需要遍历链表
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 */
ListNode* rotateRight(ListNode* head, int k) {
    // 处理特殊情况
    if (head == nullptr || head->next == nullptr || k == 0) {
        return head;
    }
    
    // 步骤1: 计算链表长度并找到尾节点
    int length = 1;
    ListNode* tail = head;
    while (tail->next != nullptr) {
        tail = tail->next;
        length++;
    }
    
    // 步骤2: 计算实际需要旋转的次数（取模操作避免多余旋转）
    k = k % length;
    if (k == 0) {
        return head;  // 不需要旋转
    }
    
    // 步骤3: 将链表首尾相连形成环
    tail->next = head;
    
    // 步骤4: 找到新的尾节点位置，距离原头节点 (length - k) 个位置
    ListNode* newTail = head;
    for (int i = 0; i < length - k - 1; i++) {
        newTail = newTail->next;
    }
    
    // 步骤5: 新的头节点是新尾节点的下一个节点
    ListNode* newHead = newTail->next;
    
    // 步骤6: 断开环
    newTail->next = nullptr;
    
    return newHead;
}

/**
 * 补充题目6: 合并两个有序链表
 * 题目来源：
 * 1. LeetCode 21. 合并两个有序链表 - https://leetcode.cn/problems/merge-two-sorted-lists/
 * 2. LintCode 165. 合并两个排序链表 - https://www.lintcode.com/problem/merge-two-sorted-lists/
 * 3. 剑指Offer 25. 合并两个排序的链表
 * 4. 牛客网 NC33 合并两个排序的链表
 * 
 * 题目描述：将两个升序链表合并为一个新的升序链表
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 * 
 * 解题思路：使用迭代或递归方法，逐个比较两个链表的节点值
 * 时间复杂度: O(n+m) - n和m分别是两个链表的长度
 * 空间复杂度: O(1) - 迭代版本，只使用常数级别的额外空间
 */
ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
    // 创建虚拟头节点，简化边界情况处理
    ListNode* dummy = new ListNode(0);
    ListNode* current = dummy;
    
    // 迭代比较两个链表的节点值
    while (l1 != nullptr && l2 != nullptr) {
        if (l1->val <= l2->val) {
            current->next = l1;
            l1 = l1->next;
        } else {
            current->next = l2;
            l2 = l2->next;
        }
        current = current->next;
    }
    
    // 连接剩余部分
    current->next = (l1 != nullptr) ? l1 : l2;
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 补充题目7: 两两交换链表中的节点
 * 题目来源：
 * 1. LeetCode 24. 两两交换链表中的节点 - https://leetcode.cn/problems/swap-nodes-in-pairs/
 * 2. LintCode 451. 两两交换链表中的节点 - https://www.lintcode.com/problem/swap-nodes-in-pairs/
 * 3. 牛客网 NC142 链表的奇偶重排（相关题目）
 * 
 * 题目描述：两两交换链表中的相邻节点
 * 输入：1->2->3->4
 * 输出：2->1->4->3
 * 
 * 解题思路：使用虚拟头节点和迭代方法
 * 时间复杂度: O(n) - 需要遍历链表一次
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 */
ListNode* swapPairs(ListNode* head) {
    // 创建虚拟头节点
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    ListNode* prev = dummy;
    
    // 当有至少两个节点可以交换时
    while (prev->next != nullptr && prev->next->next != nullptr) {
        // 获取需要交换的两个节点
        ListNode* first = prev->next;
        ListNode* second = prev->next->next;
        
        // 执行交换操作
        first->next = second->next;  // 1 -> 3
        second->next = first;        // 2 -> 1
        prev->next = second;         // dummy -> 2
        
        // 移动prev指针到下一对的前一个位置
        prev = first;
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 补充题目8: 重排链表
 * 题目来源：
 * 1. LeetCode 143. 重排链表 - https://leetcode.cn/problems/reorder-list/
 * 2. LintCode 99. 重排链表 - https://www.lintcode.com/problem/reorder-list/
 * 3. 牛客网 NC40 链表相加（二）（相关题目）
 * 
 * 题目描述：按照 L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → ... 重新排列链表
 * 输入：1->2->3->4
 * 输出：1->4->2->3
 * 
 * 解题思路：
 * 1. 使用快慢指针找到中点
 * 2. 反转后半部分链表
 * 3. 合并两个链表
 * 时间复杂度: O(n) - 需要遍历链表三次
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 */
void reorderList(ListNode* head) {
    if (head == nullptr || head->next == nullptr || head->next->next == nullptr) {
        return;  // 无需重排
    }
    
    // 步骤1: 使用快慢指针找到链表中点
    ListNode* slow = head;
    ListNode* fast = head;
    while (fast->next != nullptr && fast->next->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
    }
    
    // 步骤2: 反转后半部分链表
    ListNode* secondHalf = reverseListIterative(slow->next);
    slow->next = nullptr;  // 断开前半部分和后半部分
    
    // 步骤3: 合并两个链表
    ListNode* firstHalf = head;
    while (secondHalf != nullptr) {
        ListNode* temp1 = firstHalf->next;
        ListNode* temp2 = secondHalf->next;
        
        firstHalf->next = secondHalf;
        secondHalf->next = temp1;
        
        firstHalf = temp1;
        secondHalf = temp2;
    }
}

/**
 * 补充题目9: 删除链表的倒数第N个节点
 * 题目来源：
 * 1. LeetCode 19. 删除链表的倒数第N个节点 - https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
 * 2. LintCode 174. 删除链表中倒数第n个节点 - https://www.lintcode.com/problem/remove-nth-node-from-end-of-list/
 * 3. 牛客网 NC53 删除链表的倒数第n个节点
 * 4. 剑指Offer 22. 链表中倒数第k个节点（相关题目）
 * 
 * 题目描述：删除链表的倒数第n个节点，返回链表的头节点
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 * 
 * 解题思路：使用快慢指针，快指针先走n步，然后快慢指针一起走
 * 时间复杂度: O(n) - 只需要遍历链表一次
 * 空间复杂度: O(1) - 只使用常数级别的额外空间
 */
ListNode* removeNthFromEnd(ListNode* head, int n) {
    // 创建虚拟头节点，简化边界情况处理
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    
    // 设置快慢指针
    ListNode* fast = dummy;
    ListNode* slow = dummy;
    
    // 快指针先走n+1步
    for (int i = 0; i <= n; i++) {
        fast = fast->next;
        // 如果n大于链表长度，fast会变成null
        if (i < n && fast == nullptr) {
            delete dummy;
            return head;  // n大于链表长度，无法删除
        }
    }
    
    // 快慢指针一起走，直到快指针到达链表末尾
    while (fast != nullptr) {
        fast = fast->next;
        slow = slow->next;
    }
    
    // 此时slow指向待删除节点的前一个节点
    ListNode* toDelete = slow->next;
    slow->next = slow->next->next;
    delete toDelete;  // 释放内存
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 测试基础链表反转
 */
void testReverseList() {
    std::cout << "=== 测试基础链表反转 ===" << std::endl;
    
    // 测试用例1: [1,2,3,4,5] -> [5,4,3,2,1]
    ListNode* head1 = createList({1, 2, 3, 4, 5});
    std::cout << "原链表: ";
    printList(head1);
    ListNode* reversed1 = reverseListIterative(head1);
    std::cout << "反转后: ";
    printList(reversed1);
    deleteList(reversed1);
    
    // 测试用例2: [1,2] -> [2,1]
    ListNode* head2 = createList({1, 2});
    std::cout << "原链表: ";
    printList(head2);
    ListNode* reversed2 = reverseListRecursive(head2);
    std::cout << "反转后: ";
    printList(reversed2);
    deleteList(reversed2);
    
    // 测试用例3: [] -> []
    ListNode* head3 = nullptr;
    std::cout << "原链表: ";
    printList(head3);
    ListNode* reversed3 = reverseListIterative(head3);
    std::cout << "反转后: ";
    printList(reversed3);
    std::cout << std::endl;
}

/**
 * 测试指定区间链表反转
 */
void testReverseListII() {
    std::cout << "=== 测试指定区间链表反转 ===" << std::endl;
    
    // 测试用例1: [1,2,3,4,5], left=2, right=4 -> [1,4,3,2,5]
    ListNode* head1 = createList({1, 2, 3, 4, 5});
    std::cout << "原链表: ";
    printList(head1);
    ListNode* reversed1 = reverseBetween(head1, 2, 4);
    std::cout << "反转位置2到4后: ";
    printList(reversed1);
    deleteList(reversed1);
    
    // 测试用例2: [5], left=1, right=1 -> [5]
    ListNode* head2 = createList({5});
    std::cout << "原链表: ";
    printList(head2);
    ListNode* reversed2 = reverseBetween(head2, 1, 1);
    std::cout << "反转位置1到1后: ";
    printList(reversed2);
    deleteList(reversed2);
    std::cout << std::endl;
}

/**
 * 测试K个一组反转链表
 */
void testReverseKGroup() {
    std::cout << "=== 测试K个一组反转链表 ===" << std::endl;
    
    // 测试用例1: [1,2,3,4,5], k=2 -> [2,1,4,3,5]
    ListNode* head1 = createList({1, 2, 3, 4, 5});
    std::cout << "原链表: ";
    printList(head1);
    ListNode* reversed1 = reverseKGroup(head1, 2);
    std::cout << "每2个一组反转后: ";
    printList(reversed1);
    deleteList(reversed1);
    
    // 测试用例2: [1,2,3,4,5], k=3 -> [3,2,1,4,5]
    ListNode* head2 = createList({1, 2, 3, 4, 5});
    std::cout << "原链表: ";
    printList(head2);
    ListNode* reversed2 = reverseKGroup(head2, 3);
    std::cout << "每3个一组反转后: ";
    printList(reversed2);
    deleteList(reversed2);
    std::cout << std::endl;
}

/**
 * 运行单元测试
 */
void runUnitTests() {
    std::cout << "=== 链表反转单元测试 ===" << std::endl;
    
    // 测试用例1: 正常情况
    ListNode* test1 = createList({1, 2, 3, 4, 5});
    ListNode* result1 = reverseListIterative(test1);
    std::cout << "测试1 - 输入[1,2,3,4,5]，期望[5,4,3,2,1]，实际: ";
    printList(result1);
    deleteList(result1);
    
    // 测试用例2: 空链表
    ListNode* result2 = reverseListIterative(nullptr);
    std::cout << "测试2 - 输入[]，期望[]，实际: ";
    printList(result2);
    
    // 测试用例3: 单节点链表
    ListNode* test3 = new ListNode(1);
    ListNode* result3 = reverseListIterative(test3);
    std::cout << "测试3 - 输入[1]，期望[1]，实际: " << result3->val << std::endl;
    deleteList(result3);
    
    // 测试用例4: 两节点链表
    ListNode* test4 = createList({1, 2});
    ListNode* result4 = reverseListIterative(test4);
    std::cout << "测试4 - 输入[1,2]，期望[2,1]，实际: ";
    printList(result4);
    deleteList(result4);
    
    std::cout << "单元测试完成" << std::endl << std::endl;
}

/**
 * 补充题目10: 奇偶链表
 * 题目来源：
 * 1. LeetCode 328. 奇偶链表 - https://leetcode.cn/problems/odd-even-linked-list/
 * 2. LintCode 1292. 奇偶链表 - https://www.lintcode.com/problem/odd-even-linked-list/
 * 3. 牛客网 NC142 链表的奇偶重排
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* oddEvenList(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return head;
    }
    
    ListNode* odd = head;
    ListNode* even = head->next;
    ListNode* evenHead = even;
    
    while (even != nullptr && even->next != nullptr) {
        odd->next = even->next;
        odd = odd->next;
        even->next = odd->next;
        even = even->next;
    }
    
    odd->next = evenHead;
    return head;
}

/**
 * 补充题目11: 分隔链表
 * 题目来源：
 * 1. LeetCode 86. 分隔链表 - https://leetcode.cn/problems/partition-list/
 * 2. LintCode 96. 分隔链表 - https://www.lintcode.com/problem/partition-list/
 * 3. 牛客网 NC188 分隔链表
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* partition(ListNode* head, int x) {
    ListNode* beforeHead = new ListNode(0);
    ListNode* before = beforeHead;
    ListNode* afterHead = new ListNode(0);
    ListNode* after = afterHead;
    
    while (head != nullptr) {
        if (head->val < x) {
            before->next = head;
            before = before->next;
        } else {
            after->next = head;
            after = after->next;
        }
        head = head->next;
    }
    
    after->next = nullptr;
    before->next = afterHead->next;
    
    ListNode* result = beforeHead->next;
    delete beforeHead;
    delete afterHead;
    return result;
}

/**
 * 补充题目12: 链表求和
 * 题目来源：
 * 1. LeetCode 2. 两数相加 - https://leetcode.cn/problems/add-two-numbers/
 * 2. LeetCode 445. 两数相加 II - https://leetcode.cn/problems/add-two-numbers-ii/
 * 
 * 时间复杂度: O(max(m,n))
 * 空间复杂度: O(max(m,n))
 * 是否为最优解：是
 */
ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
    ListNode* dummy = new ListNode(0);
    ListNode* current = dummy;
    int carry = 0;
    
    while (l1 != nullptr || l2 != nullptr || carry != 0) {
        int val1 = (l1 != nullptr) ? l1->val : 0;
        int val2 = (l2 != nullptr) ? l2->val : 0;
        
        int sum = val1 + val2 + carry;
        carry = sum / 10;
        
        current->next = new ListNode(sum % 10);
        current = current->next;
        
        if (l1 != nullptr) l1 = l1->next;
        if (l2 != nullptr) l2 = l2->next;
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 补充题目13: 环形链表
 * 题目来源：
 * 1. LeetCode 141. 环形链表 - https://leetcode.cn/problems/linked-list-cycle/
 * 2. LeetCode 142. 环形链表 II - https://leetcode.cn/problems/linked-list-cycle-ii/
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
bool hasCycle(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return false;
    }
    
    ListNode* slow = head;
    ListNode* fast = head;
    
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
        
        if (slow == fast) {
            return true;
        }
    }
    
    return false;
}

ListNode* detectCycle(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return nullptr;
    }
    
    ListNode* slow = head;
    ListNode* fast = head;
    bool hasCycleFlag = false;
    
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
        
        if (slow == fast) {
            hasCycleFlag = true;
            break;
        }
    }
    
    if (!hasCycleFlag) {
        return nullptr;
    }
    
    slow = head;
    while (slow != fast) {
        slow = slow->next;
        fast = fast->next;
    }
    
    return slow;
}

/**
 * 补充题目14: 相交链表
 * 题目来源：
 * 1. LeetCode 160. 相交链表 - https://leetcode.cn/problems/intersection-of-two-linked-lists/
 * 2. LintCode 380. 相交链表 - https://www.lintcode.com/problem/intersection-of-two-linked-lists/
 * 
 * 时间复杂度: O(m+n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* getIntersectionNode(ListNode* headA, ListNode* headB) {
    if (headA == nullptr || headB == nullptr) {
        return nullptr;
    }
    
    ListNode* pA = headA;
    ListNode* pB = headB;
    
    while (pA != pB) {
        pA = (pA == nullptr) ? headB : pA->next;
        pB = (pB == nullptr) ? headA : pB->next;
    }
    
    return pA;
}

/**
 * 补充题目15: 排序链表
 * 题目来源：
 * 1. LeetCode 148. 排序链表 - https://leetcode.cn/problems/sort-list/
 * 2. LintCode 98. 排序链表 - https://www.lintcode.com/problem/sort-list/
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(log n)
 * 是否为最优解：是
 */
ListNode* sortList(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return head;
    }
    
    ListNode* slow = head;
    ListNode* fast = head->next;
    
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
    }
    
    ListNode* mid = slow->next;
    slow->next = nullptr;
    
    ListNode* left = sortList(head);
    ListNode* right = sortList(mid);
    
    return mergeTwoLists(left, right);
}

/**
 * 补充题目16: 链表随机节点
 * 题目来源：
 * 1. LeetCode 382. 链表随机节点 - https://leetcode.cn/problems/linked-list-random-node/
 * 2. 蓄水池抽样算法应用
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
class RandomNodeSelector {
private:
    ListNode* head;
    
public:
    RandomNodeSelector(ListNode* head) : head(head) {}
    
    int getRandom() {
        ListNode* current = head;
        int result = 0;
        int count = 0;
        
        while (current != nullptr) {
            count++;
            // 以1/count的概率选择当前节点
            if (rand() % count == 0) {
                result = current->val;
            }
            current = current->next;
        }
        
        return result;
    }
};

/**
 * 补充题目17: 复制带随机指针的链表
 * 题目来源：
 * 1. LeetCode 138. 复制带随机指针的链表 - https://leetcode.cn/problems/copy-list-with-random-pointer/
 * 2. 剑指Offer 35. 复杂链表的复制
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
struct NodeWithRandom {
    int val;
    NodeWithRandom* next;
    NodeWithRandom* random;
    NodeWithRandom(int x) : val(x), next(nullptr), random(nullptr) {}
};

NodeWithRandom* copyRandomList(NodeWithRandom* head) {
    if (head == nullptr) {
        return nullptr;
    }
    
    // 第一次遍历：在每个节点后面插入复制节点
    NodeWithRandom* current = head;
    while (current != nullptr) {
        NodeWithRandom* copy = new NodeWithRandom(current->val);
        copy->next = current->next;
        current->next = copy;
        current = copy->next;
    }
    
    // 第二次遍历：设置复制节点的随机指针
    current = head;
    while (current != nullptr) {
        if (current->random != nullptr) {
            current->next->random = current->random->next;
        }
        current = current->next->next;
    }
    
    // 第三次遍历：分离原链表和复制链表
    current = head;
    NodeWithRandom* copyHead = head->next;
    NodeWithRandom* copyCurrent = copyHead;
    
    while (current != nullptr) {
        current->next = current->next->next;
        if (copyCurrent->next != nullptr) {
            copyCurrent->next = copyCurrent->next->next;
        }
        current = current->next;
        copyCurrent = copyCurrent->next;
    }
    
    return copyHead;
}

/**
 * 补充题目18: 链表组件
 * 题目来源：
 * 1. LeetCode 817. 链表组件 - https://leetcode.cn/problems/linked-list-components/
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 是否为最优解：是
 */
#include <unordered_set>
int numComponents(ListNode* head, std::vector<int>& nums) {
    std::unordered_set<int> numSet(nums.begin(), nums.end());
    
    int components = 0;
    bool inComponent = false;
    ListNode* current = head;
    
    while (current != nullptr) {
        if (numSet.find(current->val) != numSet.end()) {
            if (!inComponent) {
                components++;
                inComponent = true;
            }
        } else {
            inComponent = false;
        }
        current = current->next;
    }
    
    return components;
}

/**
 * 补充题目19: 链表中的下一个更大节点
 * 题目来源：
 * 1. LeetCode 1019. 链表中的下一个更大节点 - https://leetcode.cn/problems/next-greater-node-in-linked-list/
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 是否为最优解：是
 */
#include <stack>
std::vector<int> nextLargerNodes(ListNode* head) {
    // 将链表转换为数组
    std::vector<int> list;
    ListNode* current = head;
    while (current != nullptr) {
        list.push_back(current->val);
        current = current->next;
    }
    
    int n = list.size();
    std::vector<int> result(n, 0);
    std::stack<int> stack;
    
    // 从右向左遍历，使用单调栈
    for (int i = n - 1; i >= 0; i--) {
        int currentVal = list[i];
        
        // 弹出栈顶比当前值小的元素
        while (!stack.empty() && stack.top() <= currentVal) {
            stack.pop();
        }
        
        // 如果栈不为空，栈顶就是下一个更大节点
        result[i] = stack.empty() ? 0 : stack.top();
        
        // 将当前值压入栈
        stack.push(currentVal);
    }
    
    return result;
}

/**
 * 补充题目20: 链表最大孪生和
 * 题目来源：
 * 1. LeetCode 2130. 链表最大孪生和 - https://leetcode.cn/problems/maximum-twin-sum-of-a-linked-list/
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
int pairSum(ListNode* head) {
    // 使用快慢指针找到中点
    ListNode* slow = head;
    ListNode* fast = head;
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
    }
    
    // 反转后半部分链表
    ListNode* secondHalf = reverseListIterative(slow);
    
    // 计算孪生和的最大值
    int maxSum = 0;
    ListNode* firstHalf = head;
    while (secondHalf != nullptr) {
        maxSum = std::max(maxSum, firstHalf->val + secondHalf->val);
        firstHalf = firstHalf->next;
        secondHalf = secondHalf->next;
    }
    
    return maxSum;
}

/**
 * 补充题目21: 合并K个升序链表
 * 题目来源：
 * 1. LeetCode 23. 合并K个升序链表 - https://leetcode.cn/problems/merge-k-sorted-lists/
 * 2. LintCode 104. 合并k个排序链表 - https://www.lintcode.com/problem/merge-k-sorted-lists/
 * 3. 牛客网 NC127. 合并k个已排序的链表
 * 
 * 时间复杂度: O(N log K)
 * 空间复杂度: O(K)
 * 是否为最优解：是
 */
#include <queue>
ListNode* mergeKLists(std::vector<ListNode*>& lists) {
    // 使用最小堆
    auto cmp = [](const ListNode* a, const ListNode* b) { return a->val > b->val; };
    std::priority_queue<ListNode*, std::vector<ListNode*>, decltype(cmp)> minHeap(cmp);
    
    // 将所有非空链表的头节点加入最小堆
    for (ListNode* list : lists) {
        if (list != nullptr) {
            minHeap.push(list);
        }
    }
    
    // 创建虚拟头节点
    ListNode* dummy = new ListNode(0);
    ListNode* current = dummy;
    
    // 从堆中取出最小节点，加入结果链表
    while (!minHeap.empty()) {
        ListNode* minNode = minHeap.top();  // 取出最小节点
        minHeap.pop();
        current->next = minNode;            // 加入结果链表
        current = current->next;            // 移动指针
        
        // 将取出节点的下一个节点加入堆中(如果不为空)
        if (minNode->next != nullptr) {
            minHeap.push(minNode->next);
        }
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 补充题目22: 删除链表中的节点
 * 题目来源：
 * 1. LeetCode 237. 删除链表中的节点 - https://leetcode.cn/problems/delete-node-in-a-linked-list/
 * 2. LintCode 37. 删除链表中的节点 - https://www.lintcode.com/problem/delete-node-in-a-linked-list/
 * 3. 牛客网 NC138. 删除链表的节点
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
void deleteNode(ListNode* node) {
    // 将下一个节点的值复制到当前节点
    node->val = node->next->val;
    
    // 保存要删除的节点
    ListNode* nodeToDelete = node->next;
    
    // 跳过下一个节点
    node->next = node->next->next;
    
    // 释放内存
    delete nodeToDelete;
}

/**
 * 补充题目23: 删除排序链表中的重复元素
 * 题目来源：
 * 1. LeetCode 83. 删除排序链表中的重复元素 - https://leetcode.cn/problems/remove-duplicates-from-sorted-list/
 * 2. LintCode 112. 删除排序链表中的重复元素 - https://www.lintcode.com/problem/remove-duplicates-from-sorted-list/
 * 3. 牛客网 NC141. 判断一个链表是否为回文结构
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* deleteDuplicates(ListNode* head) {
    // 边界情况处理
    if (head == nullptr || head->next == nullptr) {
        return head;
    }
    
    ListNode* current = head;
    
    // 遍历链表
    while (current->next != nullptr) {
        // 如果当前节点值等于下一个节点值，跳过下一个节点
        if (current->val == current->next->val) {
            current->next = current->next->next;
        } else {
            // 只有当下一个节点不被删除时，才移动current指针
            current = current->next;
        }
    }
    
    return head;
}

/**
 * 补充题目24: 删除排序链表中的重复元素 II
 * 题目来源：
 * 1. LeetCode 82. 删除排序链表中的重复元素 II - https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/
 * 2. LintCode 113. 删除排序链表中的重复元素 II - https://www.lintcode.com/problem/remove-duplicates-from-sorted-list-ii/
 * 3. 牛客网 NC140
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* deleteDuplicatesII(ListNode* head) {
    // 创建虚拟头节点，简化边界处理
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    
    // prev指向已处理部分的最后一个节点
    ListNode* prev = dummy;
    // current指向当前待处理节点
    ListNode* current = head;
    
    while (current != nullptr) {
        // 检查是否有重复节点
        if (current->next != nullptr && current->val == current->next->val) {
            // 记录重复值
            int duplicateValue = current->val;
            
            // 跳过所有重复节点
            while (current != nullptr && current->val == duplicateValue) {
                ListNode* nodeToDelete = current;
                current = current->next;
                delete nodeToDelete;
            }
            
            // 连接prev和current
            prev->next = current;
        } else {
            // 没有重复，正常移动指针
            prev = current;
            current = current->next;
        }
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 补充题目25: 移除链表元素
 * 题目来源：
 * 1. LeetCode 203. 移除链表元素 - https://leetcode.cn/problems/remove-linked-list-elements/
 * 2. 牛客网相关题目
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否为最优解：是
 */
ListNode* removeElements(ListNode* head, int val) {
    // 创建虚拟头节点，简化删除头节点的情况
    ListNode* dummy = new ListNode(0);
    dummy->next = head;
    
    // prev指向已处理部分的最后一个节点
    ListNode* prev = dummy;
    // current指向当前待处理节点
    ListNode* current = head;
    
    while (current != nullptr) {
        if (current->val == val) {
            // 删除当前节点
            ListNode* nodeToDelete = current;
            prev->next = current->next;
            current = current->next;
            delete nodeToDelete;
        } else {
            // 移动prev指针
            prev = current;
            current = current->next;
        }
    }
    
    ListNode* result = dummy->next;
    delete dummy;
    return result;
}

/**
 * 性能分析工具类
 */
class LinkedListProfiler {
private:
    std::chrono::high_resolution_clock::time_point startTime;
    std::chrono::high_resolution_clock::time_point endTime;
    
public:
    void start() {
        startTime = std::chrono::high_resolution_clock::now();
    }
    
    void end() {
        endTime = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(endTime - startTime);
        std::cout << "执行时间: " << duration.count() << " 纳秒" << std::endl;
    }
};

/**
 * 调试工具类
 */
class LinkedListDebugger {
public:
    static void printListState(ListNode* head, const std::string& message) {
        std::cout << message << ": ";
        printList(head);
    }
    
    static void assertList(bool condition, const std::string& message) {
        if (!condition) {
            throw std::runtime_error("链表断言失败: " + message);
        }
    }
    
    static bool verifyNoCycle(ListNode* head) {
        if (head == nullptr) return true;
        
        ListNode* slow = head;
        ListNode* fast = head;
        
        while (fast != nullptr && fast->next != nullptr) {
            slow = slow->next;
            fast = fast->next->next;
            if (slow == fast) {
                return false; // 有环
            }
        }
        return true; // 无环
    }
};

/**
 * 测试补充题目
 */
void testAdditionalProblems() {
    std::cout << "=== 补充题目测试 ===" << std::endl;
    
    // 测试链表随机节点
    ListNode* randomTest = createList({1, 2, 3, 4, 5});
    RandomNodeSelector selector(randomTest);
    std::cout << "随机节点选择测试: " << selector.getRandom() << std::endl;
    deleteList(randomTest);
    
    // 测试链表组件
    ListNode* componentTest = createList({0, 1, 2, 3});
    std::vector<int> nums = {0, 1, 3};
    std::cout << "链表组件个数: " << numComponents(componentTest, nums) << std::endl;
    deleteList(componentTest);
    
    // 测试下一个更大节点
    ListNode* largerTest = createList({2, 1, 5});
    std::vector<int> largerResult = nextLargerNodes(largerTest);
    std::cout << "下一个更大节点: ";
    for (int val : largerResult) {
        std::cout << val << " ";
    }
    std::cout << std::endl;
    deleteList(largerTest);
    
    // 测试链表最大孪生和
    ListNode* twinTest = createList({5, 4, 2, 1});
    std::cout << "链表最大孪生和: " << pairSum(twinTest) << std::endl;
    deleteList(twinTest);
    
    std::cout << "补充题目测试完成" << std::endl << std::endl;
}

/**
 * 测试合并K个升序链表
 */
void testMergeKLists() {
    std::cout << "=== 测试合并K个升序链表 ===" << std::endl;
    
    // 创建测试用例: lists = [[1,4,5],[1,3,4],[2,6]]
    ListNode* list1 = createList({1, 4, 5});
    ListNode* list2 = createList({1, 3, 4});
    ListNode* list3 = createList({2, 6});
    std::vector<ListNode*> lists = {list1, list2, list3};
    
    std::cout << "输入链表数组: ";
    for (size_t i = 0; i < lists.size(); i++) {
        if (i > 0) std::cout << ", ";
        printList(lists[i]);
    }
    
    ListNode* result = mergeKLists(lists);
    std::cout << "合并后: ";
    printList(result);
    deleteList(result);
    std::cout << std::endl;
}

/**
 * 测试删除链表中的节点
 */
void testDeleteNode() {
    std::cout << "=== 测试删除链表中的节点 ===" << std::endl;
    
    // 创建测试用例: [4,5,1,9], 删除节点5
    ListNode* head = createList({4, 5, 1, 9});
    std::cout << "原链表: ";
    printList(head);
    
    // 找到要删除的节点(值为5的节点)
    ListNode* nodeToDelete = head->next;  // 值为5的节点
    
    std::cout << "删除节点: " << nodeToDelete->val << std::endl;
    deleteNode(nodeToDelete);
    std::cout << "删除后: ";
    printList(head);
    deleteList(head);
    std::cout << std::endl;
}

/**
 * 测试删除排序链表中的重复元素
 */
void testDeleteDuplicates() {
    std::cout << "=== 测试删除排序链表中的重复元素 ===" << std::endl;
    
    // 测试用例1: [1,1,2] -> [1,2]
    ListNode* head1 = createList({1, 1, 2});
    std::cout << "原链表: ";
    printList(head1);
    ListNode* result1 = deleteDuplicates(head1);
    std::cout << "去重后: ";
    printList(result1);
    deleteList(result1);
    
    // 测试用例2: [1,1,2,3,3] -> [1,2,3]
    ListNode* head2 = createList({1, 1, 2, 3, 3});
    std::cout << "原链表: ";
    printList(head2);
    ListNode* result2 = deleteDuplicates(head2);
    std::cout << "去重后: ";
    printList(result2);
    deleteList(result2);
    std::cout << std::endl;
}

/**
 * 测试删除排序链表中的重复元素 II
 */
void testDeleteDuplicatesII() {
    std::cout << "=== 测试删除排序链表中的重复元素 II ===" << std::endl;
    
    // 测试用例1: [1,2,3,3,4,4,5] -> [1,2,5]
    ListNode* head1 = createList({1, 2, 3, 3, 4, 4, 5});
    std::cout << "原链表: ";
    printList(head1);
    ListNode* result1 = deleteDuplicatesII(head1);
    std::cout << "删除重复元素后: ";
    printList(result1);
    deleteList(result1);
    
    // 测试用例2: [1,1,1,2,3] -> [2,3]
    ListNode* head2 = createList({1, 1, 1, 2, 3});
    std::cout << "原链表: ";
    printList(head2);
    ListNode* result2 = deleteDuplicatesII(head2);
    std::cout << "删除重复元素后: ";
    printList(result2);
    deleteList(result2);
    std::cout << std::endl;
}

/**
 * 测试移除链表元素
 */
void testRemoveElements() {
    std::cout << "=== 测试移除链表元素 ===" << std::endl;
    
    // 测试用例: [1,2,6,3,4,5,6], val = 6 -> [1,2,3,4,5]
    ListNode* head = createList({1, 2, 6, 3, 4, 5, 6});
    int val = 6;
    std::cout << "原链表: ";
    printList(head);
    std::cout << "移除元素: " << val << std::endl;
    ListNode* result = removeElements(head, val);
    std::cout << "移除后: ";
    printList(result);
    deleteList(result);
    std::cout << std::endl;
}

// 主函数
int main() {
    // 设置随机种子
    srand(time(nullptr));
    
    // 运行测试
    testReverseList();
    testReverseListII();
    testReverseKGroup();
    runUnitTests();
    testAdditionalProblems();
    
    // 运行补充题目的测试
    testMergeKLists();
    testDeleteNode();
    testDeleteDuplicates();
    testDeleteDuplicatesII();
    testRemoveElements();
    
    // 性能分析示例
    LinkedListProfiler profiler;
    ListNode* perfTest = createList({1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    
    profiler.start();
    ListNode* reversed = reverseListIterative(perfTest);
    profiler.end();
    
    deleteList(reversed);
    
    std::cout << "\n=== 所有测试完成 ===" << std::endl;
    
    return 0;
}