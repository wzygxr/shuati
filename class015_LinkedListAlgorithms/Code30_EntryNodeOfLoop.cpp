// 链表中环的入口节点 - 剑指Offer
// 测试链接: https://leetcode.cn/problems/linked-list-cycle-ii/
#include <iostream>
#include <vector>
#include <unordered_set>
using namespace std;

// 定义链表节点结构
struct ListNode {
    int val;
    ListNode *next;
    ListNode(int x) : val(x), next(nullptr) {}
};

class Solution {
public:
    // 方法1: Floyd的龟兔赛跑算法（快慢指针）
    ListNode* detectCycle(ListNode* head) {
        // 边界情况处理
        if (!head || !head->next) {
            return nullptr; // 空链表或单节点链表，不可能有环
        }
        
        // 第一步：使用快慢指针找到相遇点
        ListNode* slow = head;
        ListNode* fast = head;
        bool hasCycle = false;
        
        while (fast && fast->next) {
            slow = slow->next;           // 慢指针每次移动一步
            fast = fast->next->next;     // 快指针每次移动两步
            
            if (slow == fast) {          // 快慢指针相遇，说明有环
                hasCycle = true;
                break;
            }
        }
        
        // 如果没有环，直接返回nullptr
        if (!hasCycle) {
            return nullptr;
        }
        
        // 第二步：从相遇点和头节点同时出发，相遇点即为环的入口
        slow = head;                  // 重置慢指针到头节点
        while (slow != fast) {        // 两个指针每次都移动一步
            slow = slow->next;
            fast = fast->next;
        }
        
        return slow;                  // 返回环的入口节点
    }
    
    // 方法2: 使用哈希表（空间换时间）
    ListNode* detectCycleHashSet(ListNode* head) {
        unordered_set<ListNode*> visited; // 用于存储已访问的节点
        ListNode* curr = head;
        
        while (curr) {
            // 如果当前节点已经被访问过，说明找到了环的入口
            if (visited.count(curr)) {
                return curr;
            }
            
            // 将当前节点加入已访问集合
            visited.insert(curr);
            curr = curr->next;
        }
        
        return nullptr; // 遍历完整个链表都没有找到环
    }
    
    // 方法3: 修改节点标记法（不推荐，会修改链表结构）
    ListNode* detectCycleMark(ListNode* head) {
        ListNode* curr = head;
        
        while (curr) {
            // 检查当前节点是否已被标记（使用一个特殊值表示已访问）
            if (curr->val == INT_MIN) { // 假设INT_MIN不会在正常值范围内出现
                return curr;            // 找到环的入口
            }
            
            // 标记当前节点
            curr->val = INT_MIN;
            curr = curr->next;
        }
        
        return nullptr; // 没有环
    }
    
    // 方法4: 节点计数法（统计链表长度）
    ListNode* detectCycleCount(ListNode* head) {
        // 边界情况处理
        if (!head || !head->next) {
            return nullptr;
        }
        
        // 找到环中的一个节点
        ListNode* slow = head;
        ListNode* fast = head;
        bool hasCycle = false;
        
        while (fast && fast->next) {
            slow = slow->next;
            fast = fast->next->next;
            
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        
        if (!hasCycle) {
            return nullptr;
        }
        
        // 计算环的长度
        int cycleLength = 1;
        ListNode* temp = slow->next;
        while (temp != slow) {
            cycleLength++;
            temp = temp->next;
        }
        
        // 使用两个指针，第一个指针先走环的长度步
        ListNode* first = head;
        ListNode* second = head;
        
        // 第一个指针先走cycleLength步
        for (int i = 0; i < cycleLength; i++) {
            first = first->next;
        }
        
        // 两个指针同时前进，相遇点就是环的入口
        while (first != second) {
            first = first->next;
            second = second->next;
        }
        
        return first;
    }
};

// 辅助函数：构建链表（无环）
ListNode* buildList(vector<int>& nums) {
    ListNode* dummy = new ListNode(0);
    ListNode* curr = dummy;
    for (int num : nums) {
        curr->next = new ListNode(num);
        curr = curr->next;
    }
    return dummy->next;
}

// 辅助函数：构建带环的链表
ListNode* buildListWithCycle(vector<int>& nums, int pos) {
    if (nums.empty()) {
        return nullptr;
    }
    
    ListNode* head = new ListNode(nums[0]);
    ListNode* curr = head;
    ListNode* cycleEntry = nullptr;
    
    // 记录环的入口位置
    if (pos == 0) {
        cycleEntry = head;
    }
    
    // 构建链表
    for (int i = 1; i < nums.size(); i++) {
        curr->next = new ListNode(nums[i]);
        curr = curr->next;
        if (i == pos) {
            cycleEntry = curr;
        }
    }
    
    // 创建环
    if (pos != -1) {
        curr->next = cycleEntry;
    }
    
    return head;
}

// 辅助函数：打印链表（小心环导致的无限循环）
void printList(ListNode* head, int maxNodes = 20) {
    int count = 0;
    while (head && count < maxNodes) {
        cout << head->val;
        if (head->next) {
            cout << " -> ";
        }
        head = head->next;
        count++;
    }
    if (count == maxNodes) {
        cout << " -> ... (可能有环)";
    }
    cout << endl;
}

// 辅助函数：释放链表内存（小心环导致的无限循环）
void freeList(ListNode* head, int maxNodes = 20) {
    int count = 0;
    while (head && count < maxNodes) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
        count++;
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: 带环链表 [3,2,0,-4]，环的位置在索引1（节点值为2）
    vector<int> nums1 = {3, 2, 0, -4};
    ListNode* head1 = buildListWithCycle(nums1, 1);
    cout << "测试用例1:\n带环链表 (入口在索引1): ";
    printList(head1);
    
    ListNode* result1 = solution.detectCycle(head1);
    if (result1) {
        cout << "快慢指针法检测到环的入口值: " << result1->val << endl;
    } else {
        cout << "快慢指针法未检测到环" << endl;
    }
    
    // 测试用例2: 带环链表 [1,2]，环的位置在索引0（节点值为1）
    vector<int> nums2 = {1, 2};
    ListNode* head2 = buildListWithCycle(nums2, 0);
    cout << "\n测试用例2:\n带环链表 (入口在索引0): ";
    printList(head2);
    
    ListNode* result2 = solution.detectCycleHashSet(head2);
    if (result2) {
        cout << "哈希表法检测到环的入口值: " << result2->val << endl;
    } else {
        cout << "哈希表法未检测到环" << endl;
    }
    
    // 测试用例3: 无环链表 [1]
    vector<int> nums3 = {1};
    ListNode* head3 = buildListWithCycle(nums3, -1);
    cout << "\n测试用例3:\n无环链表: ";
    printList(head3);
    
    ListNode* result3 = solution.detectCycle(head3);
    if (result3) {
        cout << "快慢指针法检测到环的入口值: " << result3->val << endl;
    } else {
        cout << "快慢指针法未检测到环" << endl;
    }
    
    // 测试用例4: 空链表
    ListNode* head4 = nullptr;
    cout << "\n测试用例4:\n空链表" << endl;
    
    ListNode* result4 = solution.detectCycle(head4);
    if (result4) {
        cout << "快慢指针法检测到环的入口值: " << result4->val << endl;
    } else {
        cout << "快慢指针法未检测到环" << endl;
    }
    
    // 测试用例5: 带环链表 [1]，自环
    vector<int> nums5 = {1};
    ListNode* head5 = buildListWithCycle(nums5, 0);
    cout << "\n测试用例5:\n带环链表 (自环): ";
    printList(head5);
    
    ListNode* result5 = solution.detectCycleCount(head5);
    if (result5) {
        cout << "节点计数法检测到环的入口值: " << result5->val << endl;
    } else {
        cout << "节点计数法未检测到环" << endl;
    }
    
    // 释放内存
    freeList(head1);
    freeList(head2);
    freeList(head3);
    freeList(head5);
    
    return 0;
}

/*
 * 题目扩展：链表中环的入口节点 - 剑指Offer
 * 来源：LeetCode、剑指Offer、牛客网
 * 
 * 题目描述：
 * 给定一个链表，返回链表开始入环的第一个节点。如果链表无环，则返回nullptr。
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从0开始）。
 * 如果 pos 是 -1，则在该链表中没有环。
 * 
 * 解题思路（Floyd的龟兔赛跑算法）：
 * 1. 使用快慢指针判断链表中是否存在环
 * 2. 如果存在环，找到快慢指针的相遇点
 * 3. 重置一个指针到链表头，另一个保持在相遇点，两个指针同时以相同速度前进
 * 4. 两个指针再次相遇的点就是环的入口
 * 
 * 数学证明：
 * 设链表头到环入口的距离为a，环入口到相遇点的距离为b，相遇点到环入口的距离为c
 * 当快慢指针相遇时，慢指针走了a+b步，快指针走了a+b+n(b+c)步（n为快指针在环中多走的圈数）
 * 由于快指针速度是慢指针的2倍，所以：2(a+b) = a+b+n(b+c)
 * 化简得：a+b = n(b+c)
 * 进一步：a = n(b+c) - b = (n-1)(b+c) + c
 * 当n=1时，a = c，即链表头到环入口的距离等于相遇点到环入口的距离
 * 
 * 时间复杂度：
 * - Floyd算法：O(n) - 寻找相遇点和环入口都需要线性时间
 * - 哈希表法：O(n) - 需要遍历链表一次
 * 
 * 空间复杂度：
 * - Floyd算法：O(1) - 只使用常数额外空间
 * - 哈希表法：O(n) - 需要存储已访问的节点
 * 
 * 最优解：Floyd的龟兔赛跑算法，因为它的空间复杂度更低
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表、无环链表
 * 2. 性能优化：避免使用可能导致栈溢出的递归
 * 3. 内存管理：在C++中需要正确管理链表节点的内存
 * 4. 代码可读性：清晰的注释和命名
 * 
 * 与机器学习等领域的联系：
 * 1. 环检测算法在图论中有广泛应用
 * 2. 在数据流分析和图算法中类似的双指针技术很常见
 * 3. 在机器学习的某些算法中，也会使用到循环检测
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用nullptr表示空指针
 * Java: 自动内存管理，使用null表示空引用
 * Python: 自动内存管理，使用None表示空引用
 * 
 * 算法深度分析：
 * Floyd的龟兔赛跑算法是一个非常优雅的解决方案，它利用了快慢指针的特性和数学证明，在不需要额外空间的情况下找到了环的入口。
 */