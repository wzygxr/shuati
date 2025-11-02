// 删除链表的倒数第 N 个结点 - LeetCode 19
// 测试链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
#include <iostream>
#include <vector>
#include <stack>
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
    // 方法1: 双指针（快慢指针）
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        // 创建哑节点，简化头节点的处理
        ListNode dummy(0);
        dummy.next = head;
        
        // 初始化快慢指针，都指向哑节点
        ListNode* fast = &dummy;
        ListNode* slow = &dummy;
        
        // 快指针先移动 n+1 步
        for (int i = 0; i <= n; i++) {
            fast = fast->next;
        }
        
        // 同时移动快慢指针，直到快指针到达链表末尾
        while (fast) {
            fast = fast->next;
            slow = slow->next;
        }
        
        // 此时慢指针指向要删除节点的前一个节点
        ListNode* toDelete = slow->next;
        slow->next = slow->next->next; // 跳过要删除的节点
        delete toDelete; // 释放内存
        
        return dummy.next;
    }
    
    // 方法2: 栈（后进先出特性）
    ListNode* removeNthFromEndStack(ListNode* head, int n) {
        ListNode dummy(0);
        dummy.next = head;
        
        // 将所有节点入栈
        stack<ListNode*> stk;
        ListNode* curr = &dummy;
        while (curr) {
            stk.push(curr);
            curr = curr->next;
        }
        
        // 弹出n个节点
        for (int i = 0; i < n; i++) {
            stk.pop();
        }
        
        // 此时栈顶是要删除节点的前一个节点
        ListNode* prev = stk.top();
        ListNode* toDelete = prev->next;
        prev->next = prev->next->next;
        delete toDelete; // 释放内存
        
        return dummy.next;
    }
    
    // 方法3: 计算链表长度
    ListNode* removeNthFromEndLength(ListNode* head, int n) {
        ListNode dummy(0);
        dummy.next = head;
        
        // 计算链表长度
        int length = 0;
        ListNode* curr = head;
        while (curr) {
            length++;
            curr = curr->next;
        }
        
        // 找到要删除节点的前一个节点
        curr = &dummy;
        for (int i = 0; i < length - n; i++) {
            curr = curr->next;
        }
        
        // 删除节点
        ListNode* toDelete = curr->next;
        curr->next = curr->next->next;
        delete toDelete; // 释放内存
        
        return dummy.next;
    }
    
    // 方法4: 递归解法
    ListNode* removeNthFromEndRecursive(ListNode* head, int n) {
        int count = removeNthHelper(head, n);
        // 如果删除的是头节点
        if (count == n) {
            ListNode* temp = head;
            head = head->next;
            delete temp;
        }
        return head;
    }
    
private:
    // 递归辅助函数，返回从当前节点到链表末尾的距离
    int removeNthHelper(ListNode* curr, int n) {
        if (!curr) return 0;
        
        int distance = removeNthHelper(curr->next, n) + 1;
        
        // 如果当前节点的下一个节点是要删除的节点
        if (distance == n + 1) {
            ListNode* toDelete = curr->next;
            curr->next = curr->next->next;
            delete toDelete;
        }
        
        return distance;
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
    
    // 测试用例1: [1,2,3,4,5], n=2
    vector<int> nums1 = {1, 2, 3, 4, 5};
    ListNode* head1 = buildList(nums1);
    int n1 = 2;
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    cout << "删除倒数第" << n1 << "个节点" << endl;
    
    // 测试双指针方法
    ListNode* head1_copy1 = copyList(head1);
    ListNode* result1 = solution.removeNthFromEnd(head1_copy1, n1);
    cout << "双指针方法结果: ";
    printList(result1);
    
    // 测试栈方法
    ListNode* head1_copy2 = copyList(head1);
    ListNode* result1_stack = solution.removeNthFromEndStack(head1_copy2, n1);
    cout << "栈方法结果: ";
    printList(result1_stack);
    
    // 测试计算长度方法
    ListNode* head1_copy3 = copyList(head1);
    ListNode* result1_length = solution.removeNthFromEndLength(head1_copy3, n1);
    cout << "计算长度方法结果: ";
    printList(result1_length);
    
    // 测试递归方法
    ListNode* head1_copy4 = copyList(head1);
    ListNode* result1_recursive = solution.removeNthFromEndRecursive(head1_copy4, n1);
    cout << "递归方法结果: ";
    printList(result1_recursive);
    
    // 测试用例2: [1], n=1
    vector<int> nums2 = {1};
    ListNode* head2 = buildList(nums2);
    int n2 = 1;
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    cout << "删除倒数第" << n2 << "个节点" << endl;
    
    ListNode* result2 = solution.removeNthFromEnd(head2, n2);
    cout << "双指针方法结果: ";
    if (result2) {
        printList(result2);
    } else {
        cout << "空链表" << endl;
    }
    
    // 测试用例3: [1,2], n=1
    vector<int> nums3 = {1, 2};
    ListNode* head3 = buildList(nums3);
    int n3 = 1;
    cout << "\n测试用例3:\n原始链表: ";
    printList(head3);
    cout << "删除倒数第" << n3 << "个节点" << endl;
    
    ListNode* result3 = solution.removeNthFromEnd(head3, n3);
    cout << "双指针方法结果: ";
    printList(result3);
    
    // 测试用例4: [1,2,3], n=3 (删除头节点)
    vector<int> nums4 = {1, 2, 3};
    ListNode* head4 = buildList(nums4);
    int n4 = 3;
    cout << "\n测试用例4:\n原始链表: ";
    printList(head4);
    cout << "删除倒数第" << n4 << "个节点" << endl;
    
    ListNode* result4 = solution.removeNthFromEnd(head4, n4);
    cout << "双指针方法结果: ";
    printList(result4);
    
    // 测试用例5: [1,2,3,4], n=2
    vector<int> nums5 = {1, 2, 3, 4};
    ListNode* head5 = buildList(nums5);
    int n5 = 2;
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    cout << "删除倒数第" << n5 << "个节点" << endl;
    
    ListNode* result5 = solution.removeNthFromEnd(head5, n5);
    cout << "双指针方法结果: ";
    printList(result5);
    
    // 释放内存
    freeList(head1);
    freeList(result1);
    freeList(result1_stack);
    freeList(result1_length);
    freeList(result1_recursive);
    // result2已经是空，无需释放
    freeList(result3);
    freeList(result4);
    freeList(result5);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 19. 删除链表的倒数第 N 个结点
 * 来源：LeetCode、LintCode、牛客网、剑指Offer
 * 
 * 题目描述：
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * 
 * 解题思路：
 * 1. 双指针法：使用快慢指针，快指针先走n+1步，然后同时移动，当快指针到达末尾时，慢指针指向要删除节点的前一个节点
 * 2. 栈法：利用栈的后进先出特性，将所有节点入栈后弹出n个，栈顶即为要删除节点的前一个节点
 * 3. 计算长度法：先计算链表长度，然后找到要删除节点的前一个节点
 * 4. 递归法：利用递归回溯的特性，在回溯时计数，找到要删除的节点
 * 
 * 时间复杂度：
 * - 双指针法：O(L)，其中L是链表长度
 * - 栈法：O(L)
 * - 计算长度法：O(L)
 * - 递归法：O(L)
 * 
 * 空间复杂度：
 * - 双指针法：O(1)
 * - 栈法：O(L)，需要存储所有节点
 * - 计算长度法：O(1)
 * - 递归法：O(L)，递归调用栈的深度
 * 
 * 最优解：双指针法，时间复杂度O(L)，空间复杂度O(1)
 * 双指针法只需要一次遍历，并且不需要额外的数据结构，是最高效的解法
 * 
 * 工程化考量：
 * 1. 使用哑节点可以简化对头节点的处理
 * 2. 注意内存管理，在C++中需要手动删除被移除的节点
 * 3. 边界情况处理：空链表、只有一个节点的链表、删除头节点
 * 4. 输入验证：确保n是有效的（1 <= n <= 链表长度）
 * 
 * 与机器学习等领域的联系：
 * 1. 链表操作是数据结构基础，在很多算法中都会用到
 * 2. 双指针技术在滑动窗口、链表遍历等场景中有广泛应用
 * 3. 栈的后进先出特性在表达式求值、括号匹配等问题中很有用
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用delete释放被删除的节点
 * Java: 有自动内存管理（垃圾回收），不需要手动释放内存
 * Python: 通过引用计数进行内存管理，同样不需要手动释放
 * 
 * 算法深度分析：
 * 双指针法是解决链表倒数问题的经典方法，它利用快慢指针之间的固定间隔，巧妙地在一次遍历中找到目标节点。这种方法避免了需要两次遍历链表的情况，大大提高了效率。使用哑节点是一个重要的技巧，它可以统一处理头节点和其他节点的删除逻辑，简化代码实现。栈方法虽然直观，但需要额外的空间，在内存受限的环境中不是最佳选择。递归方法代码简洁，但递归调用栈的开销在长链表情况下可能成为问题。
 */