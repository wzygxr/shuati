// 在链表中插入最大公约数 - LeetCode 2807
// 测试链接: https://leetcode.cn/problems/insert-greatest-common-divisors-in-linked-list/
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
    // 主函数：在每对相邻节点之间插入它们的最大公约数
    ListNode* insertGreatestCommonDivisors(ListNode* head) {
        // 处理边界情况
        if (!head || !head->next) {
            return head; // 空链表或只有一个节点，无需插入
        }
        
        // 遍历链表，在每对相邻节点之间插入GCD节点
        ListNode* curr = head;
        while (curr && curr->next) {
            // 计算当前节点和下一个节点值的最大公约数
            int gcdVal = calculateGCD(curr->val, curr->next->val);
            
            // 创建新节点存储GCD值
            ListNode* gcdNode = new ListNode(gcdVal);
            
            // 插入新节点到当前节点和下一个节点之间
            gcdNode->next = curr->next;
            curr->next = gcdNode;
            
            // 移动到下一对需要处理的节点（跳过新插入的GCD节点）
            curr = gcdNode->next;
        }
        
        return head;
    }
    
    // 方法1: 欧几里得算法求最大公约数（递归版）
    int calculateGCD(int a, int b) {
        // 确保a和b都是正数
        a = abs(a);
        b = abs(b);
        
        // 欧几里得算法的递归实现
        if (b == 0) {
            return a;
        }
        return calculateGCD(b, a % b);
    }
    
    // 方法2: 欧几里得算法求最大公约数（迭代版）
    int calculateGCDIterative(int a, int b) {
        // 确保a和b都是正数
        a = abs(a);
        b = abs(b);
        
        // 欧几里得算法的迭代实现
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    // 方法3: 更相减损术求最大公约数
    int calculateGCDSubtraction(int a, int b) {
        // 确保a和b都是正数
        a = abs(a);
        b = abs(b);
        
        // 如果a和b相等，返回a
        if (a == b) {
            return a;
        }
        
        // 更相减损术的迭代实现
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
        return a;
    }
    
    // 使用C++标准库的gcd函数（C++17及以上）
    // 注意：某些编译器可能需要包含<numeric>头文件
    int calculateGCDStd(int a, int b) {
        return gcd(abs(a), abs(b));
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
    
    // 测试用例1: [18,6,10,3]
    vector<int> nums1 = {18, 6, 10, 3};
    ListNode* head1 = buildList(nums1);
    cout << "测试用例1:\n原始链表: ";
    printList(head1);
    ListNode* result1 = solution.insertGreatestCommonDivisors(head1);
    cout << "插入GCD后: ";
    printList(result1);
    freeList(result1);
    
    // 测试用例2: [7]
    vector<int> nums2 = {7};
    ListNode* head2 = buildList(nums2);
    cout << "\n测试用例2:\n原始链表: ";
    printList(head2);
    ListNode* result2 = solution.insertGreatestCommonDivisors(head2);
    cout << "插入GCD后: ";
    printList(result2);
    freeList(result2);
    
    // 测试用例3: [2,4,6,8]
    vector<int> nums3 = {2, 4, 6, 8};
    ListNode* head3 = buildList(nums3);
    cout << "\n测试用例3:\n原始链表: ";
    printList(head3);
    ListNode* result3 = solution.insertGreatestCommonDivisors(head3);
    cout << "插入GCD后: ";
    printList(result3);
    freeList(result3);
    
    // 测试用例4: [1,2,3,4,5]
    vector<int> nums4 = {1, 2, 3, 4, 5};
    ListNode* head4 = buildList(nums4);
    cout << "\n测试用例4:\n原始链表: ";
    printList(head4);
    ListNode* result4 = solution.insertGreatestCommonDivisors(head4);
    cout << "插入GCD后: ";
    printList(result4);
    freeList(result4);
    
    // 测试用例5: [0,0,0]
    vector<int> nums5 = {0, 0, 0};
    ListNode* head5 = buildList(nums5);
    cout << "\n测试用例5:\n原始链表: ";
    printList(head5);
    ListNode* result5 = solution.insertGreatestCommonDivisors(head5);
    cout << "插入GCD后: ";
    printList(result5);
    freeList(result5);
    
    // 测试用例6: [3,5,7,9]
    vector<int> nums6 = {3, 5, 7, 9};
    ListNode* head6 = buildList(nums6);
    cout << "\n测试用例6:\n原始链表: ";
    printList(head6);
    
    // 测试迭代版本的GCD计算
    Solution sol;
    ListNode* temp = head6;
    while (temp && temp->next) {
        int gcdVal = sol.calculateGCDIterative(temp->val, temp->next->val);
        cout << "GCD(" << temp->val << ", " << temp->next->val << ") = " << gcdVal << endl;
        temp = temp->next;
    }
    
    ListNode* result6 = solution.insertGreatestCommonDivisors(head6);
    cout << "插入GCD后: ";
    printList(result6);
    freeList(result6);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 2807. 在链表中插入最大公约数
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 给你一个链表的头节点 head ，请你在链表的每对相邻节点之间插入一个新节点，新节点的值为这两个相邻节点值的最大公约数（GCD）。
 * 返回插入后的链表的头节点。
 * 
 * 解题思路：
 * 1. 遍历链表，对于每对相邻节点：
 *    a. 计算它们的最大公约数
 *    b. 创建一个新节点存储GCD值
 *    c. 插入新节点到这两个节点之间
 *    d. 移动到下一对节点（跳过新插入的节点）
 * 
 * 最大公约数计算方法：
 * 1. 欧几里得算法（辗转相除法）：利用 a, b 的最大公约数等于 b, a mod b 的最大公约数
 * 2. 更相减损术：不断用较大数减去较小数，直到两数相等
 * 3. 质因数分解法：分解质因数后取公共质因数的最小指数
 * 
 * 时间复杂度：O(n) - 遍历链表一次，GCD计算的时间复杂度为O(log min(a,b))
 * 空间复杂度：O(1) - 只使用常数额外空间（不考虑新创建的节点）
 * 
 * 最优解：欧几里得算法的迭代版本，因为迭代比递归更高效，避免了函数调用栈的开销
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 异常处理：处理负数和零的情况
 * 3. 内存管理：在C++中需要正确创建和释放节点
 * 4. 性能优化：选择高效的GCD计算方法
 * 
 * 与数学的联系：
 * 最大公约数在数论中有重要地位，在密码学、约分等领域有广泛应用
 * 
 * 与机器学习等领域的联系：
 * 1. 在特征工程中，可能需要计算特征之间的相关性和公约数
 * 2. 在图像处理中，图像缩放和特征提取可能用到类似的数学运算
 * 3. 在推荐系统中，相似度计算可能涉及到向量的公约数分析
 * 
 * 语言特性差异：
 * C++: 可以使用标准库的gcd函数（C++17及以上），或自己实现
 * Java: 需要自己实现GCD算法
 * Python: 可以使用math.gcd函数
 * 
 * 算法深度分析：
 * 欧几里得算法的核心思想是利用除法和取模操作高效地缩小问题规模。它的时间复杂度是O(log min(a,b))，远优于更相减损术的O(max(a,b))。
 */