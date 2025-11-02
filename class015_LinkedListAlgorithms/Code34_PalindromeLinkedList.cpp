// 回文链表 - LeetCode 234
// 测试链接: https://leetcode.cn/problems/palindrome-linked-list/
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
    // 方法1: 使用数组存储节点值，然后判断数组是否为回文
    bool isPalindrome(ListNode* head) {
        if (!head || !head->next) {
            return true; // 空链表或单节点链表是回文
        }
        
        // 存储链表节点值到数组
        vector<int> values;
        ListNode* curr = head;
        while (curr) {
            values.push_back(curr->val);
            curr = curr->next;
        }
        
        // 使用双指针判断数组是否为回文
        int left = 0;
        int right = values.size() - 1;
        while (left < right) {
            if (values[left] != values[right]) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    // 方法2: 快慢指针找到中点，反转后半部分，然后比较
    bool isPalindromeOptimal(ListNode* head) {
        if (!head || !head->next) {
            return true;
        }
        
        // 步骤1: 使用快慢指针找到链表中点
        ListNode* slow = head;
        ListNode* fast = head;
        while (fast->next && fast->next->next) {
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 步骤2: 反转后半部分链表
        ListNode* secondHalfHead = reverseList(slow->next);
        
        // 步骤3: 比较前半部分和反转后的后半部分
        ListNode* p1 = head;
        ListNode* p2 = secondHalfHead;
        bool isPalin = true;
        
        while (p2) { // 后半部分的长度 <= 前半部分
            if (p1->val != p2->val) {
                isPalin = false;
                break;
            }
            p1 = p1->next;
            p2 = p2->next;
        }
        
        // 步骤4: 恢复链表原状态（可选，但在工程实践中是好的做法）
        slow->next = reverseList(secondHalfHead);
        
        return isPalin;
    }
    
    // 方法3: 递归解法（利用函数调用栈模拟栈结构）
    bool isPalindromeRecursive(ListNode* head) {
        frontPointer = head;
        return recursivelyCheck(head);
    }
    
    // 方法4: 栈方法（显式使用栈）
    bool isPalindromeStack(ListNode* head) {
        if (!head || !head->next) {
            return true;
        }
        
        // 使用栈存储前半部分节点值
        vector<int> stack;
        ListNode* slow = head;
        ListNode* fast = head;
        
        // 找到中点的同时，将前半部分压入栈中
        while (fast && fast->next) {
            stack.push_back(slow->val);
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 如果链表长度为奇数，跳过中间节点
        if (fast) {
            slow = slow->next;
        }
        
        // 比较后半部分与栈中的元素
        while (slow) {
            int top = stack.back();
            stack.pop_back();
            if (slow->val != top) {
                return false;
            }
            slow = slow->next;
        }
        
        return true;
    }
    
private:
    // 用于递归解法的前向指针
    ListNode* frontPointer;
    
    // 辅助函数：反转链表
    ListNode* reverseList(ListNode* head) {
        ListNode* prev = nullptr;
        ListNode* curr = head;
        while (curr) {
            ListNode* nextTemp = curr->next;
            curr->next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }
    
    // 递归检查函数
    bool recursivelyCheck(ListNode* currentNode) {
        if (currentNode) {
            // 递归到链表末尾
            if (!recursivelyCheck(currentNode->next)) {
                return false;
            }
            // 比较当前节点与前向指针指向的节点
            if (currentNode->val != frontPointer->val) {
                return false;
            }
            // 前向指针前进
            frontPointer = frontPointer->next;
        }
        return true;
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
    
    // 测试用例1: [1,2,2,1]
    vector<int> nums1 = {1, 2, 2, 1};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n链表: ";
    printList(head1);
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head1) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head1) ? "是回文" : "不是回文") << endl;
    cout << "方法3（递归）结果: " << (solution.isPalindromeRecursive(head1) ? "是回文" : "不是回文") << endl;
    cout << "方法4（栈）结果: " << (solution.isPalindromeStack(head1) ? "是回文" : "不是回文") << endl;
    
    // 测试用例2: [1,2]
    vector<int> nums2 = {1, 2};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n链表: ";
    printList(head2);
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head2) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head2) ? "是回文" : "不是回文") << endl;
    
    // 测试用例3: [1,2,3,2,1]
    vector<int> nums3 = {1, 2, 3, 2, 1};
    ListNode* head3 = buildList(nums3);
    cout << "\n测试用例3:\n链表: ";
    printList(head3);
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head3) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head3) ? "是回文" : "不是回文") << endl;
    
    // 测试用例4: [1]
    vector<int> nums4 = {1};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n链表: ";
    printList(head4);
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head4) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head4) ? "是回文" : "不是回文") << endl;
    
    // 测试用例5: []
    ListNode* head5 = nullptr;
    cout << "\n测试用例5:\n链表: 空链表" << endl;
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head5) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head5) ? "是回文" : "不是回文") << endl;
    
    // 测试用例6: [1,2,3,3,2,1]
    vector<int> nums6 = {1, 2, 3, 3, 2, 1};
    ListNode* head6 = buildList(nums6);
    cout << "\n测试用例6:\n链表: ";
    printList(head6);
    cout << "方法1（数组）结果: " << (solution.isPalindrome(head6) ? "是回文" : "不是回文") << endl;
    cout << "方法2（最优解法）结果: " << (solution.isPalindromeOptimal(head6) ? "是回文" : "不是回文") << endl;
    
    // 释放内存
    freeList(head1);
    freeList(head2);
    freeList(head3);
    freeList(head4);
    freeList(head6);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 234. 回文链表
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 1. 数组存储法：将链表节点值存储到数组，然后使用双指针判断数组是否为回文
 * 2. 最优解法：使用快慢指针找到中点，反转后半部分，比较前后两部分，再恢复原链表
 * 3. 递归解法：利用函数调用栈模拟栈结构，从链表两端向中间比较
 * 4. 栈方法：显式使用栈存储前半部分节点值，然后与后半部分比较
 * 
 * 时间复杂度：
 * - 数组存储法：O(n)
 * - 最优解法：O(n)
 * - 递归解法：O(n)
 * - 栈方法：O(n)
 * 
 * 空间复杂度：
 * - 数组存储法：O(n)
 * - 最优解法：O(1)，原地操作（除了几个指针变量）
 * - 递归解法：O(n)，递归调用栈的深度
 * - 栈方法：O(n/2) = O(n)
 * 
 * 最优解：方法2（快慢指针+反转后半部分），时间复杂度O(n)，空间复杂度O(1)
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 链表长度奇偶性处理：奇数长度时需要跳过中间节点
 * 3. 链表恢复：在工程实践中，应该恢复原链表结构，避免对调用者造成影响
 * 4. 性能优化：避免使用额外空间，特别是对于大型链表
 * 
 * 与机器学习等领域的联系：
 * 1. 回文检测在自然语言处理中用于检测回文句子或单词
 * 2. 链表反转技术在序列数据处理中有广泛应用
 * 3. 双指针技术在滑动窗口和搜索算法中常见
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用指针操作
 * Java: 提供对象引用，自动内存管理
 * Python: 简洁的语法，使用对象引用
 * 
 * 算法深度分析：
 * 本题的关键在于如何在O(1)额外空间内完成回文检测。最优解法巧妙地结合了快慢指针找中点和链表反转操作，既满足了空间复杂度的要求，又保持了时间复杂度为O(n)。特别值得注意的是，在工程实践中恢复原链表结构是一个良好的习惯，可以避免对调用者造成意外影响。
 */