// 两数相加II - LeetCode 445
// 测试链接: https://leetcode.cn/problems/add-two-numbers-ii/
#include <iostream>
#include <vector>
#include <stack>
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
    // 方法1: 使用栈来存储两个链表的节点值
    ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        // 使用栈来存储两个链表的节点值（先进后出，方便从低位相加）
        stack<int> s1, s2;
        
        // 将两个链表的节点值分别入栈
        while (l1) {
            s1.push(l1->val);
            l1 = l1->next;
        }
        while (l2) {
            s2.push(l2->val);
            l2 = l2->next;
        }
        
        int carry = 0; // 进位
        ListNode* result = nullptr; // 结果链表的头节点
        
        // 当栈不为空或者有进位时，继续处理
        while (!s1.empty() || !s2.empty() || carry) {
            int sum = carry; // 当前位的和初始化为进位
            
            // 从栈中弹出元素并加到和中
            if (!s1.empty()) {
                sum += s1.top();
                s1.pop();
            }
            if (!s2.empty()) {
                sum += s2.top();
                s2.pop();
            }
            
            // 计算当前位的值和新的进位
            carry = sum / 10;
            int currentVal = sum % 10;
            
            // 创建新节点，并将其插入到结果链表的头部
            ListNode* newNode = new ListNode(currentVal);
            newNode->next = result;
            result = newNode;
        }
        
        return result;
    }
    
    // 方法2: 反转链表后相加，再反转结果（改变原链表结构）
    ListNode* addTwoNumbersReverse(ListNode* l1, ListNode* l2) {
        // 反转两个链表
        l1 = reverseList(l1);
        l2 = reverseList(l2);
        
        // 普通的两数相加（低位在前）
        ListNode* sumList = addTwoNumbersNormal(l1, l2);
        
        // 反转结果链表得到最终答案
        ListNode* result = reverseList(sumList);
        
        return result;
    }
    
    // 方法3: 先计算两个数的长度，对齐后相加（递归实现）
    ListNode* addTwoNumbersRecursive(ListNode* l1, ListNode* l2) {
        // 计算两个链表的长度
        int len1 = getLength(l1);
        int len2 = getLength(l2);
        
        ListNode* result = nullptr;
        int carry = 0;
        
        // 根据两个链表的长度决定如何调用递归函数
        if (len1 >= len2) {
            result = addHelper(l1, l2, len1 - len2, carry);
        } else {
            result = addHelper(l2, l1, len2 - len1, carry);
        }
        
        // 如果还有进位，添加一个新的头节点
        if (carry > 0) {
            result = new ListNode(carry, result);
        }
        
        return result;
    }
    
private:
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
    
    // 辅助函数：普通的两数相加（低位在前）
    ListNode* addTwoNumbersNormal(ListNode* l1, ListNode* l2) {
        ListNode dummy(0);
        ListNode* curr = &dummy;
        int carry = 0;
        
        while (l1 || l2 || carry) {
            int sum = carry;
            if (l1) {
                sum += l1->val;
                l1 = l1->next;
            }
            if (l2) {
                sum += l2->val;
                l2 = l2->next;
            }
            
            carry = sum / 10;
            curr->next = new ListNode(sum % 10);
            curr = curr->next;
        }
        
        return dummy.next;
    }
    
    // 辅助函数：计算链表长度
    int getLength(ListNode* head) {
        int length = 0;
        while (head) {
            length++;
            head = head->next;
        }
        return length;
    }
    
    // 辅助函数：递归相加（l1的长度大于等于l2的长度，offset为长度差）
    ListNode* addHelper(ListNode* l1, ListNode* l2, int offset, int& carry) {
        if (!l1) {
            return nullptr;
        }
        
        ListNode* node;
        int sum;
        
        if (offset > 0) {
            // l1比l2长，先递归处理l1的下一个节点
            node = addHelper(l1->next, l2, offset - 1, carry);
            sum = l1->val + carry;
        } else {
            // l1和l2对齐，递归处理两个链表的下一个节点
            node = addHelper(l1->next, l2->next, 0, carry);
            sum = l1->val + l2->val + carry;
        }
        
        // 计算当前位的值和新的进位
        carry = sum / 10;
        int currentVal = sum % 10;
        
        // 创建新节点并连接到递归结果的前面
        ListNode* newNode = new ListNode(currentVal);
        newNode->next = node;
        
        return newNode;
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
    
    // 测试用例1: l1 = [7,2,4,3], l2 = [5,6,4]
    vector<int> nums1 = {7, 2, 4, 3};
    vector<int> nums2 = {5, 6, 4};
    ListNode* l1 = buildList(nums1);
    ListNode* l2 = buildList(nums2);
    
    cout << "测试用例1:\nl1: ";
    printList(l1);
    cout << "l2: ";
    printList(l2);
    
    ListNode* result1 = solution.addTwoNumbers(l1, l2);
    cout << "方法1（栈）相加结果: ";
    printList(result1);
    
    // 重新构建链表（因为方法1不修改原链表）
    ListNode* l1_copy = buildList(nums1);
    ListNode* l2_copy = buildList(nums2);
    
    // 注意：方法2会修改原链表结构，我们在这里创建新的副本进行测试
    ListNode* l1_rev = buildList(nums1);
    ListNode* l2_rev = buildList(nums2);
    ListNode* result2 = solution.addTwoNumbersReverse(l1_rev, l2_rev);
    cout << "方法2（反转链表）相加结果: ";
    printList(result2);
    
    ListNode* result3 = solution.addTwoNumbersRecursive(l1_copy, l2_copy);
    cout << "方法3（递归）相加结果: ";
    printList(result3);
    
    // 测试用例2: l1 = [2,4,3], l2 = [5,6,4]
    vector<int> nums3 = {2, 4, 3};
    vector<int> nums4 = {5, 6, 4};
    ListNode* l3 = buildList(nums3);
    ListNode* l4 = buildList(nums4);
    
    cout << "\n测试用例2:\nl1: ";
    printList(l3);
    cout << "l2: ";
    printList(l4);
    
    ListNode* result4 = solution.addTwoNumbers(l3, l4);
    cout << "方法1（栈）相加结果: ";
    printList(result4);
    
    // 测试用例3: l1 = [0], l2 = [0]
    vector<int> nums5 = {0};
    vector<int> nums6 = {0};
    ListNode* l5 = buildList(nums5);
    ListNode* l6 = buildList(nums6);
    
    cout << "\n测试用例3:\nl1: ";
    printList(l5);
    cout << "l2: ";
    printList(l6);
    
    ListNode* result5 = solution.addTwoNumbers(l5, l6);
    cout << "方法1（栈）相加结果: ";
    printList(result5);
    
    // 测试用例4: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
    vector<int> nums7 = {9,9,9,9,9,9,9};
    vector<int> nums8 = {9,9,9,9};
    ListNode* l7 = buildList(nums7);
    ListNode* l8 = buildList(nums8);
    
    cout << "\n测试用例4:\nl1: ";
    printList(l7);
    cout << "l2: ";
    printList(l8);
    
    ListNode* result6 = solution.addTwoNumbers(l7, l8);
    cout << "方法1（栈）相加结果: ";
    printList(result6);
    
    // 释放内存
    freeList(l1);
    freeList(l2);
    freeList(result1);
    freeList(l1_copy);
    freeList(l2_copy);
    freeList(result2);
    freeList(result3);
    freeList(l3);
    freeList(l4);
    freeList(result4);
    freeList(l5);
    freeList(l6);
    freeList(result5);
    freeList(l7);
    freeList(l8);
    freeList(result6);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 445. 两数相加II
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。
 * 它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 * 
 * 解题思路：
 * 1. 栈方法：使用栈存储两个链表的节点值，然后从栈顶开始相加，将结果插入到新链表的头部
 * 2. 反转链表法：先反转两个链表，使用普通的两数相加方法，再反转结果链表
 * 3. 递归法：先计算两个链表的长度，对齐后递归相加，处理进位
 * 
 * 时间复杂度：
 * - 栈方法：O(n + m)，其中n和m是两个链表的长度
 * - 反转链表法：O(n + m)
 * - 递归法：O(n + m)
 * 
 * 空间复杂度：
 * - 栈方法：O(n + m)，需要两个栈存储节点值
 * - 反转链表法：O(1)，只使用常数额外空间（不考虑结果链表）
 * - 递归法：O(max(n, m))，递归调用栈的深度
 * 
 * 最优解：栈方法，不需要修改原链表结构，同时实现相对简单
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表、进位处理
 * 2. 数据完整性：是否允许修改原链表结构
 * 3. 内存管理：在C++中需要正确创建和释放链表节点
 * 4. 性能优化：避免不必要的链表遍历
 * 
 * 与机器学习等领域的联系：
 * 1. 在大数运算中，类似的链表结构常用于表示超出基本数据类型范围的数
 * 2. 栈的使用在表达式求值、递归实现中有广泛应用
 * 3. 在自然语言处理中，序列相加的概念与这里的数字相加有相似之处
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用new创建节点，delete释放内存
 * Java: 自动内存管理，使用new创建对象
 * Python: 自动内存管理，对象由解释器管理
 * 
 * 算法深度分析：
 * 本题的关键在于如何处理高位在前的链表相加问题。栈提供了一种自然的后进先出机制，使得我们可以从低位开始相加。递归方法则巧妙地利用了函数调用栈来实现相同的效果。
 */