// 重排链表
// 测试链接：https://leetcode.cn/problems/reorder-list/

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Code19_ReorderList {
public:
    /**
     * 重排链表
     * @param head 链表头节点
     * 
     * 解题思路：
     * 1. 找到链表中点，将链表分为两部分
     * 2. 反转后半部分链表
     * 3. 合并前半部分和反转后的后半部分
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    void reorderList(ListNode* head) {
        if (head == nullptr || head->next == nullptr) {
            return;
        }
        
        // 1. 找到链表中点
        ListNode* mid = findMiddle(head);
        
        // 2. 反转后半部分链表
        ListNode* secondHalf = reverseList(mid->next);
        mid->next = nullptr; // 断开链表
        
        // 3. 合并前半部分和反转后的后半部分
        mergeLists(head, secondHalf);
    }
    
private:
    /**
     * 找到链表中点（快慢指针法）
     * @param head 链表头节点
     * @return 链表中点
     */
    ListNode* findMiddle(ListNode* head) {
        ListNode* slow = head;
        ListNode* fast = head;
        
        while (fast->next != nullptr && fast->next->next != nullptr) {
            slow = slow->next;
            fast = fast->next->next;
        }
        
        return slow;
    }
    
    /**
     * 反转链表
     * @param head 链表头节点
     * @return 反转后的链表头节点
     */
    ListNode* reverseList(ListNode* head) {
        ListNode* prev = nullptr;
        ListNode* current = head;
        
        while (current != nullptr) {
            ListNode* next = current->next;
            current->next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    /**
     * 合并两个链表
     * @param first 第一个链表
     * @param second 第二个链表
     */
    void mergeLists(ListNode* first, ListNode* second) {
        while (second != nullptr) {
            ListNode* temp1 = first->next;
            ListNode* temp2 = second->next;
            
            first->next = second;
            second->next = temp1;
            
            first = temp1;
            second = temp2;
        }
    }
    
    /*
     * 题目扩展：LeetCode 143. 重排链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定一个单链表 L 的头节点 head ，单链表 L 表示为：
     * L0 → L1 → … → Ln - 1 → Ln
     * 请将其重新排列后变为：
     * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
     * 不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
     * 
     * 解题思路：
     * 1. 找到链表中点，将链表分为两部分
     * 2. 反转后半部分链表
     * 3. 合并前半部分和反转后的后半部分
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、双节点链表
     * 2. 内存管理：C++需要手动管理内存
     * 3. 性能优化：原地操作，空间复杂度最优
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理序列数据时，有时需要重新排列元素
     * 2. 在图神经网络中，节点重排序可能需要类似操作
     * 
     * 语言特性差异：
     * Java: 对象引用操作直观
     * C++: 指针操作更直接但需注意内存安全
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 只有一个节点
     * 3. 只有两个节点
     * 4. 奇数个节点
     * 5. 偶数个节点
     * 
     * 设计的利弊：
     * 1. 优点：将复杂问题分解为多个经典子问题
     * 2. 缺点：需要多次遍历链表
     * 
     * 为什么这么写：
     * 1. 分解问题：将一个复杂问题分解为找中点、反转链表、合并链表三个子问题
     * 2. 复用已有算法：复用经典的找中点和反转链表算法
     * 3. 空间效率：原地操作，不使用额外空间存储节点
     */
};